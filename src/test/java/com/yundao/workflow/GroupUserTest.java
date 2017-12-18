package com.yundao.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.yundao.core.constant.HeaderConstant;
import com.yundao.core.threadlocal.filter.RequestCommonParams;
import com.yundao.core.utils.HttpUtils;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.dto.GroupUserReqDto;

public class GroupUserTest {
	/**
	 * 启动工作流
	 *
	 * @throws Exception
	 */
	@Test
	public void updateGroupUsers() throws Exception {
		
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("processDefineKey", "product_reservation");

		/////////////////////////////////////////////////////////
		GroupUserReqDto groupUserReqDto1 = new GroupUserReqDto();
		groupUserReqDto1.setGroupId("product_reservation");
		groupUserReqDto1.setUserId(34l);

		List<GroupUserReqDto> allGroupUsers = new ArrayList<GroupUserReqDto>();
		allGroupUsers.add(groupUserReqDto1);
		methodParams.put("allGroupUsers", JsonUtils.objectToJson(allGroupUsers));
		
		///////////////////////////////////////////////////////////////////
		GroupUserReqDto degroupUserReqDto1 = new GroupUserReqDto();
		degroupUserReqDto1.setGroupId("product_reservation");
		degroupUserReqDto1.setUserId(1l);

		List<GroupUserReqDto> deleteGroupUsers = new ArrayList<GroupUserReqDto>();
		deleteGroupUsers.add(groupUserReqDto1);
		methodParams.put("deleteGroupUsers", JsonUtils.objectToJson(deleteGroupUsers));

		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "1");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
        
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
        
		System.out.println(HttpUtils.post(url+"/group/user/update", requestParams));
	}
	
	
	
	/**
	 * 启动工作流
	 *
	 * @throws Exception
	 */
	@Test
	public void updateGroupUser() throws Exception {
		
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("userId", "0");

		/////////////////////////////////////////////////////////
		List<String> deleteGroupIds = new ArrayList<String>();
		deleteGroupIds.add("product_reservation");
		methodParams.put("deleteGroupIds", JsonUtils.objectToJson(deleteGroupIds));
		
		///////////////////////////////////////////////////////////////////
		List<String> addGroupIds = new ArrayList<String>();
		addGroupIds.add("product_audit");
		methodParams.put("addGroupIds", JsonUtils.objectToJson(addGroupIds));

		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "1");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
        
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
        
		System.out.println(HttpUtils.post(url+"/group/user", requestParams));
	}
}
