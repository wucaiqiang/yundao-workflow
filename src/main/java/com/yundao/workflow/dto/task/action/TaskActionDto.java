

package com.yundao.workflow.dto.task.action;

import java.util.List;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午6:47:54 
 * @author   欧阳利
 * @version   
 */
public class TaskActionDto {
	
	/**
	 * 任务定义key
	 */
    private String taskDefinitionKey;
    
    /**
     * 任务操作值
     */
    private List<Integer> actionValues;

	public String getTaskDefinitionKey() {
	
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
	
		this.taskDefinitionKey = taskDefinitionKey;
	}

	public List<Integer> getActionValues() {
	
		return actionValues;
	}

	public void setActionValues(List<Integer> actionValues) {
	
		this.actionValues = actionValues;
	}


    
    
}

