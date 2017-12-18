
package com.yundao.workflow.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Function: Reason: Date: 2017年7月15日 下午7:45:27
 * 
 * @author wucq
 * @version
 */
public class BusinessKeyGenerator {
	
	private static volatile int sequence = 1;
	public static String generate(String suffix) {
		synchronized (BusinessKeyGenerator.class) {
			StringBuilder buider = new StringBuilder();
			buider.append((sequence++) + "-");
			if (sequence > 1000) {
				sequence = 1;
			}
			String uuid=String.valueOf(System.currentTimeMillis());
			buider.append(uuid);
			if(StringUtils.isNotBlank(suffix)){
				buider.append("-"+suffix);
			}
			return buider.toString();
		}
	}
}
