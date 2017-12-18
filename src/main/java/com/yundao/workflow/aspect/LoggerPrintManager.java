package com.yundao.workflow.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yundao.core.code.Result;
import com.yundao.core.pagination.PaginationSupport;
import com.yundao.core.utils.JsonUtils;
/**
 * 
 * date: 2015-8-21 上午10:53:14
 * @author:wucq
 * @description:spring aop来管理日记与异常处理
 */
@Aspect
@Component
public class LoggerPrintManager {
	
	  private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Pointcut("(execution(* com.yundao.workflow.controller..*.*(..))) && (!execution(* com.yundao.workflow.aspect..*.*(..)))")  
	private void printLoggerMethod() {  
    } 
	/**
	 * 打印日记
	 * @author: wucq
	 * @param pjp
	 * @return
	 * @throws Throwable
	 * @description:TODO
	 */
	 //声明环绕通知  
    @Around("printLoggerMethod()")  
    public Object pringLog(ProceedingJoinPoint pjp) throws Throwable { 
    	 if(pjp == null ){
    		 logger.info("com.yundao.workflow.aspect.LoggerPrintManager [pringLog] param is null");
    		 return null;
    	 }
    	 Signature signature=pjp.getSignature();
    	 String packageAndClassName="";
    	 String methodName="";
    	 
    	 if(signature !=null ){
    		  packageAndClassName=signature.getDeclaringTypeName();
        	  methodName=signature.getName();
    	 }
    	 if(signature ==null || StringUtils.isEmpty(packageAndClassName) || StringUtils.isEmpty(methodName)){
    		logger.info("com.yundao.workflow.aspect.LoggerPrintManager [pringLog] param is null");
    		return null;
    	 }
		 logger.info(packageAndClassName+" ["+methodName+"] begin!"); 
		 logger.info(packageAndClassName+" ["+methodName+"] params is : "+Arrays.toString(pjp.getArgs())); 
         //被代理方法执行
		 Object retObject=pjp.proceed();
		 if(retObject !=null && retObject instanceof Result){
			 try {
				 Result result=(Result)retObject;
				 Object resultObj=result.getResult();
				 if(resultObj instanceof PaginationSupport || resultObj instanceof List){
					 List resultList=new ArrayList<>();
					 if(resultObj instanceof PaginationSupport){
						 PaginationSupport pagination=(PaginationSupport)resultObj;
						 if(pagination !=null ){
							 resultList=pagination.getDatas();
						 }
					 }else{
						 resultList=(List)resultObj;
					 }
					 logger.info(packageAndClassName+" ["+methodName+"] return value is : {\"code:\""+result.getCode()+",\"success\":"+result.getSuccess()+",\"message\":\""+result.getMessage()+"\",data.size:"+(resultList==null?0:resultList.size())+"}");
				 }else{
					 logger.info(packageAndClassName+" ["+methodName+"] return value is :"+JsonUtils.objectToJson(result)); 
				 }
			} catch (Exception e) {
				logger.error("com.yundao.workflow.aspect.LoggerPrintManager [pringLog] is error,cause is ",e);
			}
		 }
         logger.info(packageAndClassName+" ["+methodName+"] end."); 
         
    	 return retObject;
    }
 
}
