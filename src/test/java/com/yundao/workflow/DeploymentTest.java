package com.yundao.workflow;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.yundao.core.config.system.SystemFileConfig;
import com.yundao.core.constant.CommonConstant;
import com.yundao.core.constant.HeaderConstant;
import com.yundao.core.threadlocal.filter.RequestCommonParams;
import com.yundao.core.utils.HttpUtils;

/**
 * 部署测试类
 *
 * @author wupengfei wupf86@126.com
 */
public class DeploymentTest {

    /***
     * 部署模型
     *
     * @throws Exception
     */
    @Test
    public void deployResource() throws Exception {
        Map<String, Object> methodParams = new HashMap<>();
        methodParams.put("deploymentName", "2017-09-02 报单审核");
        
        
        Map<String, String> hp = new HashMap<String,String>();
        hp.put(HeaderConstant.HEADER_USER_ID, "1");
        hp.put(HeaderConstant.HEADER_TENANT_ID, "1");
        hp.put(HeaderConstant.HEADER_REAL_NAME, "admin");
        
        RequestCommonParams requestParams = RequestCommonParams.newDefault();
        requestParams.setMethodParams(methodParams);
        requestParams.setHeaderParams(hp);

        InputStream is = new FileInputStream(new File("D:\\declaration.bpmn"));

        HttpUtils.UploadFileParams file = new HttpUtils.UploadFileParams();
        file.setFileName("declaration.bpmn");
        file.setParameterName("file");
        file.setInputStream(is);

        String url = "http://localhost:8208"; // 121.43.184.215
        String result = HttpUtils.postFiles(url + "/deploy/resource", file, requestParams);
        System.out.println(result);
    }


}
