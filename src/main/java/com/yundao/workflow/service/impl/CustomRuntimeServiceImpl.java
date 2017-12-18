

package com.yundao.workflow.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.core.service.AbstractService;
import com.yundao.workflow.service.CustomRuntimeService;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 下午2:51:52 
 * @author   wucq
 * @version   
 */
@Service
public class CustomRuntimeServiceImpl extends AbstractService implements CustomRuntimeService {
	private Log logger=LogFactory.getLog(CustomRuntimeServiceImpl.class);
	@Autowired
    private RuntimeService runtimeService;
	@Autowired
    private TaskService taskService;
	
	@Override
	public String startProcessInstanceById(String processDefinitionKey,String businessKey,Map<String,Object> variables,Long tenantId)throws Exception{
		logger.info("processDefinitionKey="+processDefinitionKey+",businessKey="+businessKey+"tenantId="+tenantId+"流程变量="+variables);
		ProcessInstance processInstance=runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, variables, tenantId.toString());
		return processInstance.getId();
	}
	
	@Override
	public Map<String, VariableInstance> selectVariables(String executionId, String variableName) throws Exception {
		Map<String, VariableInstance> map=new HashMap<>();
		
		if(StringUtils.isBlank(executionId)){
			throw new BaseException(CodeConstant.CODE_1100011);
		}
		if(StringUtils.isBlank(variableName)){
			map=runtimeService.getVariableInstances(executionId);
		}else{
			VariableInstance variableInstance=runtimeService.getVariableInstance(executionId, variableName);
			map.put(variableName, variableInstance);
		}
		return map;
		
	}

	@Override
	public Execution selectExecutionByBusinessKey(String businessKey) throws Exception {
		if(StringUtils.isBlank(businessKey)){
			throw new BaseException(CodeConstant.CODE_1100010);
		}
		Execution execution=runtimeService.createExecutionQuery().processInstanceBusinessKey(businessKey).singleResult();
		return execution;
	}

	@Override
	public ProcessInstance selectProcessInstanceByTaskId(String taskId)throws Exception{
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
        	logger.info("任务id不存在taskId=" + taskId);
            throw new BaseException(CodeConstant.CODE_1100004);
        }
        String processInstanceId = task.getProcessInstanceId();
        
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        
		return processInstance;
	}
}

