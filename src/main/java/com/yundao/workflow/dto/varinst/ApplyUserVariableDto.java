

package com.yundao.workflow.dto.varinst;

import io.swagger.annotations.ApiModelProperty;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午8:34:37 
 * @author   欧阳利
 * @version   
 */
public class ApplyUserVariableDto {
    @ApiModelProperty(value = "流程实例id")
    private String processInstanceId;
    
    @ApiModelProperty(value = "申请人id")
    private Long applyUserId;

	public String getProcessInstanceId() {
	
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
	
		this.processInstanceId = processInstanceId;
	}

	public Long getApplyUserId() {
	
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
	
		this.applyUserId = applyUserId;
	}


    
    
}

