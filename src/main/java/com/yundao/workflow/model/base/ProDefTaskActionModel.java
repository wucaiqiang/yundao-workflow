package com.yundao.workflow.model.base;

import com.yundao.core.base.model.BaseModel;
import java.io.Serializable;

public class ProDefTaskActionModel extends BaseModel implements Serializable {
    /**
	 * 任务节点key
	 */
    private String taskDefKey;

    /**
	 * 流程定义id
	 */
    private String proDefId;

    /**
	 * 操作值
	 */
    private Integer actionValue;

    /**
	 * 排序，越小越靠前
	 */
    private Integer sequence;

    private static final long serialVersionUID = 1L;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getProDefId() {
        return proDefId;
    }

    public void setProDefId(String proDefId) {
        this.proDefId = proDefId;
    }

    public Integer getActionValue() {
        return actionValue;
    }

    public void setActionValue(Integer actionValue) {
        this.actionValue = actionValue;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
}