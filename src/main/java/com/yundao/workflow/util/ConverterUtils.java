package com.yundao.workflow.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;

import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.ListUtils;
import com.yundao.workflow.converter.DateConverter;
import com.yundao.workflow.converter.ListConverter;
import com.yundao.workflow.dto.PairDTO;
import com.yundao.workflow.enums.ParameterTypeEnum;

/**
 * 类型转换工具类
 *
 * @author wupengfei wupf86@126.com
 */
public abstract class ConverterUtils {
	static {
		// 注册日期转换类和集合转换类
		ConvertUtils.register(new DateConverter(), Date.class);
		ConvertUtils.register(new ListConverter(), List.class);
	}

	/**
	 * 获取转换后的参数
	 *
	 * @param pairList
	 *            键值对集合
	 * @return 转换后的参数
	 */
	public static Map<String, Object> getConvertMap(List<PairDTO> pairList) {
		Map<String, Object> result = new HashMap<>();
		if (pairList == null || pairList.isEmpty()) {
			return result;
		}
		int size = ListUtils.getSize(pairList);
		for (int i = 0; i < size; i++) {
			PairDTO each = pairList.get(i);
			Object value = doConvert(each.getValue(), each.getValueType());
			result.put(each.getKey(), value);
		}
		return result;
	}

	/**
	 * 获取转换后的值
	 *
	 * @param value
	 *            待转换的值
	 * @param valueType
	 *            值类型
	 * @param <T>
	 *            返回的值类型
	 * @return 转换后的值
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getConvertValue(String value, String valueType) {
		return (T) doConvert(value, valueType);
	}

	/**
	 * 类型转换
	 *
	 * @param value
	 *            待转换的值
	 * @param valueType
	 *            值类型
	 * @return 转换后的值
	 */
	private static Object doConvert(String value, String valueType) {
		if (BooleanUtils.isBlank(value)) {
			return null;
		}
		valueType = BooleanUtils.isBlank(valueType) ? ParameterTypeEnum.STRING.toString() : valueType;
		Class<?> targetType = Enum.valueOf(ParameterTypeEnum.class, valueType).getValue();
		return ConvertUtils.convert(value, targetType);
	}
}
