

package com.yundao.workflow.service.task;

import org.springframework.web.bind.annotation.RequestParam;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.workflow.dto.task.audit.TaskActionResDto;
import com.yundao.workflow.dto.task.audit.TaskPageReqDto;
import com.yundao.workflow.dto.task.audit.TaskPageResDto;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年9月21日 上午10:33:58 
 * @author   欧阳利
 * @version   
 */
public interface TaskAuditService {
	
	/**
	 * 分页查询待办任务
	 * getPage:
	 * @author: 欧阳利
	 * @param dto
	 * @return
	 * @throws BaseException
	 * @description:
	 */
	public Result<PaginationSupport<TaskPageResDto>> getPage(TaskPageReqDto dto) throws BaseException;

	
	/**
	 * 查询任务操作和申请信息
	 * @param taskId
	 * @return
	 * @throws BaseException
	 */
	public Result<TaskActionResDto> getTaskActionAndApplyInfo(@RequestParam String taskId)throws BaseException;
}

