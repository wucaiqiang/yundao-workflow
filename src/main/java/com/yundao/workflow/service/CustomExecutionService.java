

package com.yundao.workflow.service;

import com.yundao.core.code.Result;
import com.yundao.workflow.dto.ProcessStartReqDTO;
import com.yundao.workflow.dto.ProcessStartResDTO;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月19日 上午10:29:29 
 * @author   wucq
 * @version   
 */
public interface CustomExecutionService {
  public Result<ProcessStartResDTO> doStart(ProcessStartReqDTO taskStartReqDTO)throws Exception;
  public Result<Boolean> processIsEnd(String businessKey)throws Exception;
  public Result<byte[]> selectProcessPicture(String businessKey)throws Exception;
}

