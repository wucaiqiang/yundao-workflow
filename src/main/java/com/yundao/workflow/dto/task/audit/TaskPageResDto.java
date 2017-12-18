package com.yundao.workflow.dto.task.audit;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 客户池分页数据
 *
 * @author jan
 * @create 2017-08-14 AM8:56
 **/
public class TaskPageResDto {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;
    
    @ApiModelProperty(value = "任务定义key")
    private String taskDefinitionKey;

    @ApiModelProperty(value = "任务创建时间")
    private Date createTime;

    @ApiModelProperty(value = "流程启动申请人")
    private Long applyUserId;
    
    @ApiModelProperty(value = "流程启动时间")
    private Date applyTime;
    
    @ApiModelProperty(value = "提交人名称")
    private Long submitUserId;
    
    @ApiModelProperty(value = "业务id")
    private String businesskey;
    
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;
    
    @ApiModelProperty(value = "流程定义id")
    private String processDefinitionId;  
    
    @ApiModelProperty(value = "执行实例id")
    private String executionId;
    
    @ApiModelProperty(value = "父任务id")
    private String parentTaskId;
    
    @ApiModelProperty(value = "应许操作值")
    private List<ActionDto> actionDtos;




	public Long getApplyUserId() {
	
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
	
		this.applyUserId = applyUserId;
	}

	public void setSubmitUserId(Long submitUserId) {
	
		this.submitUserId = submitUserId;
	}

	public Date getApplyTime() {
	
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
	
		this.applyTime = applyTime;
	}

	public String getExecutionId() {
	
		return executionId;
	}

	public void setExecutionId(String executionId) {
	
		this.executionId = executionId;
	}

	public String getTaskDefinitionKey() {
	
		return taskDefinitionKey;
	}

	public void setTaskDefinitionKey(String taskDefinitionKey) {
	
		this.taskDefinitionKey = taskDefinitionKey;
	}



	public List<ActionDto> getActionDtos() {
	
		return actionDtos;
	}

	public void setActionDtos(List<ActionDto> actionDtos) {
	
		this.actionDtos = actionDtos;
	}

	public String getTaskId() {
	
		return taskId;
	}

	public void setTaskId(String taskId) {
	
		this.taskId = taskId;
	}

	public String getTaskName() {
	
		return taskName;
	}

	public void setTaskName(String taskName) {
	
		this.taskName = taskName;
	}

	public Date getCreateTime() {
	
		return createTime;
	}

	public void setCreateTime(Date createTime) {
	
		this.createTime = createTime;
	}

	public String getBusinesskey() {
	
		return businesskey;
	}

	public void setBusinesskey(String businesskey) {
	
		this.businesskey = businesskey;
	}

	public String getProcessInstanceId() {
	
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
	
		this.processInstanceId = processInstanceId;
	}



	public String getProcessDefinitionId() {
	
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
	
		this.processDefinitionId = processDefinitionId;
	}

	public String getParentTaskId() {
	
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
	
		this.parentTaskId = parentTaskId;
	}

	public Long getSubmitUserId() {
	
		return submitUserId;
	}

    
}
