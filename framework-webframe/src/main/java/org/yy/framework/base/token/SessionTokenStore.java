/*
* 文 件 名:  SessionTokenStore.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  token的session存储器
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yy.framework.basedata.Constants;

/**
*  token的session存储器, 注意在分布式环境下session的使用
* 
* @author  zhouliang
* @version  [1.0, 2015年4月17日]
* @since  [webframe/1.0]
*/
public class SessionTokenStore implements TokenStore {
    
    /** {@inheritDoc} */
    @Override
    public String get(String token) {
        return (String)getSession().getAttribute(Constants.MVC_TOKEN_KEY);
    }
    
    /** {@inheritDoc} */
    @Override
    public void put(String token) {
        getSession().setAttribute(Constants.MVC_TOKEN_KEY, token);
    }
    
    /** {@inheritDoc} */
    @Override
    public void remove(String token) {
        getSession().removeAttribute(Constants.MVC_TOKEN_KEY);
    }
    
    protected HttpSession getSession() {
        ServletRequestAttributes srAttrs = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return srAttrs.getRequest().getSession();
    }
    
}
