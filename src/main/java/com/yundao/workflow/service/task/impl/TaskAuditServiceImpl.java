

package com.yundao.workflow.service.task.impl;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.ProcessInstanceDTO;
import com.yundao.workflow.dto.processInstance.ProcessBusinessDto;
import com.yundao.workflow.dto.task.audit.ActionDto;
import com.yundao.workflow.dto.task.audit.TaskActionResDto;
import com.yundao.workflow.dto.task.audit.TaskAuditApplyDto;
import com.yundao.workflow.dto.task.audit.TaskPageReqDto;
import com.yundao.workflow.dto.task.audit.TaskPageResDto;
import com.yundao.workflow.dto.varinst.ApplyUserVariableDto;
import com.yundao.workflow.mapper.ProcessInstanceMapper;
import com.yundao.workflow.mapper.VariableMapper;
import com.yundao.workflow.mapper.base.ProDefTaskActionModelMapper;
import com.yundao.workflow.model.base.ProDefTaskActionModel;
import com.yundao.workflow.model.base.ProDefTaskActionModelExample;
import com.yundao.workflow.service.AbstractService;
import com.yundao.workflow.service.CustomHistoryService;
import com.yundao.workflow.service.task.TaskAuditService;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 上午10:35:17 
 * @author   欧阳利
 * @version   
 */
@Service
public class TaskAuditServiceImpl  extends AbstractService implements TaskAuditService {
	
	private Log logger = LogFactory.getLog(TaskAuditServiceImpl.class);
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private ProcessInstanceMapper processInstanceMapper;
	
	@Autowired
	private ProDefTaskActionModelMapper proDefTaskActionModelMapper;
	
	@Autowired
	private VariableMapper variableMapper;
	@Autowired
	private CustomHistoryService customHistoryService;
	
	
	/**
	 * 查询任务操作和申请信息
	 * @param taskId
	 * @return
	 * @throws BaseException
	 */
	public Result<TaskActionResDto> getTaskActionAndApplyInfo(@RequestParam String taskId)throws BaseException{
		
		Result<ProcessInstanceDTO> processInstanceResult=customHistoryService.selectHistoryProcessInstanceByTaskId(taskId);
		
		TaskActionResDto dto = new TaskActionResDto();
		ProcessInstanceDTO processInstanceDTO=null;
		if(processInstanceResult.getResult() !=null){
			 processInstanceDTO=processInstanceResult.getResult();
			dto.setApplyUserId(NumberUtils.toLong(processInstanceDTO.getStartUserId()));
		    dto.setApplyTime(processInstanceDTO.getStartTime());
		    dto.setTaskId(taskId);
		}
		
		// 设置操作
		ProDefTaskActionModelExample example = new ProDefTaskActionModelExample();
		example.createCriteria().andTenantIdEqualTo(this.getHeaderTenantId())
		.andTaskDefKeyEqualTo(processInstanceDTO.getBusinessKey())
		.andProDefIdEqualTo(processInstanceDTO.getProcessDefinitionKey());
		List<ProDefTaskActionModel> list = proDefTaskActionModelMapper.selectByExample(example);
		
		if(!BooleanUtils.isEmpty(list)){
			List<Integer> actions = new ArrayList<Integer>();
			for(ProDefTaskActionModel model:list){
				actions.add(model.getActionValue());
			}
			dto.setActions(actions);
		}
		return Result.newSuccessResult(dto);
	}
	
	
	
	
	/**
	 * 分页查询待办任务
	 * getPage:
	 * @author: 欧阳利
	 * @param dto
	 * @return
	 * @throws BaseException
	 * @description:
	 */
	@Override
	public Result<PaginationSupport<TaskPageResDto>> getPage(TaskPageReqDto reqDto) throws BaseException {
		Long userId = this.getHeaderUserId();
		Long tenantId = this.getHeaderTenantId();
		if(userId == null || tenantId == null){
			return null;
		}
		
		
		if(!BooleanUtils.isEmpty(reqDto.getCandidateGroups())){
			List<String> candidateGroups = new ArrayList<String>();
			for(String candidateGroup:reqDto.getCandidateGroups()){
				candidateGroups.add(tenantId.toString()+"_"+candidateGroup);
			}
			reqDto.setCandidateGroups(candidateGroups);
		}
		
		
		logger.info("查询待办任务userId"+userId+"||"+"tenantId"+tenantId);
		PaginationSupport<TaskPageResDto> result = reqDto.getPaginationSupport();
		   
		long totalCount = taskService.createTaskQuery()
				.taskTenantId(tenantId.toString())
				.taskCandidateGroupIn(reqDto.getCandidateGroups()).count();
				//.taskDefinitionKeyLike("%audit%").count();
		if(totalCount > 0){
			List<Task> list = taskService.createTaskQuery()
					.taskTenantId(tenantId.toString()).taskCandidateGroupIn(reqDto.getCandidateGroups())
					/** 排序 */
					.orderByTaskCreateTime().desc()// 使用创建时间的升序排列
					.listPage(reqDto.getOffset(), reqDto.getPageSize());
			
			
			
			List<TaskPageResDto> taskResDtos = new ArrayList<TaskPageResDto>();
			for(Task task:list){
				TaskPageResDto taskResDto = new TaskPageResDto();
				taskResDtos.add(taskResDto);
				// 任务ID
				taskResDto.setTaskId(task.getId());
				// 任务名称
				taskResDto.setTaskName(task.getName());
				// 任务节点key
				taskResDto.setTaskDefinitionKey(task.getTaskDefinitionKey());
				// 提交时间
				taskResDto.setCreateTime(task.getCreateTime());
				// 流程实例id
				taskResDto.setProcessInstanceId(task.getProcessInstanceId());
				// 流程定义id
				taskResDto.setProcessDefinitionId(task.getProcessDefinitionId());
				// 执行实例id
				taskResDto.setExecutionId(task.getExecutionId());
				// 上一个任务id
				taskResDto.setParentTaskId(task.getParentTaskId());
				result.setDatas(taskResDtos);
			}
			
			// 设置业务key
			setBusinesskey(taskResDtos);
			
			// 设置操作集合
			setAction(taskResDtos);
			
			// 设置提交人
			setSubmitUserId(taskResDtos);
			
			// 深圳申请人id
			setApplyUserId(taskResDtos);
		}

       result.setTotalCount((int) totalCount);
       return Result.newSuccessResult(result);

	}
	
	
	/**
	 * 设置业务id
	 * setBusinesskey:
	 * @author: 欧阳利
	 * @param taskResDtos
	 * @description:
	 */
	private void setBusinesskey(List<TaskPageResDto> taskResDtos){
		if(BooleanUtils.isEmpty(taskResDtos)){
			return;
		}
		List<String> processInstanceIds = new ArrayList<String>();
		for(TaskPageResDto dto : taskResDtos){
			processInstanceIds.add(dto.getProcessInstanceId());
		}
		
		List<ProcessBusinessDto> list = processInstanceMapper.getProcessBusinessDtos(processInstanceIds);
		if(BooleanUtils.isEmpty(list)){
			return;
		}
		
		for(TaskPageResDto dto : taskResDtos){
			for(ProcessBusinessDto processDto : list){
				if(dto.getProcessInstanceId().equals(processDto.getProcessInstanceId())){
					dto.setBusinesskey(processDto.getBusinessKey());
					dto.setApplyTime(processDto.getStartTime());
					break;
				}
			}
		}
	}
	
	/**
	 * 设置操作集合
	 * setActionValue:
	 * @author: 欧阳利
	 * @param taskResDtos
	 * @description:
	 */
	private void setAction(List<TaskPageResDto> taskResDtos){
		if(BooleanUtils.isEmpty(taskResDtos)){
			return;
		}
		
		List<String> processDefinitionIds = new ArrayList<String>();
		for(TaskPageResDto dto : taskResDtos){
			processDefinitionIds.add(dto.getProcessDefinitionId());
		}
		
		ProDefTaskActionModelExample example = new ProDefTaskActionModelExample();
		example.createCriteria().andProDefIdIn(processDefinitionIds).andTenantIdEqualTo(this.getHeaderTenantId());
		List<ProDefTaskActionModel>  list = proDefTaskActionModelMapper.selectByExample(example);
		
		if(BooleanUtils.isEmpty(list)){
			return;
		}
		
		for(TaskPageResDto dto : taskResDtos){
			List<ActionDto> actionDtos = new ArrayList<ActionDto>();
			dto.setActionDtos(actionDtos);
			for(ProDefTaskActionModel model : list){
				if(dto.getProcessDefinitionId().equals(model.getProDefId()) && dto.getTaskDefinitionKey().equals(model.getTaskDefKey())){
					ActionDto actionDto = new ActionDto();
					actionDto.setActionValue(model.getActionValue());
					actionDtos.add(actionDto);
				}
			}
		}
		
	}
	
	
	/**
	 * 深圳申请人id
	 * setApplyUserId:
	 * @author: 欧阳利
	 * @param taskResDtos
	 * @description:
	 */
	private void setApplyUserId(List<TaskPageResDto> taskResDtos){
		if(BooleanUtils.isEmpty(taskResDtos)){
			return;
		}
		List<String> processInstanceIds = new ArrayList<String>();
		for(TaskPageResDto dto : taskResDtos){
			processInstanceIds.add(dto.getProcessInstanceId());
		}
		
		List<ApplyUserVariableDto> list = variableMapper.getApplyUserVariableDto(processInstanceIds);
		if(BooleanUtils.isEmpty(list)){
			return;
		}
		
		
		for(TaskPageResDto dto : taskResDtos){
			for(ApplyUserVariableDto model : list){
				if(dto.getProcessInstanceId().equals(model.getProcessInstanceId())){
					dto.setApplyUserId(model.getApplyUserId());
				}
			}
		}
	}
	
	
	
	
	/**
	 * 设置提交人id
	 * setSubmitUserId:
	 * @author: 欧阳利
	 * @param taskResDtos
	 * @description:
	 */
    private void setSubmitUserId(List<TaskPageResDto> taskResDtos){
    	if(BooleanUtils.isEmpty(taskResDtos)){
			return;
		}
    	
    	List<String> processInstanceIds = new ArrayList<String>();
		for(TaskPageResDto dto : taskResDtos){
			processInstanceIds.add(dto.getProcessInstanceId());
		}
		
    }
}

