package com.yundao.workflow.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.history.HistoricVariableInstanceQuery;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.utils.BooleanUtils;
import com.yundao.workflow.constant.Constant;
import com.yundao.workflow.dto.TaskResDTO;
import com.yundao.workflow.service.CustomHistoryTaskService;

@Service
public class CustomHistoryTaskServiceImpl implements CustomHistoryTaskService {

	@Autowired
	private HistoryService historyService;

	@Autowired
	private TaskService taskService;

	@Override
	public Map<String, List<TaskResDTO>> selectHistoryTasks(String businessKeys) throws Exception {
		Map<String, List<TaskResDTO>> retMap = new HashMap<>();
		String[] keys = businessKeys.split(",");
		for (String businessKey : keys) {
			retMap.put(businessKey, selectHistoryTasksByBusinessKey(businessKey));
		}

		if (retMap != null && !retMap.isEmpty()) {
			HistoricVariableInstanceQuery variableQuery = historyService.createHistoricVariableInstanceQuery();

			Set<Entry<String, List<TaskResDTO>>> taskSet = retMap.entrySet();
			Iterator<Entry<String, List<TaskResDTO>>> iterator = taskSet.iterator();
			while (iterator.hasNext()) {
				Entry<String, List<TaskResDTO>> entry = iterator.next();
				List<TaskResDTO> dtoList = entry.getValue();

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
					dto.setBusinessKey(entry.getKey());
				}
			}
		}

		return retMap;
	}

	/**
	 * 查询单个业务的所有所有历史任务
	 * 
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	private List<TaskResDTO> selectHistoryTasksByBusinessKey(String businessKey) throws Exception {
		List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
				.orderByTaskCreateTime().desc();

		historicTaskInstanceQuery.taskCompletedBefore(new Date());

		List<TaskResDTO> dtoList = new ArrayList<>();
		historicTaskInstanceQuery.processInstanceBusinessKey(businessKey);

		List<HistoricTaskInstance> queryTask = historicTaskInstanceQuery.list();
		if (queryTask != null && !queryTask.isEmpty()) {
			historicTaskInstances.addAll(queryTask);
		}

		if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
			this.converTask(historicTaskInstances, dtoList);
		}
		return dtoList;
	}

	/**
	 * 查询单个历史任务
	 * 
	 * @param businessKeys
	 * @return
	 * @throws Exception
	 */
	public List<TaskResDTO> selectHistoryTaskList(String businessKey) throws Exception {
		List<TaskResDTO> list = selectHistoryTasksByBusinessKey(businessKey);
		List<TaskResDTO> startList = getStartTasks(businessKey);
		list.addAll(startList);

		if (!BooleanUtils.isEmpty(list)) {
			for (TaskResDTO dto : list) {
				List<Comment> comments = taskService.getTaskComments(dto.getId());
				if (comments != null && !comments.isEmpty()) {
					dto.setComment(comments.get(0).getFullMessage());
					dto.setActionUserId(comments.get(0).getUserId());
				}

				dto.setBusinessKey(businessKey);
				dto.setHistory(true);
				if (dto.getId() != null) {
					dto.setType("userTask");
				}
			}

			List<HistoricVariableInstance> allVariables = getHistoryHistoricVariables(businessKey);
			if (!BooleanUtils.isEmpty(allVariables)) {
				for (HistoricVariableInstance variable : allVariables) {
					for (TaskResDTO task : list) {
						if (task.getId() == null) {
							if (variable.getVariableName().equals(Constant.APPLY_USER_ID)) {
								task.setActionUserId(variable.getValue().toString());
							}
						} else if (task.getId().equals(variable.getTaskId())) {
							if (variable.getVariableName().equals(Constant.ACTION_VALUE)) {
								task.setActionValue((Integer) variable.getValue());
							}
							if (variable.getVariableName().equals(Constant.ACTION_USER_ID)) {
								task.setActionUserId(variable.getValue().toString());
							}
						}
					}
				}
			}

		}
		return list;
	}

	@Override
	public List<TaskResDTO> selectHistoryTaskListByUserId(String userId,String orderBy) throws Exception {
		List<HistoricTaskInstance> historicTaskInstances = new ArrayList<>();
		HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
		if(StringUtils.isNotBlank(orderBy) && "endTime".equals(orderBy)){
			historicTaskInstanceQuery.orderByHistoricTaskInstanceEndTime().desc();
		}else{
			historicTaskInstanceQuery.orderByTaskCreateTime().desc();
		}

		historicTaskInstanceQuery.taskCompletedBefore(new Date());

		List<TaskResDTO> dtoList = new ArrayList<>();

		historicTaskInstanceQuery.taskAssignee(userId);

		List<HistoricTaskInstance> queryTask = historicTaskInstanceQuery.list();
		if (queryTask != null && !queryTask.isEmpty()) {
			historicTaskInstances.addAll(queryTask);
		}

		if (historicTaskInstances != null && !historicTaskInstances.isEmpty()) {
			this.converTask(historicTaskInstances, dtoList);
		}
		HistoricVariableInstanceQuery variableQuery = historyService.createHistoricVariableInstanceQuery();
		HistoricProcessInstanceQuery processInstanceQuery = historyService.createHistoricProcessInstanceQuery();
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
		}

		return dtoList;

	}

	public List<TaskResDTO> getStartTasks(String businessKey) {
		List<TaskResDTO> startTasks = new ArrayList<TaskResDTO>();
		List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).list();
		if (BooleanUtils.isEmpty(instances)) {
			return startTasks;
		}

		for (HistoricProcessInstance instance : instances) {
			HistoricActivityInstance historicActivity = historyService.createHistoricActivityInstanceQuery()// 创建历史活动实例的查询
					.processInstanceId(instance.getId())//
					.activityType("startEvent").orderByHistoricActivityInstanceStartTime().asc().singleResult();
			if (historicActivity != null) {
				TaskResDTO dto = new TaskResDTO();
				dto.setId(null);
				dto.setName(historicActivity.getActivityName());
				dto.setProcessInstanceId(instance.getId());
				dto.setEndTime(historicActivity.getEndTime());
				dto.setStartTime(historicActivity.getStartTime());
				dto.setType("startEvent");
				startTasks.add(dto);
			}
		}
		return startTasks;
	}

	/**
	 * 查询一个业务的所有变量
	 * 
	 * @param businessKey
	 * @return
	 */
	private List<HistoricVariableInstance> getHistoryHistoricVariables(String businessKey) {
		List<HistoricVariableInstance> allVariables = new ArrayList<HistoricVariableInstance>();
		List<HistoricProcessInstance> instances = historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey).list();
		if (BooleanUtils.isEmpty(instances)) {
			return allVariables;
		}

		for (HistoricProcessInstance instance : instances) {
			List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
					.processInstanceId(instance.getId()).list();
			if (!BooleanUtils.isEmpty(variableInstances)) {
				allVariables.addAll(variableInstances);
			}
		}
		return allVariables;
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
}
