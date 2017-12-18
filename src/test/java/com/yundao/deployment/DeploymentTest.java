package com.yundao.deployment;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class DeploymentTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**部署流程定义（从inputStream）*/
	@Test
	public void deploymentProcessDefinition_inputStream(){
		// 租户id
		String tenantId = "1";
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("task.bpmn");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
						.createDeployment()//创建一个部署对象
						.name("产品上线审核")//添加部署的名称
						.addInputStream("task.bpmn", inputStreamBpmn)//
						.tenantId(tenantId)
						.deploy();//完成部署
		System.out.println("部署ID："+deployment.getId());//
		System.out.println("部署名称："+deployment.getName());//
		System.out.println("租户id："+deployment.getTenantId());//
	}
	
	/**启动流程实例*/
	@Test
	public void startProcessInstance(){
		// 租户id
		String tenantId = "1";
		//流程定义的key
		String processDefinitionKey = "task";
		// 流程变量
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("tenantId", tenantId);
		
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
				        .startProcessInstanceByKeyAndTenantId(processDefinitionKey, variables, tenantId);
		System.out.println("部署ID:"+pi.getDeploymentId());//流程定义ID   
		System.out.println("流程实例ID:"+pi.getId());//流程实例ID  
		System.out.println("流程定义ID:"+pi.getProcessDefinitionId());//流程定义ID   
		
	}
	
}
