package com.yundao.workflow.service;


import com.yundao.core.constant.CommonConstant;
import com.yundao.core.constant.HeaderConstant;
import com.yundao.core.dto.HeaderUser;
import com.yundao.core.threadlocal.ThreadLocalUtils;

/**
 * 基础服务实现类
 */
public abstract class AbstractService extends com.yundao.core.service.AbstractService{
    /**
     * 获取请求用户的信息
     *
     * @return 请求用户信息
     */
    protected HeaderUser getHeaderUser() {
        return (HeaderUser) ThreadLocalUtils.getFromRequest(CommonConstant.HEADER_USER);
    }
    protected String getHeaderUserRole() {
        return  (String)ThreadLocalUtils.getFromRequest(HeaderConstant.USER_ROLE);
    }
    
}
