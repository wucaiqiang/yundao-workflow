

package com.yundao.workflow.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.yundao.workflow.dto.GroupUserReqDto;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 下午2:46:24 
 * @author   wucq
 * @version   
 */
public interface CustomIdentityService {
	public void setAuthenticatedUserId(String authenticatedUserId)throws Exception;
	public Boolean insertGroupAndUser(Map<String,String> groupUserMap,String groupType)throws Exception;
	public Set<String> selectGroupName()throws Exception;
	
	/**
	 * 添加组
	 * @param groups
	 */
	public void insertGroup(List<String> groups);
	
	/**
	 * 添加组
	 * @param groups
	 */
	public void insertGroup(Long tenantId,String[] groups);
	
	/**
	 * 修改用户组
	 * @param groupUsers
	 * @return
	 */
	public Integer updateGroupUser(List<GroupUserReqDto> deleteGroupUsers);
	
	/**
	 * 修改用户组
	 * @param addGroupIds
	 * @param deleteGroupIds
	 * @return
	 */
	public Integer updateGroupUsers(Long userId, List<String> addGroupIds, List<String> deleteGroupIds);
	
	/**
	 * 用户离职，删除该用户所在当前租户的组关系
	 * @param userIds
	 * @param deleteGroupIds
	 * @return
	 */
	public Integer updateGroupUsersForDimission(List<Long> userIds, List<String> deleteGroupIds);
}

