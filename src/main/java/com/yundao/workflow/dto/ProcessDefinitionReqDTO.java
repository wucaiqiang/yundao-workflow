package com.yundao.workflow.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * 流程定义请求传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class ProcessDefinitionReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义id
     */
    private String id;

    /**
     * 流程定义id集合
     */
    private Set<String> ids;

    /**
     * 类别名称是来自在定义元素的targetNamespace属性
     */
    private String category;

    /**
     * 不等于某个类别名称
     */
    private String categoryNotEquals;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 部署id
     */
    private String deploymentId;

    /**
     * 部署id集合
     */
    private Set<String> deploymentIds;

    /**
     * 流程唯一的键
     */
    private String key;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 启动流程的用户
     */
    private String initiator;

    /**
     * 租用者id
     */
    private String tenantId;

    /**
     * 是否挂起
     */
    private Boolean isSuspended;

    /**
     * 是否活动
     */
    private Boolean isActive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getIds() {
        return ids;
    }

    public void setIds(Set<String> ids) {
        this.ids = ids;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryNotEquals() {
        return categoryNotEquals;
    }

    public void setCategoryNotEquals(String categoryNotEquals) {
        this.categoryNotEquals = categoryNotEquals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public Set<String> getDeploymentIds() {
        return deploymentIds;
    }

    public void setDeploymentIds(Set<String> deploymentIds) {
        this.deploymentIds = deploymentIds;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getSuspended() {
        return isSuspended;
    }

    public void setSuspended(Boolean suspended) {
        isSuspended = suspended;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
