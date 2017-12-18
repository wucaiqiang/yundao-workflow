package com.yundao.workflow.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yundao.workflow.constant.CodeConstant;

import io.swagger.annotations.ApiModelProperty;

/**
 * 任务开始时的请求传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class ProcessStartResDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务流水号
     */
    @ApiModelProperty(value = "业务流水号")
    private String businessKey;
    /**
     * 下个待办任务Id
     */
    @ApiModelProperty(value = "下个待办任务Id")
    private String taskId;
	public String getBusinessKey() {
	
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
	
		this.businessKey = businessKey;
	}
	public String getTaskId() {
	
		return taskId;
	}
	public void setTaskId(String taskId) {
	
		this.taskId = taskId;
	}
    
    
    
}
