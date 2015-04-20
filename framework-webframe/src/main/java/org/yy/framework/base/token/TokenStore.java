/*
* 文 件 名:  TokenStore.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  Token存储
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;

/**
* Token存储
* 
* @author  zhouliang
* @version  [1.0, 2015年4月17日]
* @since  [webframework/1.0]
*/
public interface TokenStore {
    
    /**
     * 获取token
     */
    public String get(String token);
    
    /**
     * 存储token,默认值为1
     */
    public void put(String token);
    
    /**
     * 删除token
     */
    public void remove(String token);
    
}
