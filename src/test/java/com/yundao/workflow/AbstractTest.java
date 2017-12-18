package com.yundao.workflow;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yundao.core.constant.CommonConstant;
import com.yundao.core.constant.HeaderConstant;
import com.yundao.core.constant.MethodConstant;
import com.yundao.core.threadlocal.filter.RequestCommonParams;
import com.yundao.core.utils.EDUtils;
import com.yundao.core.utils.FileUtils;
import com.yundao.core.utils.HttpUtils;


public abstract class AbstractTest {
	Logger logger = LoggerFactory.getLogger(this.getClass());


	public void post(String requestUrl, Map<String, Object> methodParams) throws Exception {
		this.doExecute(requestUrl, methodParams, MethodConstant.POST);
	}


	public void get(String requestUrl, Map<String, Object> methodParams) throws Exception {
		this.doExecute(requestUrl, methodParams, MethodConstant.GET);
	}

	/**
	 * 获取头部用户信息
	 * 
	 * @return
	 */
	public Map<String, String> getHeaderParams() {
		Map<String, String> result = new HashMap<String, String>();
		result.put(HeaderConstant.HEADER_USER_ID, "5");
		result.put(HeaderConstant.HEADER_REAL_NAME, "ouyangping");
		result.put(HeaderConstant.HEADER_TENANT_ID, 1+"");
		result.put(HeaderConstant.USER_ROLE, "5");
		result.put(HeaderConstant.USER_ROLE, "5");
		return result;
	}
	
	/**
	 * 获取头部用户信息
	 * 
	 * @return
	 */
	public Map<String, String> getHeaderParams1() {
		Map<String, String> result = new HashMap<String, String>();
		result.put(HeaderConstant.HEADER_USER_ID, "5");
		result.put(HeaderConstant.HEADER_REAL_NAME, "ouyangping");
		result.put(HeaderConstant.HEADER_TENANT_ID, 1+"");
		result.put(HeaderConstant.USER_ROLE, "5");
		return result;
	}


	private void doExecute(String requestUrl, Map<String, Object> methodParams, String method) throws Exception {
		Map<String, String> headerParams = this.getHeaderParams();
		RequestCommonParams requestParams = RequestCommonParams.newDefault();
		requestParams.setHeaderParams(headerParams);
		requestParams.setMethodParams(methodParams);
		String url ="http://localhost:8208";

		String result = null;
		if (MethodConstant.GET.equals(method)) {
			result = HttpUtils.get(FileUtils.getRealPath(url, requestUrl), requestParams);
		} else {
			result = HttpUtils.post(FileUtils.getRealPath(url, requestUrl), requestParams);
		}
		//logger.info(result);
	}

}
