package com.yundao.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JavaType;
import com.yundao.core.code.Result;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.dto.GroupUserReqDto;
import com.yundao.workflow.service.CustomIdentityService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("组用户相关接口")
public class GroupUserController {
    
	@Autowired
	CustomIdentityService customIdentityService;
	
	@RequestMapping(value = "/group/users", method = RequestMethod.POST)
	@ApiOperation(value = "修改组用户")
	@ResponseBody
	public Result<Integer> updateGroupUsers(String allGroupUsers){
		
		List<GroupUserReqDto> allGroupUserList =null;
		JavaType javaType = JsonUtils.getCollectionType(List.class, GroupUserReqDto.class);
		if(!BooleanUtils.isBlank(allGroupUsers)){
			allGroupUserList = JsonUtils.jsonToObject(allGroupUsers, javaType);
		}
		
		return Result.newSuccessResult(customIdentityService.updateGroupUser(allGroupUserList));
	}
	
	
	@RequestMapping(value = "/group/user", method = RequestMethod.POST)
	@ApiOperation(value = "修改组用户")
	@ResponseBody
	public Result<Integer> updateGroupUsers(Long userId, String addGroupIds, String deleteGroupIds){
		 List<String> addGroupIdList = null; 
		 List<String> deleteGroupIdList = null;
		 JavaType javaType = JsonUtils.getCollectionType(List.class, String.class);
		 if(!BooleanUtils.isBlank(addGroupIds)){
			 addGroupIdList = JsonUtils.jsonToObject(addGroupIds, javaType);
		 }
         if(!BooleanUtils.isBlank(deleteGroupIds)){
        	 deleteGroupIdList = JsonUtils.jsonToObject(deleteGroupIds, javaType);
		 }

		return Result.newSuccessResult(customIdentityService.updateGroupUsers(userId, addGroupIdList, deleteGroupIdList));
	}
	
	
	@RequestMapping(value = "/group/user/dimission", method = RequestMethod.POST)
	@ApiOperation(value = "修改组用户 -- 用户离职")
	@ResponseBody
	public Result<Integer> updateGroupUsersForDimission(String userIds, String deleteGroupIds){
		 List<Long> userIdList = null; 
		 List<String> deleteGroupIdList = null;
		 JavaType LongJavaType = JsonUtils.getCollectionType(List.class, Long.class);
		 JavaType javaType = JsonUtils.getCollectionType(List.class, String.class);
		 if(!BooleanUtils.isBlank(userIds)){
			 userIdList = JsonUtils.jsonToObject(userIds, LongJavaType);
		 }
         if(!BooleanUtils.isBlank(deleteGroupIds)){
        	 deleteGroupIdList = JsonUtils.jsonToObject(deleteGroupIds, javaType);
		 }
		return Result.newSuccessResult(customIdentityService.updateGroupUsersForDimission(userIdList, deleteGroupIdList));
	}
	
}
