package com.yundao.workflow.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.stereotype.Component;

import com.yundao.core.threadlocal.filter.ThreadLocalFilter;

/**
 * 本地线程过滤器
 *
 * @author jan
 * @create 2017-06-27 PM5:20
 **/
@Component
@WebFilter(filterName = "tenantThreadLocalFilter", urlPatterns = "/*")
public class TenantThreadLocalFilter extends ThreadLocalFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }
}
