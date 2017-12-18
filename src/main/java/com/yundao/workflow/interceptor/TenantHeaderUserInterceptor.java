package com.yundao.workflow.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;

import com.yundao.core.constant.CommonConstant;
import com.yundao.core.constant.HeaderConstant;
import com.yundao.core.interceptor.spring.AbstractSpringLoginInterceptor;
import com.yundao.core.log.Log;
import com.yundao.core.log.LogFactory;
import com.yundao.core.utils.BooleanUtils;
import com.yundao.core.utils.EDUtils;
import com.yundao.core.dto.HeaderUser;

/**
 * 从头部获取、设置用户信息拦截器
 *
 * @author jan
 * @create 2017-06-22 PM2:23
 **/
public class TenantHeaderUserInterceptor extends AbstractSpringLoginInterceptor {

    private static Log log = LogFactory.getLog(TenantHeaderUserInterceptor.class);

    /**
     * 获取用户登录后的会话
     *
     * @param request servlet请求
     * @return 请求用户信息
     */
    @Override
    public Object getSession(HttpServletRequest request) {
        // 获取用户的登录信息
        HeaderUser result = null;
        String userId = request.getHeader(HeaderConstant.HEADER_USER_ID);
        String realName = EDUtils.decode(request.getHeader(HeaderConstant.HEADER_REAL_NAME));
        String tenantId = request.getHeader(HeaderConstant.HEADER_TENANT_ID);
        //String userRole = request.getHeader(HeaderConstant.USER_ROLE);
        if (!BooleanUtils.isBlank(userId) && !BooleanUtils.isBlank(tenantId)) {
        	result=new HeaderUser();
            result.setUserId(NumberUtils.toLong(userId));
            result.setRealName(realName);
            result.setTenantId(NumberUtils.toLong(tenantId));
            result.setTenantId(NumberUtils.toLong(tenantId));
            request.setAttribute(CommonConstant.HEADER_USER, result);
            //request.setAttribute(HeaderConstant.USER_ROLE, userRole);
        }
        log.info("从头部获取用户的信息headerUserId=" + userId + ",headerRealName=" + realName + ",headerTenantId=" + tenantId);
        return result;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.printf("");
    }

}
