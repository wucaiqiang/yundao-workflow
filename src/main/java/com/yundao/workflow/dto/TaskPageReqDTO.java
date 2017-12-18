package com.yundao.workflow.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 任务返回传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class TaskPageReqDTO extends BasePageDto {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "需要过虑的节点类型")
	private String filtNodeCategorys;
	public String getFiltNodeCategorys() {
	
		return filtNodeCategorys;
	}
	public void setFiltNodeCategorys(String filtNodeCategorys) {
	
		this.filtNodeCategorys = filtNodeCategorys;
	}
	

}
