package com.yundao.workflow.model.base;

import com.yundao.core.base.model.BaseModel;
import java.io.Serializable;

public class ActionConfigModel extends BaseModel implements Serializable {
    /**
	 * 动作值
	 */
    private Integer value;

    /**
	 * 动作中文名
	 */
    private String name;

    private static final long serialVersionUID = 1L;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}