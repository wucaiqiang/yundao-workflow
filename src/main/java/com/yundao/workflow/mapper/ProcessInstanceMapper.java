

package com.yundao.workflow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yundao.workflow.dto.processInstance.ProcessBusinessDto;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午5:30:52 
 * @author   欧阳利
 * @version   
 */
public interface ProcessInstanceMapper {
	
	/**
	 * 查询业务key对应的流程实例id
	 * getProcessBusinessDtos:
	 * @author: 欧阳利
	 * @return
	 * @description:
	 */
    public List<ProcessBusinessDto> getProcessBusinessDtos(@Param("processInstanceIds")List<String> processInstanceIds);



    /**
     * 获取任务业务key
     * @param taskId
     * @return
     */
    public String getBusinessKey(@Param("taskId")String taskId,@Param("tenantId")Long tenantId);

}

