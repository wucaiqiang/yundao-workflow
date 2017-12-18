

package com.yundao.workflow.service;

import java.io.InputStream;
import java.util.List;

import com.yundao.core.code.Result;
import com.yundao.workflow.dto.task.action.TaskActionDto;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月20日 下午2:38:03 
 * @author   wucq
 * @version   
 */
public interface CustomDeploymentService {
   public Result<Boolean> doDeployResource(InputStream inputStream, String fileName,String deploymentName,List<TaskActionDto> list)throws Exception;
}

