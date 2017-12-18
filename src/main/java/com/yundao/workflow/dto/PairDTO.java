package com.yundao.workflow.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 键值对传输类
 *
 * @author wupengfei wupf86@126.com
 */
public class PairDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 键
     */
    private String key;

    /**
     * 值
     */
    private String value;

    /**
     * 值类型
     *
     * @see com.zcmall.workflow.enums.ParameterTypeEnum
     */
    private String valueType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
