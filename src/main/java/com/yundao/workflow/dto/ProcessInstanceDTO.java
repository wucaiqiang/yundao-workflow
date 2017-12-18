package com.yundao.workflow.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 流程实例传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class ProcessInstanceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程实例id
     */
    private String id;

    /**
     * 流程定义id
     */
    private String processDefinitionId;

    /**
     * 流程定义名字
     */
    private String processDefinitionName;

    /**
     * 流程定义键
     */
    private String processDefinitionKey;

    /**
     * 流程定义的版本号
     */
    private Integer processDefinitionVersion;

    /**
     * 流程部署id
     */
    private String deploymentId;

    /**
     * 业务键
     */
    private String businessKey;

    /**
     * 是否挂起
     */
    private boolean isSuspended;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;

    /**
     * 租用者id
     */
    private String tenantId;

    /**
     * 流程名
     */
    private String name;

    /**
     * 流程描述
     */
    private String description;
    /**
     * 流程发起人ID
     */
    private String startUserId;
    /**
     * 流程创建时间
     */
    private Date startTime;
    /**
     * 流程审批时间
     */
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getStartUserId() {
	
		return startUserId;
	}

	public void setStartUserId(String startUserId) {
	
		this.startUserId = startUserId;
	}

	public Date getStartTime() {
	
		return startTime;
	}

	public void setStartTime(Date startTime) {
	
		this.startTime = startTime;
	}

	public Date getEndTime() {
	
		return endTime;
	}

	public void setEndTime(Date endTime) {
	
		this.endTime = endTime;
	}
    
}
