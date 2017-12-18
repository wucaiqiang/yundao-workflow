

package com.yundao.workflow.service;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.workflow.dto.ProcessInstanceDTO;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 上午10:41:12 
 * @author   wucq
 * @version   
 */
public interface CustomHistoryService {
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByBusinessKey(String businessKey) throws Exception;
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByTaskId(String taskId) throws BaseException;
	public Result<ProcessInstanceDTO> selectHistoryProcessInstanceByProcessInstanceId(String processInstanceId) throws Exception;
	public Result<Boolean> processIsEnd(String businessKey) throws Exception;
	public Result<Boolean> processIsEndByProcessInstanceId(String processInstanceId) throws Exception;
}

