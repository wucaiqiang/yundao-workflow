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
import com.yundao.workflow.dto.PairDTO;
import com.yundao.workflow.enums.ParameterTypeEnum;

/**
 * 任务测试类
 *
 * @author wupengfei wupf86@126.com
 */
public class ExecutionTest extends AbstractTest {
//	@Test
//	public void test() throws Exception {
//		String[] arr={"wu","cai"}; 
//		Map input=new HashMap<>();
//		input.put("role", arr);
//		String ttt=JsonUtils.objectToJson(input);
//		Map<String,List<String>> map=JsonUtils.jsonToObject(ttt, Map.class);
//		List<String> aaa=(List)map.get("role");
//		System.out.println(aaa);
//	}

	/**
	 * 启动工作流
	 *
	 * @throws Exception
	 */
	@Test
	public void start() throws Exception {
		
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("processDefineKey", "declaration");

		PairDTO productAccessUserIdPair = new PairDTO();
		productAccessUserIdPair.setKey("productId");
		productAccessUserIdPair.setValue("1");
		productAccessUserIdPair.setValueType(ParameterTypeEnum.INTEGER.toString());
		
		PairDTO productAccessUserIdPair2 = new PairDTO();
		productAccessUserIdPair2.setKey("customerId");
		productAccessUserIdPair2.setValue("1");
		productAccessUserIdPair2.setValueType(ParameterTypeEnum.INTEGER.toString());

		List<PairDTO> pairList = new ArrayList<>();
		pairList.add(productAccessUserIdPair);
		pairList.add(productAccessUserIdPair2);
		methodParams.put("pairList", JsonUtils.objectToJson(pairList));

		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "1");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
        
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
		
		System.out.print(HttpUtils.post(url+"/process/start", requestParams));
	}

	/**
	 * 完成任务
	 *
	 * @throws Exception
	 */
	@Test
	public void complete() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("taskId", "128523");
		methodParams.put("passValue", "1");// 动作值 ，例如：1：通过  2：驳回 3：取消 4：重新提交
		methodParams.put("comment", "this is test11");// false true

//		PairDTO levelPair = new PairDTO();
//		levelPair.setKey("userIds");
//		levelPair.setValue("[3,4,5]");
//		levelPair.setValueType(ParameterTypeEnum.LIST.toString());
//
//		List<PairDTO> pairList = new ArrayList<>();
//		pairList.add(levelPair);
//		methodParams.put("pairList", JsonUtils.objectToJson(pairList));

//		this.post("/task/complete", methodParams);
		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "34");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
        
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
        System.out.println(HttpUtils.post(url+"/task/complete", requestParams));
	}

	/**
	 * 获取任务 gets:
	 * 
	 * @author: wucq
	 * @throws Exception
	 * @description:
	 */
	@Test
	public void gets() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("taskStatus", "TODO");//TODO待办，DONE：完成
		methodParams.put("businessKeys", "7-1504174944997-product_reservation");
		//this.get("/task/gets", methodParams);
		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "34");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
		
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
		
		System.out.println(HttpUtils.get(url+"/task/gets", requestParams));
	}
	
	
	@Test
	public void getAudit() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "34");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
		
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
		
		System.out.println(HttpUtils.get(url+"/task/audit/get_page", requestParams));
	}
	
	
	@Test
	public void getAllDetail() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("businessKey", "7-1504492570317-product_reservation");
		
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "34");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
		
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);
        String url = "http://localhost:8208"; 
		
		System.out.println(HttpUtils.get(url+"/task/get/all/detail", requestParams));
	}
	
	
	
	@Test
	public void getTasksByResourceName() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("resourceName", "product_audit.bpmn");
//		methodParams.put("taskStatus", "TODO");//TODO待办，DONE：完成
		this.get("/task/gets_by_resource_name", methodParams);
	}
	@Test
	public void get() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("taskId", "10047");
		this.get("/task/get", methodParams);
	}

	/**
	 * 领取任务
	 *
	 * @throws Exception
	 */
	@Test
	public void claim() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("taskId", "10047");

		this.get("/task/claim", methodParams);
	}
	@Test
	public void processIsEnd() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("businessKey", "BUSIKEY-1-1500628190757-dy_audit");

		this.get("/process/is_end", methodParams);
	}
	
	@Test
	public void getProcessPicture() throws Exception {
		Map<String, Object> methodParams = new HashMap<>();
		methodParams.put("businessKey", "BUSIKEY-1-1500628190757-dy_audit");
		this.get("/process/get_picture", methodParams);
	}
}
