/*
* 文 件 名:  DocumentFilter.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  加载服务化文档资源文件
* 修 改 人:  zhouliang
* 修改时间:  2014年7月24日
* 修改内容:  <修改内容>
*/
package org.yy.framework.document.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
*加载服务化文档资源文件
* 
* @author  zhouliang
* @version  [１.０, 2014年7月24日]
* @since 　 [framework-base/1.0]
*/
public class DocumentFilter implements Filter {
    
    /** {@inheritDoc} */
    @Override
    public void destroy() {
    }
    
    /** {@inheritDoc} */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain fc)
        throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest)req;
        String uri = httpReq.getRequestURI();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(uri);
        
        byte[] buffer = new byte[5120];
        int length = 0;
        while( (length =  is.read(buffer))  > 0){
            resp.getOutputStream().write(buffer, 0, length);
        }
        
        is.close();
        resp.flushBuffer();
    }
    
    /** {@inheritDoc} */
    @Override
    public void init(FilterConfig arg0)
        throws ServletException {
        
    }
    
}
