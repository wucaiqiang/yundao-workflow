package com.yundao.workflow.enums;


import java.util.Date;
import java.util.List;

import com.yundao.core.utils.BooleanUtils;

/**
 * 参数数据类型枚举类
 *
 * @author wupengfei wupf86@126.com
 */
public enum ParameterTypeEnum {

    /**
     * 布尔型
     */
    BOOLEAN(Boolean.class),

    /**
     * 字节
     */
    BYTE(Byte.class),

    /**
     * 字符
     */
    CHAR(Character.class),

    /**
     * 双浮点型
     */
    DOUBLE(Double.class),

    /**
     * 浮点型
     */
    FLOAT(Float.class),

    /**
     * 字符串
     */
    STRING(String.class),

    /**
     * 整形
     */
    INTEGER(Integer.class),

    /**
     * 长整形
     */
    LONG(Long.class),

    /**
     * 短整形
     */
    SHORT(Short.class),

    /**
     * 集合
     */
    LIST(List.class),

    /**
     * 日期型
     */
    DATE(Date.class);

    private Class<?> clazz;

    ParameterTypeEnum(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * 如果为空则返回默认的类型，STRING
     *
     * @param valueType 值类型
     * @return 值类型
     */
    public static String getDefaultIfNull(String valueType) {
        return BooleanUtils.isBlank(valueType) ? ParameterTypeEnum.STRING.toString() : valueType;
    }

    public Class<?> getValue() {
        return clazz;
    }
}