package com.yundao.workflow.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

/**
 * 任务开始时的请求传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class ProcessStartReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    
    @ApiModelProperty(value = "流程定义key")
    private String processDefineKey;
    
    /**
     * 键值对列表
     */
    private List<PairDTO> pairList;


    public String getProcessDefineKey() {
		return processDefineKey;
	}

	public void setProcessDefineKey(String processDefineKey) {
		this.processDefineKey = processDefineKey;
	}

	public List<PairDTO> getPairList() {
        return pairList;
    }

    public void setPairList(List<PairDTO> pairList) {
        this.pairList = pairList;
    }
}
