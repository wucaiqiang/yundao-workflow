

package com.yundao.workflow.controller;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.yundao.core.code.Result;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.JsonUtils;
import com.yundao.workflow.dto.task.action.TaskActionDto;
import com.yundao.workflow.service.CustomDeploymentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Function: 
 * Reason:	  
 * Date:     2017年7月20日 下午2:34:35 
 * @author   wucq
 * @version   
 */
@RestController
@ResponseBody
@Api("工作流部署")
public class DeploymentController {
	private static Log log = LogFactory.getLog(ExecutionController.class);
	@Autowired
	private CustomDeploymentService customDeploymentService; 
	/**
     * 部署资源
     *
     * @param file 资源文件
     * @return 部署成功则返回true，否则false
     * @throws Exception
     */
    @RequestMapping(value = "/deploy/resource",method=RequestMethod.POST)
    public Result<Boolean> deployResource(@RequestParam(value = "file") MultipartFile file,@RequestParam String deploymentName,@RequestParam String taskActionDtos) throws Exception {
        // 产品上线申请：[{"taskDefinitionKey":"product_audit_apply","actionValues":[1,2,3]},{"taskDefinitionKey":"product_reject_edit","actionValues":[4,3]}]
    	// 预约审批：[{"taskDefinitionKey":"product_reservation_audit","actionValues":[1,2,3]},{"taskDefinitionKey":"product_reservation_reject_edit","actionValues":[4,3]}]
    	// 报单审批：[{"taskDefinitionKey":"declaration_audit","actionValues":[1,2,3]},{"taskDefinitionKey":"declaration_reject_edit","actionValues":[4,3]}]
    	// 公告审批：[{"taskDefinitionKey":"product_notice_audit","actionValues":[1,2,3]},{"taskDefinitionKey":"product_notice_reject_edit","actionValues":[4,3]}]
    	// 结佣审批：[{"taskDefinitionKey":"knot_commission_audit","actionValues":[1,5]}]
    	// 退款审批：[{"taskDefinitionKey":"refund_audit","actionValues":[1,2,3]},{"taskDefinitionKey":"refund_reject_edit","actionValues":[4,3]}]

    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(taskActionDtos, javaType);
    	
    	return customDeploymentService.doDeployResource(file.getInputStream(), file.getOriginalFilename(),deploymentName,list);
    }
    
    @ApiOperation(value = "部署公共流程")
    @RequestMapping(value = "/deploy/common/resource",method=RequestMethod.POST)
    public Result<Boolean> deployCommonResource()throws Exception{
    	log.info("部署公共流程");
    	log.info("部署产品上线审批");
    	deployProductAudit();
    	log.info("部署公告审批");
    	deployProductNoticeAudit();
    	log.info("部署报单审批");
    	deployDeclarationAudit();
    	log.info("部署预约审批");
    	deployReservationAudit();
    	log.info("部署佣金审批");
    	deployKnotCommissionAudit();
    	log.info("部署退款审批");
		return deployRefundAudit();
    }
    
   
    private Result<Boolean> deployProductAudit()throws Exception{
    	// 部署产品上线审批
    	String json = "[{\"taskDefinitionKey\":\"product_audit_apply\",\"actionValues\":[1,2,3]},{\"taskDefinitionKey\":\"product_reject_edit\",\"actionValues\":[4,3]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/product_audit.zip");
		
		return customDeploymentService.doDeployResource(in, "product_audit.zip","product_audit",list);
    }
    
    
    private Result<Boolean> deployProductNoticeAudit()throws Exception{
    	// 部署公告审批
    	String json = "[{\"taskDefinitionKey\":\"product_notice_audit\",\"actionValues\":[1,2,3]},{\"taskDefinitionKey\":\"product_notice_reject_edit\",\"actionValues\":[4,3]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/product_notice.zip");
		
		return customDeploymentService.doDeployResource(in, "product_notice.zip","product_notice",list);
    }
    
    
    private Result<Boolean> deployReservationAudit()throws Exception{
    	// 部署预约审批
    	String json = "[{\"taskDefinitionKey\":\"product_reservation_audit\",\"actionValues\":[1,2,3]},{\"taskDefinitionKey\":\"product_reservation_reject_edit\",\"actionValues\":[4,3]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/reservation.zip");
		
		return customDeploymentService.doDeployResource(in, "reservation.zip","reservation",list);
    }
    
    private Result<Boolean> deployKnotCommissionAudit()throws Exception{
    	// 部署佣金审批
    	String json = "[{\"taskDefinitionKey\":\"knot_commission_audit\",\"actionValues\":[1,5]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/knot_commission.zip");
		
		return customDeploymentService.doDeployResource(in, "knot_commission.zip","knot_commission",list);
    }
    
    /**
     * 报单部署
     * deployDeclarationAudit:
     * @author: 欧阳利
     * @return
     * @throws Exception
     * @description:
     */
    private Result<Boolean> deployDeclarationAudit()throws Exception{
    	// 部署报单审批
    	String json = "[{\"taskDefinitionKey\":\"declaration_audit\",\"actionValues\":[1,2,3]},{\"taskDefinitionKey\":\"declaration_reject_edit\",\"actionValues\":[4,3]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/declaration.zip");
		
		return customDeploymentService.doDeployResource(in, "declaration.zip","declaration",list);
    }
    
    
    /**
     * 退款审批
     * deployDeclarationAudit:
     * @author: 欧阳利
     * @return
     * @throws Exception
     * @description:
     */
    private Result<Boolean> deployRefundAudit()throws Exception{
    	// 退款审批
    	String json = "[{\"taskDefinitionKey\":\"refund_audit\",\"actionValues\":[1,2,3]},{\"taskDefinitionKey\":\"refund_reject_edit\",\"actionValues\":[4,3]}]";
    	JavaType javaType = JsonUtils.getCollectionType(List.class, TaskActionDto.class);
		List<TaskActionDto> list = JsonUtils.jsonToObject(json, javaType);
		
		InputStream in = this.getClass().getResourceAsStream("/config/flowchart/common/zip/refund.zip");
		
		return customDeploymentService.doDeployResource(in, "refund.zip","refund",list);
    }
    
    
    
}

