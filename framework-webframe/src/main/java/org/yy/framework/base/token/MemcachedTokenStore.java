/*
* 文 件 名:  MemcachedTokenStore.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  token的memcached存储器
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.rubyeye.xmemcached.MemcachedClient;

/**
* token的memcached存储器，实用于分布式环境
* <bean name="memcachedClient"
                class="net.rubyeye.xmemcached.utils.XMemcachedClientFactoryBean">
                <property name="servers">
                        <value>host1:port1 host2:port2 host3:port3</value>
                </property>
                <property name="weights">
                        <list>
                                <value>1</value>
                                <value>2</value>
                                <value>3</value>
                        </list>
                </property>
                <property name="sessionLocator">
                        <bean class="net.rubyeye.xmemcached.impl.KetamaMemcachedSessionLocator"></bean>
                </property>
                <property name="transcoder">
                        <bean class="net.rubyeye.xmemcached.transcoders.SerializingTranscoder" />
                </property>
                <property name="bufferAllocator">
                        <bean class="net.rubyeye.xmemcached.buffer.SimpleBufferAllocator"></bean>
                </property>
        </bean>
* @author  zhouliang
* @version  [版本号, 2015年4月17日]
* @since  [产品/模块版本]
*/
public class MemcachedTokenStore implements TokenStore {
    
    private static final Logger log = LoggerFactory.getLogger(MemcachedTokenStore.class);
    
    private MemcachedClient client;
    
    //过期时间，秒为单位
    private int expire = 60 * 20;
    
    public MemcachedTokenStore(MemcachedClient client) {
        this.client = client;
    }
    
    /** {@inheritDoc} */
    @Override
    public String get(String token) {
        try {
            return client.get(token);
        }
        catch (Exception e) {
            log.error("get token(" + token + ") error", e);
            return null;
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void put(String token) {
        try {
            client.set(token, expire, token);
        }
        catch (Exception e) {
            log.error("put token(" + token + ") error", e);
        }
    }
    
    /** {@inheritDoc} */
    @Override
    public void remove(String token) {
        try {
            client.delete(token);
        }
        catch (Exception e) {
            log.error("remove token(" + token + ") error", e);
        }
    }
    
    /**
    * @return 返回 expire
    */
    public int getExpire() {
        return expire;
    }
    
    /**
    * @param 对expire进行赋值
    */
    public void setExpire(int expire) {
        this.expire = expire;
    }
    
}
