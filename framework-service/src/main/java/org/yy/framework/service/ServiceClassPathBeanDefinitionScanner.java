/*
* 文 件 名:  ServiceClassPathScanner.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  扫描服务元注解标识的类，并形成初步定义BeanDefinition
* 修 改 人:  zhouliang
* 修改时间:  2015年10月14日
* 修改内容:  <修改内容>
*/
package org.yy.framework.service;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.yy.framework.service.anno.Hessian;
import org.yy.framework.service.parser.HessianParser;
import org.yy.framework.service.parser.Parser;

/**
* 扫描服务元注解标识的类，并形成初步定义BeanDefinition
* 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
*/
public class ServiceClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
    
    //定义处理的服务元注解
    private static List<Class<? extends Annotation>> annoClasses = new ArrayList<Class<? extends Annotation>>();
    
    //服务元注解解析器
    private static Map<String, Parser> parsers = new HashMap<String, Parser>();
    
    //扫注解配置
    private ServiceScannerConfigurer scannerConfigurer;
    
    public ServiceClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry,
        ServiceScannerConfigurer scannerConfigurer) {
        super(registry, false);
        this.scannerConfigurer = scannerConfigurer;
        this.initIncludeFilter();
        this.initParers();
    }
    
    /** {@inheritDoc} */
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        
        Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<BeanDefinitionHolder>();
        for (String basePackage : basePackages) {
            //Step 一， 获取Hessian注解标识的BeanDefinition，未注册到spring容器
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            
            //Step 二，生成指定Service的BeanDefinition
            for (BeanDefinition temp : candidates) {
                
                ScannedGenericBeanDefinition srcBeanDefinition = (ScannedGenericBeanDefinition)temp;
                
                //Step ：遍历所有的元注解，每个注解生成一个BeanDefinition
                for (String annotationType : annotationTypes(srcBeanDefinition)) {
                    
                    if (ServiceScannerConfigurer.SERVER_WHERE.equals(scannerConfigurer.getWhere())) {
                        processServerBeanDefinition(annotationType, srcBeanDefinition, beanDefinitions);
                    }
                    else {
                        processClientBeanDefinition();
                    }
                }
                beanDefinitions.remove(srcBeanDefinition);
            }
        }
        
        return beanDefinitions;
    }
    
    //处理服务端的注解定义
    protected void processServerBeanDefinition(String annotationType, ScannedGenericBeanDefinition srcBeanDefinition,
        Set<BeanDefinitionHolder> beanDefinitions) {
        Parser parser = parsers.get(annotationType);
        
        GenericBeanDefinition targetBeanDefinition = new GenericBeanDefinition(srcBeanDefinition);
        targetBeanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON); //统一单例
        String beanName = parser.buildServerBeanName(srcBeanDefinition); //指定Bean名称
        
        if (targetBeanDefinition instanceof AbstractBeanDefinition) {
            postProcessBeanDefinition((AbstractBeanDefinition)targetBeanDefinition, beanName);
        }
        if (targetBeanDefinition instanceof AnnotatedBeanDefinition) {
            AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition)targetBeanDefinition);
        }
        
        //解析并注册BeanDefinition
        if (checkCandidate(beanName, targetBeanDefinition)) {
            BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(targetBeanDefinition, beanName);
            beanDefinitions.add(definitionHolder);
            parser.parseServerBeanDefine(srcBeanDefinition, targetBeanDefinition);
            registerBeanDefinition(definitionHolder, this.getRegistry()); //注册
        }
    }
    
    //处理客户端的注解定义
    protected void processClientBeanDefinition(String annotationType, ScannedGenericBeanDefinition srcBeanDefinition,
        Set<BeanDefinitionHolder> beanDefinitions) {
        Parser parser = parsers.get(annotationType);
        GenericBeanDefinition targetBeanDefinition = new GenericBeanDefinition(srcBeanDefinition);
        targetBeanDefinition.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON); //统一单例
        String beanName = parser.buildClientBeanName(srcBeanDefinition); //指定Bean名称
    }
    
    //判断beanDefine是否为接口
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
    }
    
    //判断beanDefine是否重名
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
        throws IllegalStateException {
        if (!this.getRegistry().containsBeanDefinition(beanName) && super.checkCandidate(beanName, beanDefinition)) {
            return true;
        }
        else {
            logger.warn("Skipping HessianServiceExporter with name '" + beanName + "' and '"
                + beanDefinition.getBeanClassName() + "' serviceInterface "
                + ". Bean already defined with the same name!");
            return false;
        }
    }
    
    //初始化要使用的注解
    static {
        annoClasses.add(Hessian.class);
    }
    
    /**
     * 只对服务元注解标识的类生成BeanDefine
     */
    protected void initIncludeFilter() {
        for (int i = 0; i < annoClasses.size(); ++i) {
            addIncludeFilter(new AnnotationTypeFilter(annoClasses.get(i)));
        }
    }
    
    /**
     * 只对服务元注解标识的类生成BeanDefine
     */
    protected void initParers() {
        parsers.put(Hessian.class.getName(), new HessianParser());
    }
    
    /**
     * 获取所有的元注解类型
     */
    protected Set<String> annotationTypes(ScannedGenericBeanDefinition defintion) {
        return defintion.getMetadata().getAnnotationTypes();
    }
    
}
