package com.yundao.workflow.converter;

import java.util.List;

import org.apache.commons.beanutils.Converter;

import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.JsonUtils;

/**
 * 
 * date: 2017年8月14日 上午11:05:13
 * @author:wucq
 * @description:
 */
public class ListConverter implements Converter {

    private static Log log = LogFactory.getLog(ListConverter.class);

    @SuppressWarnings("unchecked")
    @Override
    public Object convert(Class type, Object value) {
        try {
            Object result = null;
            if (type == List.class && value != null) {
                result = JsonUtils.jsonToObject(value.toString(), List.class);
            }
            return result;
        }
        catch (Exception e) {
            log.error("转换异常", e);
            return null;
        }
    }

}
