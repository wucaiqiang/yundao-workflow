
package com.yundao.workflow.service.impl;

import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.exception.BaseException;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.ListUtils;
import com.yundao.workflow.constant.CodeConstant;
import com.yundao.workflow.dto.ProcessDefinitionDTO;
import com.yundao.core.service.AbstractService;
import com.yundao.workflow.service.CustomRepositoryService;

/**
 * Function: Reason: Date: 2017年7月19日 下午2:30:53
 * 
 * @author wucq
 * @version
 */
@Service
public class CustomRepositoryServiceImpl extends AbstractService implements CustomRepositoryService {
	private Log logger = LogFactory.getLog(CustomHistoryServiceImpl.class);
	@Autowired
	private RepositoryService repositoryService;

	@Override
	public Result<ProcessDefinitionDTO> selectByResourceName(String resourceName) throws Exception {

		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
				.processDefinitionResourceName(resourceName).orderByProcessDefinitionVersion().desc();
		List<ProcessDefinition> list = processDefinitionQuery.list();
		int size = ListUtils.getSize(list);
		if (size == 0) {
			logger.info("资源名有误resourceName=" + resourceName);
			throw new BaseException(CodeConstant.CODE_1100001);
		}
		ProcessDefinitionDTO result = getProcessDefinitionDTO(list.get(0));
		return Result.newSuccessResult(result);
	}

	@Override
	public Result<BpmnModel> selectBpmnModel(String processDefinitionId) throws Exception {
		 BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		 if(bpmnModel ==null){
			 throw new BaseException(CodeConstant.CODE_1100009);
		 }
		return Result.newSuccessResult(bpmnModel);
		
	}

	private ProcessDefinitionDTO getProcessDefinitionDTO(ProcessDefinition processDefinition) {
		if(processDefinition ==null){
			return null;
		}
		ProcessDefinitionDTO result = new ProcessDefinitionDTO();
		BeanUtils.copyProperties(processDefinition, result);
		return result;
	}
}
