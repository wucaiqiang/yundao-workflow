
package com.yundao.workflow.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.GroupUserReqDto;
import com.yundao.workflow.mapper.GroupUserMapper;
import com.yundao.workflow.service.AbstractService;
import com.yundao.workflow.service.CustomIdentityService;

/**
 * Function: Reason: Date: 2017年7月19日 下午2:47:00
 * 
 * @author wucq
 * @version
 */
@Service
public class CustomIdentityServiceImpl extends AbstractService implements CustomIdentityService {
	private Log logger = LogFactory.getLog(CustomIdentityServiceImpl.class);
	@Autowired
	private IdentityService identityService;
	@Autowired
	@Qualifier("processEngineConfiguration")
	private SpringProcessEngineConfiguration springProcessEngineConfiguration;

	@Autowired
	private GroupUserMapper groupUserMapper;


	/**
	 * 用户离职，删除该用户所在当前租户的组关系
	 * @param userIds
	 * @param deleteGroupIds
	 * @return
	 */
	public Integer updateGroupUsersForDimission(List<Long> userIds, List<String> deleteGroupIds){
		logger.info("用户离职：userIds="+userIds);
		if(BooleanUtils.isEmpty(userIds) || BooleanUtils.isEmpty(deleteGroupIds)){
			return 0;
		}
		Long tenantId = this.getHeaderUser().getTenantId();
		// 删除用户组
		if(!BooleanUtils.isEmpty(deleteGroupIds)){
			for(String groupId : deleteGroupIds){
				for(Long userId:userIds){
					identityService.deleteMembership(userId.toString(), tenantId.toString()+"_"+groupId);
				}
			}
		}
		return 1;
	}


	/**
	 * 修改用户组
	 * @param addGroupIds
	 * @param deleteGroupIds
	 * @return
	 */
	public Integer updateGroupUsers(Long userId,List<String> addGroupIds, List<String> deleteGroupIds){
		if(BooleanUtils.isEmpty(addGroupIds) && BooleanUtils.isEmpty(deleteGroupIds)){
			return 0;
		}
		Long tenantId = this.getHeaderUser().getTenantId();
		// 添加组
		List<String> groups= new ArrayList<String>();
		if(!BooleanUtils.isEmpty(addGroupIds)){
			for(String groupId : addGroupIds){
				if(!groups.contains(groupId)){
					groups.add(groupId);
				}
			}
		}

		if(!BooleanUtils.isEmpty(deleteGroupIds)){
			for(String groupId : deleteGroupIds){
				if(!groups.contains(groupId)){
					groups.add(groupId);
				}
			}
		}

		insertGroup(this.getHeaderUser().getTenantId(),groups);

		// 删除用户组
		if(!BooleanUtils.isEmpty(deleteGroupIds)){
			for(String groupId : deleteGroupIds){
				identityService.deleteMembership(userId.toString(), tenantId.toString()+"_"+groupId);
			}
		}


		// 添加组用户
		if(!BooleanUtils.isEmpty(addGroupIds)){
			for(String groupId  : addGroupIds){
				User user = identityService.createUserQuery().userId(userId.toString()).singleResult();
				if(user == null){
					identityService.saveUser(new UserEntity(userId.toString()));
				}
				identityService.deleteMembership(userId.toString(), tenantId.toString()+"_"+groupId);
				identityService.createMembership(userId.toString(), tenantId.toString()+"_"+groupId);
			}
		}


		return 1;
	}


	/**
	 * 修改用户组
	 * @param groupUsers
	 * @return
	 */
	public Integer updateGroupUser(List<GroupUserReqDto> allGroupUsers){
		Long tenantId = this.getHeaderUser().getTenantId();
		
		
		// 删除已经存在组和用户关系
		//delete from act_id_membership where GROUP_ID_ like '1_%'
		groupUserMapper.deleteTenantGroupUser(tenantId.toString()+"_");
		
		// 添加组
		List<String> groups= new ArrayList<String>();
		if(!BooleanUtils.isEmpty(allGroupUsers)){
			for(GroupUserReqDto dto : allGroupUsers){
				if(!groups.contains(dto.getGroupId())){
					groups.add(dto.getGroupId());
				}
			}
		}

		insertGroup(this.getHeaderUser().getTenantId(),groups);

		// 添加组用户
		if(!BooleanUtils.isEmpty(allGroupUsers)){
			for(GroupUserReqDto dto : allGroupUsers){
				User user = identityService.createUserQuery().userId(dto.getUserId().toString()).singleResult();
				if(user == null){
					identityService.saveUser(new UserEntity(dto.getUserId().toString()));
				}
				identityService.deleteMembership(dto.getUserId().toString(), tenantId.toString()+"_"+dto.getGroupId());
				identityService.createMembership(dto.getUserId().toString(), tenantId.toString()+"_"+dto.getGroupId());
			}
		}

		return 1;
	}

	/**
	 * 添加组
	 * @param groups
	 */
	private void insertGroup(Long tenantId,List<String> groups){
		if(BooleanUtils.isEmpty(groups)){
			return;
		}
		List<String> groupList = new ArrayList<String>();
		for(String s : groups){
			if(BooleanUtils.isBlank(s)){
				continue;
			}
			groupList.add(tenantId.toString()+"_"+s);
		}
		insertGroup(groupList);
	}

	/**
	 * 添加组
	 * @param groups
	 */
	public void insertGroup(Long tenantId,String[] groups){
		if(BooleanUtils.isEmpty(groups)){
			return;
		}
		List<String> groupList = new ArrayList<String>();
		for(String s : groups){
			if(BooleanUtils.isBlank(s)){
				continue;
			}
			groupList.add(tenantId.toString()+"_"+s);
		}
		insertGroup(groupList);
	}

	/**
	 * 添加组
	 * @param groups
	 */
	public void insertGroup(List<String> groups){
		if(BooleanUtils.isEmpty(groups)){
			return;
		}
		for(String groupId:groups){
			Group oldGroup = identityService.createGroupQuery().groupId(groupId).singleResult();
			if(oldGroup != null){
				continue;
			}
			Group group = new GroupEntity(groupId);
			identityService.saveGroup(group);
		}

	}


	public void setAuthenticatedUserId(String authenticatedUserId) throws Exception {
		identityService.setAuthenticatedUserId(authenticatedUserId);
	}

	@Override
	public Boolean insertGroupAndUser(Map<String, String> groupUserMap, String groupType) throws Exception {
		if (groupUserMap == null || groupUserMap.isEmpty()) {
			return false;
		}
		if (StringUtils.isBlank(groupType)) {
			throw new BaseException(CodeConstant.CODE_1100013);
		}
		try {
			groupUserMap.forEach((k, v) -> {
				// k=init_C0001
				String[] keyValues = k.split("_");
				if (keyValues.length >= 2) {
					String groupName = keyValues[1];
					synchronized (this) {
						String groupId = "";
						// 添加用户组
						Group group = identityService.createGroupQuery().groupType(groupType).groupName(groupName)
								.singleResult();
						if (group == null) {
							groupId = springProcessEngineConfiguration.getIdGenerator().getNextId();
							group = identityService.newGroup(groupId);
							group.setType(groupType);
							group.setName(groupName);
							identityService.saveGroup(group);
						} else {
							groupId = group.getId();
						}
						final String groupId_ = groupId;
						// 添加用户
						if (StringUtils.isNotBlank(v)) {
							// {"role":["caiwu","caiwu01"]}
							Map<String, List<String>> valueMap = JsonUtils.jsonToObject(v, Map.class);
							valueMap.forEach((k1, v1) -> {
								for (int i = 0; i < v1.size(); i++) {
									String userId = "";
									String firstName = v1.get(i);
									UserQuery userQuery = identityService.createUserQuery();
									User user = userQuery.userFirstName(firstName).userLastName(k1).singleResult();
									if (user == null) {
										userId = springProcessEngineConfiguration.getIdGenerator().getNextId();
										user = identityService.newUser(userId);
										user.setFirstName(firstName);
										user.setLastName(k1);
										identityService.saveUser(user);
									} else {
										userId = user.getId();
									}
									User userInGroup = userQuery.memberOfGroup(groupId_).singleResult();
									if (userInGroup == null) {
										identityService.createMembership(userId, groupId_);
									}
								}
							});
						}
					}

				}

			});
		} catch (Exception e) {
			logger.error("流程启动时，流程数据初始化出现异常，异常信息为：", e);
			throw new BaseException(CodeConstant.CODE_1100014);
		}
		return true;

	}

	@Override
	public Set<String> selectGroupName() throws Exception {
		Set<String> groupNames = new HashSet<>();
		String roles = this.getHeaderUserRole();
		if (StringUtils.isNotBlank(roles)) {
			String[] roleAr = roles.split(",");
			for (int i = 0; i < roleAr.length; i++) {
				User user = identityService.createUserQuery().userFirstName(roleAr[i]).userLastName("role")
						.singleResult();
				if (user != null) {
					List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
					if (groups != null && !groups.isEmpty()) {
						for (Group group : groups) {
							groupNames.add(group.getName());
						}
					}
				}

			}
		}
		groupNames.add(String.valueOf(this.getHeaderUser().getUserId()));//添加当前用户ID，兼容同时查询当前用户及其角色相关数据
		
		return groupNames;

	}

}
