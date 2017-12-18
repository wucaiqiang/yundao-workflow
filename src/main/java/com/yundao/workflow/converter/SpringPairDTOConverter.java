package com.yundao.workflow.converter;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.yundao.core.converter.AbstractConverter;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.dto.PairDTO;

/**
 * 键值对转化类
 * date: 2017年8月14日 上午11:05:38
 * @author:wucq
 * @description:
 */
public class SpringPairDTOConverter extends AbstractConverter<String, List<PairDTO>> {

    private static Log log = LogFactory.getLog(SpringPairDTOConverter.class);

    @Override
    public List<PairDTO> convert(String source) {
        log.info("字符串转化为键值对source=" + source);
        if (BooleanUtils.isNotBlank(source)) {
            try {
                JavaType javaType = JsonUtils.getCollectionType(List.class, PairDTO.class);
                return JsonUtils.jsonToObject(source, javaType);
            }
            catch (Exception e) {
                log.error("键值对转化类异常", e);
            }
        }
        return null;
    }

}
