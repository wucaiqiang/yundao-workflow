package com.yundao.workflow.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.validator.spring.BindingResultHandler;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.TaskCompleteReqDTO;
import com.yundao.workflow.dto.TaskCompleteResDTO;
import com.yundao.workflow.dto.TaskPageReqDTO;
import com.yundao.workflow.dto.TaskResDTO;
import com.yundao.workflow.service.CustomTaskService;

import io.swagger.annotations.Api;

@RestController
@Api("任务相关")
public class TaskController {
	@Autowired
	private CustomTaskService customTaskService;

	
	/**
	 * 获取任务的业务key
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/task/get_businessKey", method = RequestMethod.GET)
	@ResponseBody
	public Result<String> getBusinessKey(@RequestParam String taskId)throws Exception{
		String businessKey = customTaskService.getBusinessKey(taskId);
		return Result.newSuccessResult(businessKey);
	}
	
	
	
	/**
	 * 完成任务
	 *
	 * @param completeReqDTO
	 *            完成任务的请求对象
	 * @param bindingResult
	 *            校验的对象
	 * @return 是否成功
	 */
	@RequestMapping(value = "/task/complete", method = RequestMethod.POST)
	@ResponseBody
	public Result<TaskCompleteResDTO> complete(@Validated @ModelAttribute TaskCompleteReqDTO completeReqDTO,
			BindingResult bindingResult) throws Exception {
		BindingResultHandler.handleByException(bindingResult);
		// 完成任务
		Result<TaskCompleteResDTO> result = customTaskService.doComplete(completeReqDTO);
		
		// 设置任务的类别
		customTaskService.doSetCategory();
		
		return result;
	}

	/**
	 * 根据任务ID获取任务信息 getTaskByTaskId:
	 * 
	 * @author: wucq
	 * @param taskId
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/get", method = RequestMethod.GET)
	@ResponseBody
	public Result<TaskResDTO> getTaskByTaskId(@RequestParam String taskId) throws Exception {
		return customTaskService.selectTaskByTaskId(taskId);
	}

	/**
	 * 获取我的任务 getTasks:
	 * 
	 * @author: wucq
	 * @param businessKey
	 *            业务流水号
	 * @param taskStatus
	 *            任务状态：例如 DONE（已完成） TODO(待办) 空代表查询DONE（已完成）
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/gets", method = RequestMethod.GET)
	@ResponseBody
	public Result<Map<String, List<TaskResDTO>>> getTasks(@RequestParam(required = false) String businessKeys,
			@RequestParam(required = false) String taskStatus) throws Exception {
		return customTaskService.selectTasks(businessKeys, taskStatus);
	}

	/**
	 * 获取用户的所有任务
	 * getTaskAll:
	 * 
	 * @author: wucq
	 * @param taskStatus 例如 DONE（已完成） TODO(待办) 空代表查询DONE（已完成）
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/user/get_all", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<TaskResDTO>> getTaskAll(@RequestParam(required = false) String taskStatus) throws Exception {
		return customTaskService.selectTasksByTaskStatus(taskStatus);
	}
	/**
	 * 获取用户所有已完成的任务
	 * getDoneTaskAll:
	 * @author: wucq
	 * @param orderBy 默认按任务创建时间排序，orderBy=endTime 任务审批时间
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/user/done/get_all", method = RequestMethod.GET)
	@ResponseBody
	public Result<PaginationSupport<TaskResDTO>> getDoneTaskAll(@ModelAttribute TaskPageReqDTO dto) throws Exception {
		return customTaskService.selectDoneTaskAll(dto);
	}
	/**
	 * 每个流程只取最新的一个任务 getSingleTasks:
	 * 
	 * @author: wucq
	 * @param businessKeys
	 * @param taskStatus
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/single/gets", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<TaskResDTO>> getSingleTasks(@RequestParam(required = false) String businessKey,
			@RequestParam(required = false) String taskStatus) throws Exception {
		return customTaskService.selectSingleTasks(businessKey, taskStatus);
	}

	/**
	 * 根据资源文件名获取相关的任务列表信息 getTasksByResourceName:
	 * 
	 * @author: wucq
	 * @param resourceName
	 * @param taskStatus
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/task/gets_by_processDefineKey", method = RequestMethod.GET)
	@ResponseBody
	public Result<List<TaskResDTO>> getTasksByResourceName(@RequestParam String processDefineKey,
			@RequestParam(required = false) String taskStatus) throws Exception {
		return customTaskService.selectTasksByResourceName(processDefineKey, taskStatus);
	}

	/**
	 * 领取任务 claim:
	 * 
	 * @author: wucq
	 * @param taskId
	 * @return
	 * @throws BaseException
	 * @description:
	 */
	@RequestMapping(value = "/task/claim", method = RequestMethod.GET)
	@ResponseBody
	public Result<Boolean> claim(@RequestParam String taskId) throws Exception {
		return customTaskService.doClaim(taskId);
	}

	/**
	 * 查询单个业务的任务列表(待办任务，历史任务)
	 * 
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/task/get/all/detail", method = RequestMethod.GET)
	public Result<List<TaskResDTO>> selectBusinessKeyTaskList(@RequestParam String businessKey) throws Exception {
		if (StringUtils.isBlank(businessKey)) {
			throw new BaseException(CodeConstant.CODE_1100010);
		}
		return customTaskService.selectBusinessKeyTaskList(businessKey);
	}

}
