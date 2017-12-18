

package com.yundao.workflow.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午8:33:59 
 * @author   欧阳利
 * @version   
 */
public interface GroupUserMapper {
    
	/**
	 * 删除用户和组的关系
	 * deleteTenantGroupUser:
	 * @author: 欧阳利
	 * @param tenantIdPrefix
	 * @return
	 * @description:
	 */
	int  deleteTenantGroupUser(@Param("tenantIdPrefix") String tenantIdPrefix);



}

