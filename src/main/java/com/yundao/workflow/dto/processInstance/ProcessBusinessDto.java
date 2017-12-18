

package com.yundao.workflow.dto.processInstance;

import java.util.Date;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午5:31:26 
 * @author   欧阳利
 * @version   
 */
public class ProcessBusinessDto {
    private String  processInstanceId;
    
    private String businessKey;
    
    private Date startTime;

	public Date getStartTime() {
	
		return startTime;
	}

	public void setStartTime(Date startTime) {
	
		this.startTime = startTime;
	}

	public String getProcessInstanceId() {
	
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
	
		this.processInstanceId = processInstanceId;
	}

	public String getBusinessKey() {
	
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
	
		this.businessKey = businessKey;
	}
    
}

