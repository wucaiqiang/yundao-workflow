

package com.yundao.workflow.service;

import org.activiti.bpmn.model.BpmnModel;

import com.yundao.core.code.Result;
import com.yundao.workflow.dto.ProcessDefinitionDTO;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 下午2:30:21 
 * @author   wucq
 * @version   
 */
public interface CustomRepositoryService {
	public Result<ProcessDefinitionDTO> selectByResourceName(String resourceName) throws Exception;
	public Result<BpmnModel> selectBpmnModel(String processDefinitionId) throws Exception;
}

