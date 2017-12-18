
package com.yundao.workflow.service.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.ProcessInstanceDTO;
import com.yundao.core.service.AbstractService;
import com.yundao.workflow.service.CustomHistoryService;

/**
 * Function: Reason: Date: 2017年7月19日 上午10:41:56
 * 
 * @author wucq
 * @version
 */
@Service
public class CustomHistoryServiceImpl extends AbstractService implements CustomHistoryService {
	private Log logger = LogFactory.getLog(CustomHistoryServiceImpl.class);
	@Autowired
	private HistoryService historyService;

	@Override
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByBusinessKey(String businessKey) throws Exception {
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).singleResult();
		ProcessInstanceDTO result = getProcessInstanceDTO(processInstance);

		return Result.newSuccessResult(result);
	}

	@Override
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByTaskId(String taskId) throws BaseException {
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		HistoricTaskInstance task = historicTaskInstanceQuery.taskId(taskId).singleResult();
		ProcessInstanceDTO result = new ProcessInstanceDTO();
		if (task != null) {
			HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
			HistoricProcessInstance processInstance = processInstanceQuery
					.processInstanceId(task.getProcessInstanceId()).singleResult();
			result = getProcessInstanceDTO(processInstance);
		}
		return Result.newSuccessResult(result);

	}

	@Override
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByProcessInstanceId(String processInstanceId)
			throws Exception {

		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		ProcessInstanceDTO result = getProcessInstanceDTO(processInstance);

		return Result.newSuccessResult(result);

	}

	@Override
	public Result<Boolean> processIsEnd(String businessKey) throws Exception {
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).singleResult();
		if (processInstance == null) {
			throw new BaseException(CodeConstant.CODE_1100008);
		}
		if (processInstance.getEndTime() != null) {
			// 代表已经结束
			return Result.newSuccessResult(true);
		}
		// 流程未结束
		return Result.newSuccessResult(false);

	}

	@Override
	public Result<Boolean> processIsEndByProcessInstanceId(String processInstanceId) throws Exception {

		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (processInstance == null) {
			throw new BaseException(CodeConstant.CODE_1100020);
		}
		if (processInstance.getEndTime() != null) {
			// 代表已经结束
			return Result.newSuccessResult(true);
		}
		// 流程未结束
		return Result.newSuccessResult(false);

	}

	private ProcessInstanceDTO getProcessInstanceDTO(HistoricProcessInstance processInstance) {
		ProcessInstanceDTO result = null;
		if (processInstance != null) {
			result = new ProcessInstanceDTO();
			BeanUtils.copyProperties(processInstance, result);
		}
		return result;
	}
}
