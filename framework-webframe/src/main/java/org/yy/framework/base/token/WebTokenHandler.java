/*
* 文 件 名:  AbstractTokenHandler.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  token处理
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;

import java.util.UUID;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.yy.framework.basedata.Constants;

/**
* 
* @author  zhouliang
* @version  [版本号, 2015年4月17日]
* @since  [产品/模块版本]
*/
public class WebTokenHandler implements TokenHandler {
    
    //token存储器
    private TokenStore store;
    
    public WebTokenHandler(TokenStore store) {
        this.store = store;
    }
    
    /** {@inheritDoc} */
    @Override
    public String generateToken() {
        String s = UUID.randomUUID().toString();
        //使用session每次生成新的token时删除，使用memcached时，只在验证或过期后删除
        store.remove("");
        store.put(s);
        return s;
    }
    
    /** {@inheritDoc} */
    @Override
    public String fetchToken() {
        ServletRequestAttributes srAttrs = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        return srAttrs.getRequest().getParameter(Constants.MVC_TOKEN_KEY);
    }
    
    /** {@inheritDoc} */
    @Override
    public boolean validToken(String token) {
        String s   = store.get(token);
        store.remove(token);
        return token.equals(s);
    }
}
