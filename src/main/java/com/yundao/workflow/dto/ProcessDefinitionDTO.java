package com.yundao.workflow.dto;

import java.io.Serializable;

/**
 * 流程定义传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class ProcessDefinitionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义id
     */
    private String id;

    /**
     * 类别名称是来自在定义元素的targetNamespace属性
     */
    private String category;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程唯一的键
     */
    private String key;

    /**
     * 描述
     **/
    private String description;

    /**
     * 版本号
     */
    private int version;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 部署id
     */
    private String deploymentId;

    /**
     * 资源图片名称
     */
    private String diagramResourceName;

    /**
     * 是否有表单key
     */
    private boolean hasStartFormKey;

    /**
     * 是否有图形符号
     */
    private boolean hasGraphicalNotation;

    /**
     * 是否挂起
     */
    private boolean isSuspended;

    /**
     * 租用者id
     */
    private String tenantId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public boolean isHasStartFormKey() {
        return hasStartFormKey;
    }

    public void setHasStartFormKey(boolean hasStartFormKey) {
        this.hasStartFormKey = hasStartFormKey;
    }

    public boolean isHasGraphicalNotation() {
        return hasGraphicalNotation;
    }

    public void setHasGraphicalNotation(boolean hasGraphicalNotation) {
        this.hasGraphicalNotation = hasGraphicalNotation;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
