/*
* 文 件 名:  HessianParser.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  解析Hessian服务元注解
* 修 改 人:  zhouliang
* 修改时间:  2015年10月14日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service.parser;

import java.util.Map;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.yy.framework.service.anno.Hessian;

/**
* 解析Hessian服务元注解
* 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
*/
public class HessianParser implements Parser {
    
    /** {@inheritDoc} */
    @Override
    public String buildServerBeanName(ScannedGenericBeanDefinition beanDefinition) {
        Map<String, Object> annotationAttributes =
            beanDefinition.getMetadata().getAnnotationAttributes(Hessian.class.getName());
        String uri = (String)annotationAttributes.get("uri");
        return uri;
    }
    
    /** {@inheritDoc} */
    @Override
    public String buildClientBeanName(ScannedGenericBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Hessian.class.getName());
        String name = (String)annotationAttributes.get("bean");
        return name;
    }
    
    /** {@inheritDoc} */
    @Override
    public void parseServerBeanDefine(ScannedGenericBeanDefinition srcBeanDefinition,
        GenericBeanDefinition targetBeanDefinition) {
        AnnotationMetadata metadata = srcBeanDefinition.getMetadata();
        Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Hessian.class.getName());
        String beanRef = (String)annotationAttributes.get("bean");
        
        targetBeanDefinition.getPropertyValues().add("serviceInterface", targetBeanDefinition.getBeanClassName());
        targetBeanDefinition.getPropertyValues().add("service", new RuntimeBeanReference(beanRef));
        targetBeanDefinition.setBeanClass(HessianServiceExporter.class);
    }
    
    /** {@inheritDoc} */
    @Override
    public void parseClientBeanDefine(ScannedGenericBeanDefinition srcBeanDefinition,
        GenericBeanDefinition targetBeanDefinition) {
        // TODO Auto-generated method stub
        
    }
    
}
