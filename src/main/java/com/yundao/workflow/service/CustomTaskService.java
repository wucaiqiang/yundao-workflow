
package com.yundao.workflow.service;

import java.util.List;
import java.util.Map;

import com.yundao.core.code.Result;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.workflow.dto.TaskCompleteReqDTO;
import com.yundao.workflow.dto.TaskCompleteResDTO;
import com.yundao.workflow.dto.TaskPageReqDTO;
import com.yundao.workflow.dto.TaskResDTO;

/**
 * Function: Reason: Date: 2017年7月19日 下午3:05:13
 * 
 * @author wucq
 * @version
 */
public interface CustomTaskService {
	/**
	 * 获取任务的业务key
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public String getBusinessKey(String taskId)throws Exception;
	
	
	public Result<List<TaskResDTO>> selectTaskByProcessInstanceId(String processInstanceId) throws Exception;

	public Result<TaskResDTO> selectTaskByTaskId(String taskId) throws Exception;

	public Result<List<TaskResDTO>> selectTasksByTaskStatus(String taskStatus) throws Exception;
	public Result<List<TaskResDTO>> selectDoneTaskAll(String orderBy) throws Exception;
	public Result<PaginationSupport<TaskResDTO>> selectDoneTaskAll(TaskPageReqDTO dto) throws Exception;
	public Result<Map<String, List<TaskResDTO>>> selectTasks(String businessKeys, String taskStatus) throws Exception;
	public Result<List<TaskResDTO>> selectSingleTasks(String businessKey, String taskStatus) throws Exception;

	public Result<List<TaskResDTO>> selectTasksByResourceName(String resourceName, String taskStatus) throws Exception;

	public Result<Boolean> doClaim(String taskId) throws Exception;

	public Result<TaskCompleteResDTO> doComplete(TaskCompleteReqDTO completeReqDTO) throws Exception;

    /**
     * 设置任务分类
     * doSetCategory:
     * @author: 欧阳利
     * @return
     * @throws Exception
     * @description:
     */
	public Result<Integer> doSetCategory()throws Exception;
	
	/**
	 * 查询单个业务的任务列表(待办任务，历史任务)
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	public Result<List<TaskResDTO>> selectBusinessKeyTaskList(String businessKey) throws Exception;
}
