package com.yundao.workflow.dto.task.audit;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class TaskAuditApplyDto {
    @ApiModelProperty(value = "任务id")
    private String taskId;  
	
    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;  
    
    @ApiModelProperty(value = "流程启动申请人")
    private Long applyUserId;
    
    @ApiModelProperty(value = "流程启动时间")
    private Date applyTime;
    
    
    @ApiModelProperty(value = "任务定义key")
    private String taskDefinitionKey;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}


	public String getTaskDefinitionKey() {
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
		this.taskDefinitionKey = taskDefinitionKey;
	}
    
}
