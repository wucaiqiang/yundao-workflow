package com.yundao.workflow.converter;

import java.util.Date;

import org.apache.commons.beanutils.Converter;

import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.DateUtils;

/**
 * 日期转化类
 * date: 2017年8月14日 上午11:05:24
 * @author:wucq
 * @description:
 */
public class DateConverter implements Converter {

    private static Log log = LogFactory.getLog(DateConverter.class);

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Class type, Object value) {
        try {
            Object result = null;
            if (type == Date.class) {
                result = doConvertToDate(value);
            }
            else if (type == String.class) {
                result = doConvertToString(value);
            }
            return result;
        }
        catch (Exception e) {
            log.error("转换异常", e);
            return null;
        }
    }

    private Date doConvertToDate(Object value) throws Exception {
        Date result = null;

        if (value instanceof String) {
            result = DateUtils.parse((String) value, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        else if (value instanceof Object[]) {
            Object[] array = (Object[]) value;
            if (array.length >= 1) {
                result = doConvertToDate(value);
            }
        }
        else if (Date.class.isAssignableFrom(value.getClass())) {
            result = (Date) value;
        }
        return result;
    }

    private String doConvertToString(Object value) {
        String result = null;
        if (value instanceof Date) {
            result = DateUtils.format((Date) value, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        return result;
    }

}
