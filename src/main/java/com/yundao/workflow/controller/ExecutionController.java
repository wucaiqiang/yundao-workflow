
package com.yundao.workflow.controller;

import javax.servlet.http.HttpServletResponse;

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
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.validator.spring.BindingResultHandler;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.ProcessStartReqDTO;
import com.yundao.workflow.dto.ProcessStartResDTO;
import com.yundao.workflow.service.CustomExecutionService;
import com.yundao.workflow.service.CustomTaskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Function: Reason: Date: 2017年7月19日 上午10:27:34
 * 
 * @author wucq
 * @version
 */
@RestController
@Api("工作流相关")
public class ExecutionController {
	
	private static Log log = LogFactory.getLog(ExecutionController.class);
	
	@Autowired
	private CustomExecutionService executionService;
	@Autowired
	private CustomTaskService customTaskService;

	/**
	 * 启动工作流
	 *
	 * @param taskStartReqDTO
	 *            任务启动的参数对象
	 * @param bindingResult
	 *            校验的对象
	 * @return 业务编号
	 * @throws BaseException
	 */
	@RequestMapping(value = "/process/start", method = RequestMethod.POST)
	@ApiOperation(value = "启动流程")
	@ResponseBody
	public Result<ProcessStartResDTO> start(@Validated @ModelAttribute ProcessStartReqDTO processStartReqDTO,
			BindingResult bindingResult) throws Exception {
		BindingResultHandler.handleByException(bindingResult);
		// 启动工作流
		Result<ProcessStartResDTO> startResult = executionService.doStart(processStartReqDTO);
		// 根据流程实例id获取任务
		return startResult;
	}

	/**
	 * 
	 * processIsEnd:
	 * 
	 * @author: wucq
	 * @param processStartReqDTO
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 * @description:
	 */
	@RequestMapping(value = "/process/is_end", method = RequestMethod.GET)
	@ApiOperation(value = "判断流程是否已结束")
	@ResponseBody
	public Result<Boolean> processIsEnd(@RequestParam String businessKey) throws Exception {
		Result<Boolean> startResult = executionService.processIsEnd(businessKey);
		// 根据流程实例id获取任务
		return startResult;
	}

//	/**
//	 * 动态修改流程节点权限
//	 * updateIdUserByGroupNameAndResourceName:
//	 * 
//	 * @author: wucq
//	 * @param resourceName
//	 * @param assignee
//	 * @param handleType
//	 * @param roleId
//	 * @return
//	 * @throws Exception
//	 * @description:
//	 */
//	@RequestMapping(value = "/process/update_id_user", method = RequestMethod.POST)
//	@ResponseBody
//	public Result<Boolean> updateIdUserByGroupNameAndResourceName(@RequestParam String resourceName,
//			@RequestParam(required = true) String assignee, @RequestParam(required = true) Integer handleType,
//			@RequestParam(required = true) Integer roleId) throws Exception {
//		return customTaskService.updateIdUserByGroupNameAndResourceName(resourceName, assignee, handleType, roleId);
//	}
	
	/**
	 * 根据流程定义id显示图片
	 *
	 * @param response
	 *            HttpServletResponse
	 * @param processDefinitionId
	 *            流程定义id
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/get_picture", method = RequestMethod.GET)
	public void getPicture(HttpServletResponse response, @RequestParam String businessKey) throws Exception {
		if (StringUtils.isBlank(businessKey)) {
			throw new BaseException(CodeConstant.CODE_1100010);
		}
		/*
		 * Result<byte[]> pictureResult =
		 * executionService.selectProcessPicture(businessKey); File file = new
		 * File("C:\\Users\\Administrator\\Desktop\\workflow\\history_test.png")
		 * ; FileUtils.writeByteArrayToFile(file, pictureResult.getResult(),
		 * true); ResponseUtils.writeByte(response, pictureResult.getResult());
		 */
	}
}
