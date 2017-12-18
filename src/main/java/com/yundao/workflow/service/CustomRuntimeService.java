

package com.yundao.workflow.service;

import java.util.Map;

import org.activiti.engine.impl.persistence.entity.VariableInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 下午2:51:17 
 * @author   wucq
 * @version   
 */
public interface CustomRuntimeService {
	public String startProcessInstanceById(String processDefinitionKey,String businessKey,Map<String,Object> variables,Long tenantId)throws Exception;
	public Map<String, VariableInstance> selectVariables(String executionId,String variableName)throws Exception;
	public ProcessInstance selectProcessInstanceByTaskId(String taskId)throws Exception;
	public Execution selectExecutionByBusinessKey(String businessKey)throws Exception;
}

