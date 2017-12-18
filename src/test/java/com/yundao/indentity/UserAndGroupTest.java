package com.yundao.indentity;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;

public class UserAndGroupTest {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	
	@Test
	public void addUser(){
		IdentityService identityService = processEngine.getIdentityService();
		//创建用户
		identityService.saveUser(new UserEntity("欧阳利"));
		identityService.saveUser(new UserEntity("李四"));
		identityService.saveUser(new UserEntity("王五"));
	}
	
	
	@Test
	public void addUserAndGroup1(){
		
		// 租户id
		String tenantId = "2";
		// 功能资源名称
		String resourceName = "product_audit";
		// 功能资源名称对应的组
		String groupId = tenantId+"_"+resourceName;
		/**添加用户角色组*/
		IdentityService identityService = processEngine.getIdentityService();//
		//创建角色
		//identityService.createUserQuery().saveGroup(new GroupEntity(groupId));

		//建立用户和角色的关联关系
		identityService.createMembership("欧阳利", groupId);
		identityService.createMembership("李四", groupId);
		identityService.createMembership("王五", groupId);
		System.out.println("添加组织机构成功");
	}
	
	
	public void deleteAddUserAndGroup1(){
		IdentityService identityService = processEngine.getIdentityService();
		identityService.deleteMembership("欧阳利", "1_product_audit");
	}
	
	
	
	@Test
	public void addUserAndGroup2(){
		/**添加用户角色组*/
		IdentityService identityService = processEngine.getIdentityService();//
		//创建角色
		identityService.saveGroup(new GroupEntity("2_product_audit"));
		//建立用户和角色的关联关系
		identityService.createMembership("欧阳利", "2_product_audit");
		identityService.createMembership("李四", "2_product_audit");
		identityService.createMembership("王五", "2_product_audit");
		System.out.println("添加组织机构成功");
	}
	
	
	public void deleteAddUserAndGroup2(){
		IdentityService identityService = processEngine.getIdentityService();
		identityService.deleteMembership("欧阳利", "2_product_audit");
	}
   
	
}
