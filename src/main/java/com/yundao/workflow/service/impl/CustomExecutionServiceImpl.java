
package com.yundao.workflow.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.yundao.core.dto.HeaderUser;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.Execution;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.CloseableUtils;
import com.yundao.core.utils.ListUtils;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.constant.Constant;
import com.yundao.workflow.dto.ProcessInstanceDTO;
import com.yundao.workflow.dto.ProcessStartReqDTO;
import com.yundao.workflow.dto.ProcessStartResDTO;
import com.yundao.workflow.dto.TaskResDTO;
import com.yundao.workflow.service.AbstractService;
import com.yundao.workflow.service.CustomExecutionService;
import com.yundao.workflow.service.CustomHistoryService;
import com.yundao.workflow.service.CustomIdentityService;
import com.yundao.workflow.service.CustomRepositoryService;
import com.yundao.workflow.service.CustomRuntimeService;
import com.yundao.workflow.service.CustomTaskService;
import com.yundao.workflow.util.BusinessKeyGenerator;
import com.yundao.workflow.util.ConverterUtils;

/**
 * Function: Reason: Date: 2017年7月19日 上午10:30:05
 *
 * @author wucq
 * @version
 */
@Service
public class CustomExecutionServiceImpl extends AbstractService implements CustomExecutionService {
	private Log logger = LogFactory.getLog(CustomExecutionServiceImpl.class);

	@Autowired
	private CustomHistoryService customHistoryService;
	@Autowired
	private CustomRepositoryService customRepositoryService;
	@Autowired
	private CustomIdentityService customIdentityService;
	@Autowired
	private CustomRuntimeService customRuntimeService;
	@Autowired
	private ProcessEngineConfiguration processEngineConfiguration;
	@Autowired
	private ProcessEngine processEngine;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private CustomTaskService customTaskService;

	@Override
	public Result<ProcessStartResDTO> doStart(ProcessStartReqDTO taskStartReqDTO) throws Exception {
		HeaderUser user = this.getHeaderUser();
		Long tenantId = this.getHeaderUser().getTenantId();
		try {
//			int index_ = taskStartReqDTO.getResourceName().indexOf(".");
//			String resourceName = taskStartReqDTO.getResourceName().substring(0, index_);

			String businessKey = BusinessKeyGenerator.generate(taskStartReqDTO.getProcessDefineKey());

			// 校验历史的业务值是否已存在
			Result<ProcessInstanceDTO> pidResult = customHistoryService
					.selectHistoryProcessInstanceByBusinessKey(businessKey);
			if (pidResult.getResult() != null) {
				logger.info("历史的业务值已存在");
				throw new BaseException(CodeConstant.CODE_1100000);
			}

			// 转换启动时的变量
			Map<String, Object> variables = ConverterUtils.getConvertMap(taskStartReqDTO.getPairList());
			variables.put(Constant.TENANT_ID, tenantId);
			variables.put(Constant.APPLY_USER_ID, this.getHeaderUser().getUserId());
			logger.info(() -> "转换后启动的变量variables=" + variables);
			// 设置启动流程的人员ID
			customIdentityService.setAuthenticatedUserId(String.valueOf(user.getUserId()));

			String result = customRuntimeService.startProcessInstanceById(taskStartReqDTO.getProcessDefineKey(), businessKey,
					variables,tenantId);
            // 初始化流程的用户组信息
			//initTenantGroups(tenantId, businessKey);
			logger.info("流程实例processInstanceId=" + result);

			Result<List<TaskResDTO>> taskResult=customTaskService.selectTaskByProcessInstanceId(result);
			if(taskResult ==null || taskResult.getResult() ==null || taskResult.getResult().isEmpty()){
				throw new BaseException(CodeConstant.CODE_1100015);
			}
			TaskResDTO task=taskResult.getResult().get(0);
			ProcessStartResDTO resDTO=new ProcessStartResDTO();
			resDTO.setBusinessKey(businessKey);
			resDTO.setTaskId(task.getId());
			return Result.newSuccessResult(resDTO);

		} finally {
			customIdentityService.setAuthenticatedUserId(null);
		}
	}


	/**
	 * 初始化流程的用户组
	 * @param tenantId
	 * @param businessKey
	 * @throws Exception
	 */
	private void initTenantGroups(Long tenantId,String businessKey)throws Exception{
		Execution execution = customRuntimeService.selectExecutionByBusinessKey(businessKey);
		if (execution == null) {
			throw new BaseException(CodeConstant.CODE_1100012);
		}
		Map<String, VariableInstance> variableMap = customRuntimeService.selectVariables(execution.getId(), null);

		List<String> groups = new ArrayList<String>();
		if (variableMap != null && !variableMap.isEmpty()) {
			variableMap.forEach((k, v) -> {
				// 流程图 data Objects对象中配置的，带有init_tenant_group开头的变量
				if (k.indexOf("init_tenant_group") != -1) {
					groups.add(tenantId.toString()+k.substring("init_tenant_group".length()));
				}
			});
		}

		if (!BooleanUtils.isEmpty(groups)) {
			customIdentityService.insertGroup(groups);
		}
	}

	@Override
	public Result<Boolean> processIsEnd(String businessKey) throws Exception {
		return customHistoryService.processIsEnd(businessKey);

	}

	@Override
	public Result<byte[]> selectProcessPicture(String businessKey) throws Exception {
		Result<ProcessInstanceDTO> processInstanceresult = customHistoryService
				.selectHistoryProcessInstanceByBusinessKey(businessKey);
		ProcessInstanceDTO processInstanceDTO = processInstanceresult.getResult();
		String processDefinitionId = processInstanceDTO.getProcessDefinitionId();
		String processInstanceId = processInstanceDTO.getId();
		InputStream pictureStream = null;
		try {
			BpmnModel bpmnModel = customRepositoryService.selectBpmnModel(processDefinitionId).getResult();
			// 获取图片流
			String imageType = "PNG";
			String activityFontName = processEngineConfiguration.getActivityFontName();
			String labelFontName = processEngineConfiguration.getLabelFontName();
			String annotationFontName = processEngineConfiguration.getAnnotationFontName();
			ClassLoader customClassLoader = processEngineConfiguration.getClassLoader();

			logger.info("显示字体activityFontName=" + activityFontName + ",labelFontName=" + labelFontName
					+ ",annotationFontName=" + annotationFontName);

			ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration()
					.getProcessDiagramGenerator();

			if (BooleanUtils.isNotBlank(processInstanceId)) {
				// 获取流程走过的线
				ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
						.getDeployedProcessDefinition(processDefinitionId);
				List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery()
						.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
				List<String> highlightedActivityId = getHighlightedActivity(processDefinitionEntity, activityInstances);

				// 获取流程走过的节点id
				List<String> activityIds = activityInstances.stream().map(HistoricActivityInstance::getActivityId)
						.collect(Collectors.toList());
				pictureStream = processDiagramGenerator.generateDiagram(bpmnModel, imageType, activityIds,
						highlightedActivityId, activityFontName, labelFontName, annotationFontName, customClassLoader,
						1.0);
			} else {
				pictureStream = processDiagramGenerator.generateDiagram(bpmnModel, imageType, activityFontName,
						labelFontName, annotationFontName, customClassLoader, 1.0);
			}
			byte[] result = IOUtils.toByteArray(pictureStream);
			return Result.newSuccessResult(result);

		} catch (Exception e) {
			logger.error("获取图片异常", e);
			return Result.newFailureResult();
		} finally {
			CloseableUtils.close(pictureStream);
		}

	}

	private List<String> getHighlightedActivity(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> result = new ArrayList<>();
		int size = ListUtils.getSize(historicActivityInstances);

		// 对历史流程节点进行遍历
		for (int i = 0; i < size - 1; i++) {

			// 用以保存后需开始时间相同的节点
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();

			// 将后面第一个节点放在时间相同节点的集合里
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			sameStartTimeNodes.add(sameActivityImpl1);

			for (int j = i + 1; j < size - 1; j++) {
				// 后续第一个节点
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);

				// 后续第二个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);

				// 如果第一个节点和第二个节点开始时间相同保存
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					// 有不相同跳出循环
					break;
				}
			}

			// 取出节点的所有出去的线
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
			for (PvmTransition pvmTransition : pvmTransitions) {

				// 如果取出的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					result.add(pvmTransition.getId());
				}
			}
		}
		return result;
	}

}
