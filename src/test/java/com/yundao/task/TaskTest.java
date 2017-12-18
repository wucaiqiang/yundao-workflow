package com.yundao.task;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class TaskTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	
	@Test
	public void findProcessDefinition(){
		List<ProcessDefinition> list = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
						.createProcessDefinitionQuery()//创建一个流程定义的查询
						/**指定查询条件,where条件*/
						.deploymentId("93500")//使用部署对象ID查询
//						.processDefinitionId("")//使用流程定义ID查询
//						.processDefinitionKey(processDefinitionKey)//使用流程定义的key查询
//						.processDefinitionNameLike(processDefinitionNameLike)//使用流程定义的名称模糊查询

						/**排序*/
						.orderByProcessDefinitionVersion().asc()//按照版本的升序排列
//						.orderByProcessDefinitionName().desc()//按照流程定义的名称降序排列

						/**返回的结果集*/
						.list();//返回一个集合列表，封装流程定义
//						.singleResult();//返回惟一结果集
//						.count();//返回结果集数量
//						.listPage(firstResult, maxResults);//分页查询
		if(list!=null && list.size()>0){
			for(ProcessDefinition pd:list){
				System.out.println("流程定义ID:"+pd.getDescription());//流程定义的key+版本+随机生成数
				System.out.println("流程定义ID:"+pd.getId());//流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称:"+pd.getName());//对应helloworld.bpmn文件中的name属性值
				System.out.println("流程定义的key:"+pd.getKey());//对应helloworld.bpmn文件中的id属性值
				System.out.println("流程定义的版本:"+pd.getVersion());//当流程定义的key值相同的相同下，版本升级，默认1
				System.out.println("资源名称bpmn文件:"+pd.getResourceName());
				System.out.println("资源名称png文件:"+pd.getDiagramResourceName());
				System.out.println("部署对象ID："+pd.getDeploymentId());
				System.out.println("#########################################################");
			}
		}
	}

	/**查询当前人的个人任务*/
	@Test
	public void findMyPersonalTask(){
		String assignee = "欧阳利";
		String tenantId = "1";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						/**查询条件（where部分）*/
						.taskAssignee(assignee)//指定个人任务查询，指定办理人
						.taskTenantId(tenantId)
						
						/**排序*/
						.orderByTaskCreateTime().asc()//使用创建时间的升序排列
						/**返回结果集*/
						.list();//返回列表
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("父亲ID:"+task.getParentTaskId());
				System.out.println("########################################################");
			}
		}
	}
	
	/**查询当前人的组任务*/
	@Test
	public void findMyGroupTask(){
		String candidateUser = "欧阳利";
		String tenantId = "1";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						/**查询条件（where部分）*/
						.taskCandidateUser(candidateUser)//组任务的办理人查询
						//.taskCandidateGroup(candidateGroup)
						.taskTenantId(tenantId)
						/**排序*/
						.orderByTaskCreateTime().asc()//使用创建时间的升序排列
						/**返回结果集*/
						.list();//返回列表
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	
	/**查询当前人的组任务*/
	@Test
	public void findGroupTask(){
		String candidateGroup = "1_product_audit";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
						.createTaskQuery()//创建任务查询对象
						/**查询条件（where部分）*/
						.taskCandidateGroup(candidateGroup)
						/**排序*/
						.orderByTaskCreateTime().asc()//使用创建时间的升序排列
						/**返回结果集*/
						.list();//返回列表
		if(list!=null && list.size()>0){
			for(Task task:list){
				System.out.println("任务ID:"+task.getId());
				System.out.println("任务名称:"+task.getName());
				System.out.println("任务的创建时间:"+task.getCreateTime());
				System.out.println("任务的办理人:"+task.getAssignee());
				System.out.println("流程实例ID："+task.getProcessInstanceId());
				System.out.println("执行对象ID:"+task.getExecutionId());
				System.out.println("流程定义ID:"+task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
	}
	
	/**完成我的任务*/
	@Test
	public void completeMyPersonalTask(){
		//任务ID
		String taskId = "7504";
		processEngine.getTaskService()//与正在执行的任务管理相关的Service
					.complete(taskId);
		System.out.println("完成任务：任务ID："+taskId);
	}
	
	
	/**查询正在执行的任务办理人表*/
	@Test
	public void findRunPersonTask(){
		//任务ID
		String taskId = "6204";
		List<IdentityLink> list = processEngine.getTaskService()//
					.getIdentityLinksForTask(taskId);
		if(list!=null && list.size()>0){
			for(IdentityLink identityLink:list){
				System.out.println(identityLink.getTaskId()+"   "+identityLink.getType()+"   "+identityLink.getProcessInstanceId()+"   "+identityLink.getUserId());
			}
		}
	}
	
	/**查询历史任务的办理人表*/
	@Test
	public void findHistoryPersonTask(){
		//流程实例ID
		String processInstanceId = "6201";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()//
						.getHistoricIdentityLinksForProcessInstance(processInstanceId);
		if(list!=null && list.size()>0){
			for(HistoricIdentityLink identityLink:list){
				System.out.println(identityLink.getTaskId()+"   "+identityLink.getType()+"   "+identityLink.getProcessInstanceId()+"   "+identityLink.getUserId());
			}
		}
	}
	
	
	/**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
	@Test
	public void claim(){
		//将组任务分配给个人任务
		//任务ID
		String taskId = "7504";
		//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		String userId = "张三";
		processEngine.getTaskService().claim(taskId, userId);
	}

	

	
}
