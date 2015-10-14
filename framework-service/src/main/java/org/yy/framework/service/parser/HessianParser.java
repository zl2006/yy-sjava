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
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.yy.framework.service.anno.Hessian;
import org.yy.framework.service.util.MapUtils;

/**
 * 解析Hessian服务元注解
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public class HessianParser implements Parser {

	// 客户端地址
	private Map<String, String> remoteUrls;
	
	// 访问协议
	private static final String PROTOCAL = "http://";

	public HessianParser(Map<String, String> remoteUrls) {
		this.remoteUrls = remoteUrls;
	}

	/** {@inheritDoc} */
	@Override
	public String buildServerBeanName( ScannedGenericBeanDefinition beanDefinition) {
		Map<String, Object> annotationAttributes = beanDefinition.getMetadata().getAnnotationAttributes(Hessian.class.getName());
		String uri = (String) annotationAttributes.get("uri");
		return uri;
	}

	/** {@inheritDoc} */
	@Override
	public String buildClientBeanName(ScannedGenericBeanDefinition beanDefinition) {
		AnnotationMetadata metadata = beanDefinition.getMetadata();
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Hessian.class.getName());
		String name = (String) annotationAttributes.get("bean");
		return name;
	}

	/** {@inheritDoc} */
	@Override
	public void parseServerBeanDefine( ScannedGenericBeanDefinition srcBeanDefinition, GenericBeanDefinition targetBeanDefinition) {
		AnnotationMetadata metadata = srcBeanDefinition.getMetadata();
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Hessian.class.getName());
		String beanRef = (String) annotationAttributes.get("bean");

		targetBeanDefinition.getPropertyValues().add("serviceInterface", targetBeanDefinition.getBeanClassName());		//服务接口设置
		targetBeanDefinition.getPropertyValues().add("service", new RuntimeBeanReference(beanRef));						//实际目标服务bean引用
		targetBeanDefinition.setBeanClass(HessianServiceExporter.class);												//服务导出类设置
	}

	/** {@inheritDoc} */
	@Override
	public void parseClientBeanDefine( ScannedGenericBeanDefinition srcBeanDefinition, GenericBeanDefinition targetBeanDefinition) {
		AnnotationMetadata metadata = srcBeanDefinition.getMetadata();
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(Hessian.class.getName());
		
		String app = annotationAttributes.get("app").toString();
		String uri = (String) annotationAttributes.get("uri");
		Boolean overloadEnabled = MapUtils.getBoolean(annotationAttributes, "overloadEnabled", false);
		
		targetBeanDefinition.getPropertyValues().add("serviceUrl", PROTOCAL + remoteUrls.get(app) + uri);			//服务远程调用地址设置
		targetBeanDefinition.getPropertyValues().add("serviceInterface", srcBeanDefinition.getBeanClassName());		//服务接口设置
		targetBeanDefinition.getPropertyValues().add("overloadEnabled", overloadEnabled);							//客户端是否支持重载方法的调用设置，设置为true将支持接口中的重载方法调用，默认为false
		targetBeanDefinition.setBeanClass(HessianProxyFactoryBean.class);											//客户端调用代理类设置

	}

}
