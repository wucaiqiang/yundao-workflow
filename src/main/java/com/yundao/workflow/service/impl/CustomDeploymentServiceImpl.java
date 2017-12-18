
package com.yundao.workflow.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yundao.core.code.Result;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.service.AbstractService;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.CloseableUtils;
import com.yundao.core.utils.ListUtils;
import com.yundao.workflow.dto.task.action.TaskActionDto;
import com.yundao.workflow.mapper.base.ProDefTaskActionModelMapper;
import com.yundao.workflow.model.base.ProDefTaskActionModel;
import com.yundao.workflow.service.CustomDeploymentService;
import com.yundao.workflow.service.CustomIdentityService;

/**
 * Function: Reason: Date: 2017年7月20日 下午2:38:49
 * 
 * @author wucq
 * @version
 */
@Service
public class CustomDeploymentServiceImpl extends AbstractService implements CustomDeploymentService {
	private Log logger = LogFactory.getLog(CustomDeploymentServiceImpl.class);

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	CustomIdentityService customIdentityService;
	
	@Autowired
	ProDefTaskActionModelMapper proDefTaskActionModelMapper;

	@Override
	public Result<Boolean> doDeployResource(InputStream inputStream, String fileName,String deploymentName,List<TaskActionDto> list) throws Exception {
		boolean result = false;
		ZipInputStream zip = null;
		Long tenantId = this.getHeaderUser().getTenantId();
		try {
			// 部署文件
			Deployment deployment;
			String extension = FilenameUtils.getExtension(fileName);
			if ("zip".equalsIgnoreCase(extension) || "bar".equalsIgnoreCase(extension)) {
				zip = new ZipInputStream(inputStream);
				deployment = repositoryService.createDeployment()
						.name(deploymentName)//添加部署的名称
						.addZipInputStream(zip)
						.tenantId(tenantId.toString())
						.deploy();
			} else {
				deployment = repositoryService.createDeployment()
						.name(deploymentName)//添加部署的名称
						.addInputStream(fileName, inputStream)
						.tenantId(tenantId.toString())
						.deploy();
			}

			// 获取流程的定义
			List<ProcessDefinition> pdList = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deployment.getId()).list();
			int size = ListUtils.getSize(pdList);

			//流程图保存目录
			String classPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
			String uploadPath = com.yundao.core.utils.FileUtils.getRealPath(classPath, "/config/fowchart/");

			for (int i = 0; i < size; i++) {
				ProcessDefinition each = pdList.get(i);
				exportDiagramToFile(repositoryService, each,uploadPath);
				saveTaskAction(each.getId(),list);
			}
			result = true;
			
			initTenantGroups(tenantId, deployment.getId());
		} catch (Exception e) {
			logger.error("流程部署异常", e);
			throw e;
		} finally {
			CloseableUtils.close(zip);
		}
		return Result.newSuccessResult(result);
	}

	
	/**
	 * 保存任务节点的操作
	 * saveTaskAction:
	 * @author: 欧阳利
	 * @param proDefId
	 * @param list
	 * @description:
	 */
	private void saveTaskAction(String proDefId,List<TaskActionDto> list){
		if(BooleanUtils.isEmpty(list)){
			return;
		}
		for(TaskActionDto dto : list){
			if(!BooleanUtils.isEmpty(dto.getActionValues())){
				for(int i=0;i<dto.getActionValues().size();i++){
					ProDefTaskActionModel record = new ProDefTaskActionModel();
					record.setActionValue(dto.getActionValues().get(i));
					record.setSequence(i);
					record.setProDefId(proDefId);
					record.setTaskDefKey(dto.getTaskDefinitionKey());
					record.setIsDelete(0);
					record.setTenantId(this.getHeaderTenantId());
					record.setCreateUserId(this.getHeaderUserId());
					record.setCreateDate(new Date());
					proDefTaskActionModelMapper.insert(record);
				}
				
			}
			
		}
	}
	
	
	
	
	
	private String exportDiagramToFile(RepositoryService repositoryService, ProcessDefinition processDefinition,
			String diagramPath) throws IOException {

		InputStream resourceAsStream = null;
		try {
			// 复制资源流
			String diagramResourceName = processDefinition.getDiagramResourceName();
			resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
					diagramResourceName);
			byte[] bytes = IOUtils.toByteArray(resourceAsStream);

			// 创建路径
			diagramPath = diagramPath + "/" + processDefinition.getVersion() + "/" + diagramResourceName;
			File file = new File(diagramPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// 文件不存在则创建
			if (!file.exists()) {
				file.createNewFile();
				FileUtils.writeByteArrayToFile(file, bytes, true);
			}
			return diagramPath;
		} finally {
			CloseableUtils.close(resourceAsStream);
		}
	}

	
	
	/**
	 * 初始化流程的用户组
	 * @param tenantId
	 * @param businessKey
	 * @throws Exception
	 */
	private void initTenantGroups(Long tenantId,String deploymentId)throws Exception{
		ProcessDefinition processDefinition = repositoryService//与流程定义和部署对象相关的Service
				.createProcessDefinitionQuery()//创建一个流程定义的查询
				/**指定查询条件,where条件*/
				.deploymentId(deploymentId)//使用部署对象ID查询
                .singleResult();//返回惟一结果集
		String description = processDefinition.getDescription();
		
		if(BooleanUtils.isBlank(description)){
			return;
		}
		
		String[] groups = description.split(";");
		if(BooleanUtils.isEmpty(groups)){
			return ;
		}
		
		if (!BooleanUtils.isEmpty(groups)) {
			customIdentityService.insertGroup(tenantId,groups);
		}
	}
	
}
