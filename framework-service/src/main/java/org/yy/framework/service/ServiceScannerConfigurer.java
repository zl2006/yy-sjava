/*
 * 文 件 名:  ServiceScanner.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  扫描服务元注解标识的接口，并生成Spring的BeanDefinition（主要为服务导入类定义）
 * 修 改 人:  zhouliang
 * 修改时间:  2015年10月14日
 * 修改内容:  <修改内容>
 */
package org.yy.framework.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 扫描服务元注解标识的接口，并生成Spring的BeanDefinition
 * 
 * rmi开头的属性只供RMI服务使用
 * hessian开头的属性只供HESSIAN服务使用
 * 
 * 其它属性通用
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public class ServiceScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    
    public static final String SERVER_WHERE = "server";
    
    public static final String CLIENT_WHERE = "client";
    
    protected static final Logger logger = LoggerFactory.getLogger(ServiceScannerConfigurer.class);
    
    // 扫描服务包，多个包时使用“，;”分隔
    private String basePackage;
    
    // 扫描类时，BeanDefinition是否包括元注解配置信息
    private boolean includeAnnotationConfig = true;
    
    // server表示服务端，client表示客户端
    private String where = "server";
    
    // 服务版本,会做为服务uri的一部分http://localhost:8080/hessian/testHessianServer/1
    private String version = "1";
    
    // 访问服务的远程地址
    private Map<String, String> remoteUrls = new HashMap<String, String>();
    
    private ApplicationContext applicationContext;
    
    /** {@inheritDoc} */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
        throws BeansException {
        if (basePackage == null || "".equals(basePackage.trim())) {
            logger.warn("ServiceScanner中没有指定扫描包！");
            return;
        }
        // Step 1， 扫描器初始化
        ServiceClassPathBeanDefinitionScanner scan = new ServiceClassPathBeanDefinitionScanner(registry, this);
        scan.setResourceLoader(this.applicationContext);
        scan.setIncludeAnnotationConfig(this.includeAnnotationConfig);
        String[] basePackages =
            StringUtils.tokenizeToStringArray(this.basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        
        // Step 2， 扫描包，通过模板方法调用doScan实现
        scan.scan(basePackages);
    }
    
    /** {@inheritDoc} */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    /** {@inheritDoc} */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
    }
    
    public void setWhere(String where) {
        this.where = where;
    }
    
    public void setRemoteUrls(Map<String, String> remoteUrls) {
        this.remoteUrls = remoteUrls;
    }
    
    public String getWhere() {
        return where;
    }
    
    public Map<String, String> getRemoteUrls() {
        return remoteUrls;
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
}
