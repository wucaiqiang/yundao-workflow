package com.yundao.workflow.service;

import java.util.List;
import java.util.Map;

import com.yundao.workflow.dto.TaskResDTO;

public interface CustomHistoryTaskService {
	
	/**
	 * 查询历史任务
	 * @param businessKeys
	 * @return
	 * @throws Exception
	 */
    public  Map<String, List<TaskResDTO>> selectHistoryTasks(String businessKeys) throws Exception;
    
    
	/**
	 * 查询单个历史任务
	 * @param businessKeys
	 * @return
	 * @throws Exception
	 */
    public  List<TaskResDTO> selectHistoryTaskList(String businessKey) throws Exception;
    /**
     * 
     * selectHistoryTaskList:
     * @author: wucq
     * @return
     * @throws Exception
     * @description:
     */
    public  List<TaskResDTO> selectHistoryTaskListByUserId(String userId,String orderBy) throws Exception;
    
    /**
     * 查询开始事件
     * @param businessKey
     * @return
     */
    public List<TaskResDTO> getStartTasks(String businessKey);
}
