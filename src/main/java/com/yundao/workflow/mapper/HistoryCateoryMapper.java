

package com.yundao.workflow.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午8:33:59 
 * @author   欧阳利
 * @version   
 */
public interface HistoryCateoryMapper {
    
	
	int updateRunTask(@Param("cateory") String cateory ,@Param("tenantId") String tenantId);
	
	
	int updateHistoryTask(@Param("cateory") String cateory ,@Param("tenantId") String tenantId);



}

