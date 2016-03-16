/*
* 文 件 名:  TokenHandler.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  token处理
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;

/**
*  token处理， 
* 
* @author  zhouliang
* @version  [1.0, 2015年4月17日]
* @since  [framework/1.0]
*/
public interface TokenService {
    
    /**
     * 生成token
     */
    public String generateToken();
    
    /**
     * 验证token
     * 验证后会删除token
     */
    public boolean validToken(String token);
    
    /**
     * 获取请求的token
     */
    public String fetchToken();
}
