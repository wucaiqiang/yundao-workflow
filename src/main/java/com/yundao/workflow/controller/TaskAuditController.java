package com.yundao.workflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JavaType;
import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.dto.task.audit.TaskActionResDto;
import com.yundao.workflow.dto.task.audit.TaskPageReqDto;
import com.yundao.workflow.dto.task.audit.TaskPageResDto;
import com.yundao.workflow.service.task.TaskAuditService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("待审核任务")
@RequestMapping("/task/audit/")
public class TaskAuditController {

	@Autowired
	TaskAuditService taskAuditService;
	
	
	/**
	 * 分页查询待审批的任务
	 * getPagePersonal:
	 * @author: 欧阳利
	 * @param dto
	 * @return
	 * @throws BaseException
	 * @description:
	 */
	@ApiOperation(value = "分页查询待审批的任务")
	@RequestMapping(value = "get_page", method = RequestMethod.GET)
    public Result<PaginationSupport<TaskPageResDto>> getPage(@ModelAttribute TaskPageReqDto dto) throws BaseException {
       
	   JavaType javaType = JsonUtils.getCollectionType(List.class, String.class);
	   if(!BooleanUtils.isBlank(dto.getAuditGroups())){
		   dto.setCandidateGroups(JsonUtils.jsonToObject(dto.getAuditGroups(), javaType));
	   }
		
        return taskAuditService.getPage(dto);
    }
	
	
	
	@ApiOperation(value = "获取任务的操作和申请信息")
	@RequestMapping(value = "get_action_and_apply_info", method = RequestMethod.GET)
	public Result<TaskActionResDto> getTaskActionAndApplyInfo(@RequestParam String taskId)throws BaseException{

		return taskAuditService.getTaskActionAndApplyInfo(taskId);
	}

}
