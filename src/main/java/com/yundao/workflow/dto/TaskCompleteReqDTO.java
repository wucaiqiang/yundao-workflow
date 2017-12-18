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
public class TaskCompleteReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @NotNull(message = "{" + CodeConstant.CODE_1100003 + "}")
    @ApiModelProperty(value = "任务ID")
    private String taskId;
    /**
     * 内容
     */
    @ApiModelProperty(value = "审核内容")
    private String comment;
    /**
     * 动作值 ，例如：1：通过  2：驳回 3：取消 4：重新提交
     */
    @ApiModelProperty(value = "动作值")
    private Integer actionValue;

    private List<PairDTO> pairList;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
	
		return comment;
	}

	public void setComment(String comment) {
	
		this.comment = comment;
	}

	public List<PairDTO> getPairList() {
	
		return pairList;
	}

	public void setPairList(List<PairDTO> pairList) {
	
		this.pairList = pairList;
	}

	public Integer getActionValue() {
		return actionValue;
	}

	public void setActionValue(Integer actionValue) {
		this.actionValue = actionValue;
	}
}
