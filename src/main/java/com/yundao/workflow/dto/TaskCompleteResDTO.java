package com.yundao.workflow.dto;
import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yundao.workflow.constant.CodeConstant;

import io.swagger.annotations.ApiModelProperty;

/**
 * 任务完成请求传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class TaskCompleteResDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务ID")
    private String taskId;
    /**
     * 流程是否已经结束 
     */
    private Boolean processIsEnd;
    /**
     * 业务流水号
     */
    private String businessKey;
	public String getTaskId() {
	
		return taskId;
	}
	public void setTaskId(String taskId) {
	
		this.taskId = taskId;
	}
	public Boolean getProcessIsEnd() {
	
		return processIsEnd;
	}
	public void setProcessIsEnd(Boolean processIsEnd) {
	
		this.processIsEnd = processIsEnd;
	}
	public String getBusinessKey() {
	
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
	
		this.businessKey = businessKey;
	}
    

}
