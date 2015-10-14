/*
* 文 件 名:  Parser.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  解析服务元注解
* 修 改 人:  zhouliang
* 修改时间:  2015年10月14日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service.parser;


import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;

/**
* 解析服务元注解
* 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 * 
 */
public interface Parser {
    
    /**
     * 生成服务端Bean名称
     */
    public String buildServerBeanName(ScannedGenericBeanDefinition beanDefinition);
    
    /**
     * 生成客户端Bean名称
     */
    public String buildClientBeanName(ScannedGenericBeanDefinition beanDefinition);
    
    /**
     * 解析服务端BeanDefinition
     */
    public void parseServerBeanDefine(ScannedGenericBeanDefinition srcBeanDefinition,
        GenericBeanDefinition targetBeanDefinition);
    
    /**
     * 解析客户端BeanDefinition
     */
    public void parseClientBeanDefine(ScannedGenericBeanDefinition srcBeanDefinition,
        GenericBeanDefinition targetBeanDefinition);
}
