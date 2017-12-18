

package com.yundao.workflow.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yundao.workflow.dto.task.audit.TaskAuditApplyDto;
import com.yundao.workflow.dto.varinst.ApplyUserVariableDto;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 下午8:33:59 
 * @author   欧阳利
 * @version   
 */
public interface VariableMapper {
    
	/**
	 * 查询流程实例的申请人
	 * getApplyUserVariableDto:
	 * @author: 欧阳利
	 * @param processInstanceIds
	 * @return
	 * @description:
	 */
	List<ApplyUserVariableDto>  getApplyUserVariableDto(@Param("processInstanceIds")List<String> processInstanceIds);


    /**
     * 获取任务的申请人和节点信息
     * @param taskId
     * @param tenantId
     * @return
     */
	TaskAuditApplyDto getTaskAuditApplyDto(@Param("taskId")String taskId,@Param("tenantId")Long tenantId);

	/**
	 * 查询流程的发起人
	 * getInstanceApplyUserId:
	 * @author: 欧阳利
	 * @param instanceId
	 * @param tenantId
	 * @return
	 * @description:
	 */
	Long getInstanceApplyUserId(@Param("instanceId")String instanceId, @Param("tenantId")Long tenantId);
}

