
package com.yundao.workflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.history.NativeHistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.service.AbstractService;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.constant.Constant;
import com.yundao.workflow.dto.ProcessInstanceDTO;
import com.yundao.workflow.dto.TaskCompleteReqDTO;
import com.yundao.workflow.dto.TaskCompleteResDTO;
import com.yundao.workflow.dto.TaskPageReqDTO;
import com.yundao.workflow.dto.TaskResDTO;
import com.yundao.workflow.dto.varinst.ApplyUserVariableDto;
import com.yundao.workflow.enums.TaskStatusEnum;
import com.yundao.workflow.enums.WorkflowActionEnum;
import com.yundao.workflow.mapper.HistoryCateoryMapper;
import com.yundao.workflow.mapper.ProcessInstanceMapper;
import com.yundao.workflow.mapper.VariableMapper;
import com.yundao.workflow.service.CustomHistoryService;
import com.yundao.workflow.service.CustomHistoryTaskService;
import com.yundao.workflow.service.CustomIdentityService;
import com.yundao.workflow.service.CustomTaskService;
import com.yundao.workflow.util.ConverterUtils;

/**
 * Function: Reason: Date: 2017年7月19日 下午3:06:15
 * 
 * @author wucq
 * @version
 */
@Service
public class CustomTaskServiceImpl extends AbstractService implements CustomTaskService {
	private Log logger = LogFactory.getLog(CustomTaskServiceImpl.class);
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private CustomIdentityService customIdentityService;
	@Autowired
	private CustomHistoryTaskService customHistoryTaskService;
	@Autowired
	@Qualifier("processEngineConfiguration")
	private SpringProcessEngineConfiguration springProcessEngineConfiguration;
	@Autowired
	private CustomHistoryService customHistoryService;

	@Autowired
	private ProcessInstanceMapper processInstanceMapper;

	@Autowired
	VariableMapper variableMapper;
	@Autowired
	private ManagementService managementService;
	@Autowired
	private HistoryCateoryMapper historyCateoryMapper;

	/**
	 * 获取任务的业务key
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public String getBusinessKey(String taskId) throws Exception {
		Long tenantId = this.getHeaderTenantId();
		return processInstanceMapper.getBusinessKey(taskId, tenantId);
	}



	@Override
	public Result<List<TaskResDTO>> selectTaskByProcessInstanceId(String processInstanceId) throws Exception {
		List<TaskResDTO> result = new ArrayList<>();
		TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstanceId)
				.orderByTaskCreateTime();
		List<Task> tasks = taskQuery.desc().list();
		if (tasks != null && !tasks.isEmpty()) {
			this.converTask(tasks, result);
		}
		return Result.newSuccessResult(result);
	}

	@Override
	public Result<TaskResDTO> selectTaskByTaskId(String taskId) throws Exception {

		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		HistoricTaskInstance task = historicTaskInstanceQuery.taskId(taskId).singleResult();

		TaskResDTO dto = new TaskResDTO();
		if (task != null) {
			BeanUtils.copyProperties(task, dto);
		}
		List<Comment> comments = taskService.getTaskComments(taskId);
		if (comments != null && !comments.isEmpty()) {
			Comment comment = comments.get(0);
			dto.setComment(comment.getFullMessage());
			dto.setActionUserId(comment.getUserId());
		}

		HistoricVariableInstanceQuery variableQuery = historyService.createHistoricVariableInstanceQuery();
		HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		HistoricVariableInstance variableInstance = variableQuery.executionId(dto.getExecutionId())
				.variableName(Constant.ACTION_VALUE).singleResult();
		if (variableInstance != null) {
			dto.setActionValue((Integer) variableInstance.getValue());
		}
		HistoricProcessInstance processInstance = processInstanceQuery.processInstanceId(dto.getProcessInstanceId())
				.singleResult();
		if (processInstance != null) {
			dto.setBusinessKey(processInstance.getBusinessKey());
		}

		return Result.newSuccessResult(dto);
	}

	@Override
	public Result<List<TaskResDTO>> selectTasksByResourceName(String resourceName, String taskStatus) throws Exception {
		List<TaskResDTO> dtoList = new ArrayList<>();

		Set<String> groupNames = customIdentityService.selectGroupName();

		if (groupNames == null || groupNames.isEmpty()) {
			throw new BaseException(CodeConstant.CODE_1100005);
		}
		// ProcessDefinitionDTO processDefinitionDTO =
		// customRepositoryService.selectByResourceName(resourceName)
		// .getResult();

		if (StringUtils.isNotBlank(taskStatus) && TaskStatusEnum.TODO.getValue().equals(taskStatus)) {
			List<Task> task = new ArrayList<>();
			// 查询我的待办
			TaskQuery taskQuery = taskService.createTaskQuery();

			taskQuery.processDefinitionKey(resourceName);

			for (Iterator<String> iter = groupNames.iterator(); iter.hasNext();) {
				List<Task> queryTask = taskQuery.taskCandidateOrAssigned(iter.next()).list();
				if (queryTask != null && !queryTask.isEmpty()) {
					task.addAll(queryTask);
				}
			}
			if (task != null && !task.isEmpty()) {
				this.converTask(task, dtoList);
			}

		} else {

			List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();

			HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
			if (TaskStatusEnum.DONE.getValue().equals(taskStatus)) {
				// 查询我的已办
				historicTaskInstanceQuery.taskCompletedBefore(new Date());
			}
			historicTaskInstanceQuery.processDefinitionKey(resourceName);

			for (Iterator<String> iter = groupNames.iterator(); iter.hasNext();) {
				List<HistoricTaskInstance> queryTask = historicTaskInstanceQuery.taskAssignee(iter.next()).list();
				if (queryTask != null && !queryTask.isEmpty()) {
					historicTaskInstances.addAll(queryTask);
				}
			}
			if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
				this.converTask(historicTaskInstances, dtoList);
			}
		}
		if (dtoList != null && !dtoList.isEmpty()) {

			HistoricVariableInstanceQuery variableQuery = historyService.createHistoricVariableInstanceQuery();
			HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
			for (TaskResDTO dto : dtoList) {

				List<Comment> comments = taskService.getTaskComments(dto.getId());
				if (comments != null && !comments.isEmpty()) {
					dto.setComment(comments.get(0).getFullMessage());
					dto.setActionUserId(comments.get(0).getUserId());
				}

				HistoricVariableInstance variableInstance = variableQuery.executionId(dto.getExecutionId())
						.variableName(Constant.ACTION_VALUE).singleResult();
				if (variableInstance != null) {
					dto.setActionValue((Integer) variableInstance.getValue());
				}
				HistoricProcessInstance processInstance = processInstanceQuery
						.processInstanceId(dto.getProcessInstanceId()).singleResult();
				if (processInstance != null) {
					dto.setBusinessKey(processInstance.getBusinessKey());
				}
			}
		}

		return Result.newSuccessResult(dtoList);

	}

	@Override
	public Result<List<TaskResDTO>> selectSingleTasks(String businessKey, String taskStatus) throws Exception {
		List<TaskResDTO> allList = new ArrayList<>();
		if (StringUtils.isBlank(businessKey)) {
			throw new BaseException(CodeConstant.CODE_1100010);
		}
		if (TaskStatusEnum.TODO.getValue().equals(taskStatus)) {
			List<Task> task = new ArrayList<>();
			// 查询我的待办
			TaskQuery taskQuery = taskService.createTaskQuery();
			List<TaskResDTO> dtoList = new ArrayList<>();

			taskQuery.processInstanceBusinessKey(businessKey);

			List<Task> queryTask = taskQuery.taskCandidateOrAssigned(this.getHeaderUser().getUserId().toString())
					.list();
			if (queryTask != null) {
				task.addAll(queryTask);
			}
			if (task != null && !task.isEmpty()) {
				this.converTask(task, dtoList);
				allList.addAll(dtoList);
			}
		}
		return Result.newSuccessResult(allList);

	}

	@Override
	public Result<Map<String, List<TaskResDTO>>> selectTasks(String businessKeys, String taskStatus) throws Exception {
		Map<String, List<TaskResDTO>> retMap = new HashMap<>();
		if (StringUtils.isBlank(businessKeys)) {
			throw new BaseException(CodeConstant.CODE_1100010);
		}

		if (StringUtils.isNotBlank(taskStatus) && TaskStatusEnum.TODO.getValue().equals(taskStatus)) {
			retMap = selectTasks(businessKeys);
		} else {
			retMap = customHistoryTaskService.selectHistoryTasks(businessKeys);
		}

		return Result.newSuccessResult(retMap);

	}

	@Override
	public Result<List<TaskResDTO>> selectTasksByTaskStatus(String taskStatus) throws Exception {
		List<TaskResDTO> taskResDTOs = null;

		if (StringUtils.isNotBlank(taskStatus) && TaskStatusEnum.TODO.getValue().equals(taskStatus)) {
			taskResDTOs = selectTaskList(null);
		} else {
			taskResDTOs = customHistoryTaskService.selectHistoryTaskListByUserId(String.valueOf(this.getHeaderUserId()),
					null);
		}

		return Result.newSuccessResult(taskResDTOs);

	}

	@Override
	public Result<List<TaskResDTO>> selectDoneTaskAll(String orderBy) throws Exception {
		List<TaskResDTO> taskResDTOs = customHistoryTaskService
				.selectHistoryTaskListByUserId(String.valueOf(this.getHeaderUserId()), orderBy);
		return Result.newSuccessResult(taskResDTOs);
	}

	@Override
	public Result<PaginationSupport<TaskResDTO>> selectDoneTaskAll(TaskPageReqDTO reqDTO) throws Exception {
		List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
		
		List<HistoricTaskInstance> queryTask=this.nativeSelectDoneTasks(reqDTO);
		
		if (queryTask != null && !queryTask.isEmpty()) {
			historicTaskInstances.addAll(queryTask);
		}
		List<TaskResDTO> dtoList = new ArrayList<>();
		if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
			this.converTask(historicTaskInstances, dtoList);
		}
		HistoricVariableInstanceQuery variableQuery = historyService.createHistoricVariableInstanceQuery();
		HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
		Set<String> processInstanceIds =new HashSet();
		for (TaskResDTO dto : dtoList) {
			List<Comment> comments = taskService.getTaskComments(dto.getId());
			if (comments != null && !comments.isEmpty()) {
				dto.setComment(comments.get(0).getFullMessage());
				dto.setActionUserId(comments.get(0).getUserId());
			}

			HistoricVariableInstance variableInstance = variableQuery.taskId(dto.getId())
					.variableName(Constant.ACTION_VALUE).singleResult();
			if (variableInstance != null) {
				dto.setActionValue((Integer) variableInstance.getValue());
			}
			HistoricProcessInstance processInstance = processInstanceQuery.processInstanceId(dto.getProcessInstanceId())
					.singleResult();
			if (processInstance != null) {
				dto.setBusinessKey(processInstance.getBusinessKey());
			}
			dto.setApplyTime(processInstance.getStartTime());
			processInstanceIds.add(processInstance.getId());
		}
		if(processInstanceIds !=null && !processInstanceIds.isEmpty()){
			
			List<ApplyUserVariableDto> list = variableMapper.getApplyUserVariableDto(new ArrayList<>(processInstanceIds));
			for(TaskResDTO dto : dtoList){
				for(ApplyUserVariableDto model : list){
					if(dto.getProcessInstanceId().equals(model.getProcessInstanceId())){
						dto.setApplyUserId(model.getApplyUserId());
					}
				}
			}
		}
		
		int count=nativeSelectDoneTasksCount(reqDTO);
		PaginationSupport<TaskResDTO> pager=reqDTO.getPaginationSupport();
		pager.setDatas(dtoList);
		pager.setTotalCount(count);
		
		return Result.newSuccessResult(pager);

	}
    private List<HistoricTaskInstance> nativeSelectDoneTasks(TaskPageReqDTO reqDTO)throws BaseException{
    	NativeHistoricTaskInstanceQuery nativeHistoricTaskInstanceQuery = historyService
				.createNativeHistoricTaskInstanceQuery();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from " + managementService.getTableName(HistoricTaskInstance.class) + " H");
		sql.append(" where H.END_TIME_ is not null and H.ASSIGNEE_ =#{userId} and H.TENANT_ID_ =#{tenantId}");
		if (StringUtils.isNotBlank(reqDTO.getFiltNodeCategorys())) {
			sql.append(" and IFNULL(H.CATEGORY_ ,'') NOT IN (");
			String[] categorys = reqDTO.getFiltNodeCategorys().split(",");
			for (int i = 0; i < categorys.length; i++) {
				if(i>0){
					sql.append(",");
				}
				sql.append("'").append(categorys[i]).append("'");
			}
			sql.append(" )");

		}
		sql.append(" order by ");
		if (StringUtils.isNotBlank(reqDTO.getOrderColumn())) {
			sql.append(" H."+reqDTO.getOrderColumn()+" ").append(reqDTO.getSort());
		} else {
			sql.append(" H.START_TIME_ desc");
		}

		List<HistoricTaskInstance> queryTask = nativeHistoricTaskInstanceQuery.sql(sql.toString())
				.parameter("userId", this.getHeaderUserId()).parameter("tenantId", this.getHeaderTenantId()).listPage(reqDTO.getOffset(), reqDTO.getPageSize());
		return queryTask;
		
    }
    private int nativeSelectDoneTasksCount(TaskPageReqDTO reqDTO)throws BaseException{
    	NativeHistoricTaskInstanceQuery nativeHistoricTaskInstanceQuery = historyService
				.createNativeHistoricTaskInstanceQuery();
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from " + managementService.getTableName(HistoricTaskInstance.class) + " H");
		sql.append(" where H.END_TIME_ is not null and H.ASSIGNEE_ =#{userId}");
		if (StringUtils.isNotBlank(reqDTO.getFiltNodeCategorys())) {
			sql.append(" and IFNULL(H.CATEGORY_ ,'') NOT IN (");
			String[] categorys = reqDTO.getFiltNodeCategorys().split(",");
			for (int i = 0; i < categorys.length; i++) {
				if(i>0){
					sql.append(",");
				}
				sql.append("'").append(categorys[i]).append("'");
			}
			sql.append(" )");

		}
		Long count = nativeHistoricTaskInstanceQuery.sql(sql.toString())
				.parameter("userId", this.getHeaderUserId()).count();
		return count.intValue();
    }
	/**
	 * 查询单个业务的任务列表(待办任务，历史任务)
	 * 
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	public Result<List<TaskResDTO>> selectBusinessKeyTaskList(String businessKey) throws Exception {
		if (StringUtils.isBlank(businessKey)) {
			throw new BaseException(CodeConstant.CODE_1100010);
		}

		// 查询待办任务
		// List<TaskResDTO> tasklist = selectTaskAuditList(businessKey);
		// 查询历史任务
		List<TaskResDTO> historyTasklist = customHistoryTaskService.selectHistoryTaskList(businessKey);

		// List<TaskResDTO> allTasks = new ArrayList<TaskResDTO>();
		// if(!BooleanUtils.isEmpty(tasklist)){
		// allTasks.addAll(tasklist);
		// }
		// if (!BooleanUtils.isEmpty(historyTasklist)) {
		// allTasks.addAll(historyTasklist);
		// }
		return Result.newSuccessResult(historyTasklist);

	}

	private List<TaskResDTO> selectTaskAuditList(String businessKey) throws Exception {
		List<Task> task = new ArrayList<>();
		// 查询我的待办
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<TaskResDTO> dtoList = new ArrayList<>();

		taskQuery.processInstanceBusinessKey(businessKey);

		List<Task> queryTask = taskQuery.list();
		if (queryTask != null && !queryTask.isEmpty()) {
			task.addAll(queryTask);
		}
		if (task != null && !task.isEmpty()) {
			this.converTask(task, dtoList);
		}
		for (TaskResDTO dto : dtoList) {
			dto.setHistory(false);
			dto.setType("userTask");
		}
		return dtoList;
	}

	private List<TaskResDTO> selectTaskList(String businessKey) throws Exception {
		String userId = this.getHeaderUser().getUserId().toString();
		List<Task> task = new ArrayList<>();
		// 查询我的待办
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<TaskResDTO> dtoList = new ArrayList<>();

		if (StringUtils.isNotBlank(businessKey)) {
			taskQuery.processInstanceBusinessKey(businessKey);
		}

		List<Task> queryTask = taskQuery.taskCandidateOrAssigned(userId).list();
		if (queryTask != null && !queryTask.isEmpty()) {
			task.addAll(queryTask);
		}
		if (task != null && !task.isEmpty()) {
			this.converTask(task, dtoList);
		}
		for (TaskResDTO dto : dtoList) {
			dto.setHistory(false);
			dto.setType("userTask");
		}
		return dtoList;
	}

	/**
	 * 查询多个有多个业务的待办
	 * 
	 * @param businessKeys
	 * @return
	 * @throws Exception
	 */
	private Map<String, List<TaskResDTO>> selectTasks(String businessKeys) throws Exception {
		Map<String, List<TaskResDTO>> retMap = new HashMap<>();
		String[] keys = businessKeys.split(",");
		for (String businessKey : keys) {
			retMap.put(businessKey, selectTaskList(businessKey));
		}
		return retMap;
	}

	@Override
	public Result<Boolean> doClaim(String taskId) throws Exception {
		try {
			Task task = this.selectTaskById(taskId, this.getHeaderUser().getTenantId().toString()).getResult();
			taskService.claim(taskId, task.getAssignee());
		} catch (ActivitiObjectNotFoundException e) {
			logger.error("任务id不存在taskId=" + taskId, e);
			throw new BaseException(CodeConstant.CODE_1100006);
		} catch (ActivitiTaskAlreadyClaimedException e) {
			logger.error("任务已被他人领取taskId=" + taskId, e);
			throw new BaseException(CodeConstant.CODE_1100007);
		}
		return Result.newSuccessResult(true);
	}

	@Override
	public Result<TaskCompleteResDTO> doComplete(TaskCompleteReqDTO completeReqDTO) throws Exception {
		String taskId = completeReqDTO.getTaskId();

		Result<Task> taskResult = this.selectTaskById(taskId, this.getHeaderUser().getTenantId().toString());

		// 发起人可以取消任务
		if (completeReqDTO.getActionValue() == WorkflowActionEnum.CANCEL.getValue()) {
			Long applyUserId = variableMapper.getInstanceApplyUserId(taskResult.getResult().getProcessInstanceId(),
					this.getHeaderUser().getTenantId());
			if (applyUserId == null || !applyUserId.equals(this.getHeaderUser().getUserId())) {
				return Result.newFailureResult(CodeConstant.CODE_1100022);
			}
		}

		// 转换完成时的变量
		Map<String, Object> variables = ConverterUtils.getConvertMap(completeReqDTO.getPairList());
		if (completeReqDTO.getActionValue() == null) {
			throw new BaseException(CodeConstant.CODE_1100019);
		}
		if (StringUtils.isNotBlank(completeReqDTO.getComment())) {
			try {
				Authentication.setAuthenticatedUserId(String.valueOf(this.getHeaderUser().getUserId()));
				taskService.addComment(taskId, taskResult.getResult().getProcessInstanceId(),
						completeReqDTO.getComment());
			} finally {
				Authentication.setAuthenticatedUserId(null);
			}
		}

		variables.put(Constant.ACTION_VALUE, completeReqDTO.getActionValue());
		variables.put(Constant.ACTION_USER_ID, this.getHeaderUser().getUserId());

		taskService.setVariableLocal(taskId, Constant.ACTION_VALUE, completeReqDTO.getActionValue());
		taskService.setVariableLocal(taskId, Constant.ACTION_USER_ID, this.getHeaderUser().getUserId());
		if (!(taskResult.getResult().getAssignee() != null
				&& taskResult.getResult().getAssignee().equals(this.getHeaderUser().getUserId().toString()))) {
			taskService.claim(taskId, this.getHeaderUser().getUserId().toString());
		}
		taskService.complete(taskId, variables);

		// 获取下个待办任务
		TaskCompleteResDTO resDTO = new TaskCompleteResDTO();
		Result<List<TaskResDTO>> nextTaskResult = this
				.selectTaskByProcessInstanceId(taskResult.getResult().getProcessInstanceId());
		if (nextTaskResult != null && nextTaskResult.getResult() != null && !nextTaskResult.getResult().isEmpty()) {
			TaskResDTO taskDto = nextTaskResult.getResult().get(0);
			resDTO.setTaskId(taskDto.getId());
		}
		Result<Boolean> isEndResult = customHistoryService
				.processIsEndByProcessInstanceId(taskResult.getResult().getProcessInstanceId());
		if (isEndResult != null && isEndResult.getResult() != null) {
			resDTO.setProcessIsEnd(isEndResult.getResult());
		}
		Result<ProcessInstanceDTO> processInstanceDtoResult = customHistoryService
				.selectHistoryProcessInstanceByProcessInstanceId(taskResult.getResult().getProcessInstanceId());
		if (processInstanceDtoResult != null && processInstanceDtoResult.getResult() != null) {
			ProcessInstanceDTO instanceDTO = processInstanceDtoResult.getResult();
			resDTO.setBusinessKey(instanceDTO.getBusinessKey());
		}
		return Result.newSuccessResult(resDTO);
	}

	public Result<Task> selectTaskById(String taskId, String tenantId) throws Exception {
		TaskQuery taskQuery = taskService.createTaskQuery().taskId(taskId).taskTenantId(tenantId);
		Task task = taskQuery.singleResult();
		if (task == null) {
			throw new BaseException(CodeConstant.CODE_1100004);
		}
		return Result.newSuccessResult(task);
	}

	/**
	 * 把来源任务转换后添加到目的任务
	 *
	 * @param fromTasks
	 *            来源任务
	 * @param toTasks
	 *            目的任务
	 */
	private <T> void converTask(List<T> fromTasks, List<TaskResDTO> toTasks) {
		if (fromTasks == null || fromTasks.isEmpty()) {
			return;
		}
		for (T each : fromTasks) {
			TaskResDTO dto = new TaskResDTO();
			BeanUtils.copyProperties(each, dto);
			toTasks.add(dto);
		}
	}
	
	
	
    /**
     * 设置任务分类(处理历史数据)
     * doSetCategory:
     * @author: 欧阳利
     * @return
     * @throws Exception
     * @description:
     */
	public Result<Integer> doSetCategory()throws Exception{
		Long tenantId = this.getHeaderTenantId();
		if(tenantId >= 5){
			return Result.newSuccessResult(0);
		}
		int count = 0;
		count = count + historyCateoryMapper.updateRunTask("audit", tenantId.toString());
		count = count + historyCateoryMapper.updateRunTask("reject_edit", tenantId.toString());
		
		count = count + historyCateoryMapper.updateHistoryTask("audit", tenantId.toString());
		count = count + historyCateoryMapper.updateHistoryTask("reject_edit", tenantId.toString());
		logger.info("修改类别历史数据个数="+count);
		return Result.newSuccessResult(count);
	}

}
