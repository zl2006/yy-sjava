package org.yy.framework.service.hessian;

import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.yy.framework.util.aop.AopTargetUtils;

/**
 *  hessian 接口服务端自动扫描注入<br>
 *  配置实例：<br>
 *  1，配置
 *  <bean class="org.yy.framework.service.hessian.HessianServerScannerConfigurer">
        <property name="basePackage" value="com.**.remote"></property>
        <property name="annotationClass" value="com.hunteron.core.Hessian"></property>
    </bean>
    2，编码
    package com.hunteron.api.capability.remote.v2;
    import java.util.List;
    import com.hunteron.core.Context;
    import com.hunteron.core.Hessian;
    // Context.API_V2 用于客户端调用
    @Hessian(context = Context.API_V2, uri = "/v2/hhService")
    public interface IHhRemoteService {
        public List<String> getList(List<Long> hhs);
    }
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public class HessianServerScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean,
    ApplicationContextAware, BeanNameAware {
    
    protected final Logger logger = LoggerFactory.getLogger(HessianServerScannerConfigurer.class);
    
    private String beanName;
    
    private String basePackage;
    
    private boolean includeAnnotationConfig = true;
    
    private ApplicationContext applicationContext;
    
    // 实现了该接口
    private Class<?> markerInterface;
    
    // 配置了该注解
    private Class<? extends Annotation> annotationClass;
    
    // 存放spring 容器中的有接口的实现类的bean name
    private Map<String, String> implClassContextName = new HashMap<String, String>();
    
    private BeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator() {
        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            AnnotationMetadata metadata = ((ScannedGenericBeanDefinition)definition).getMetadata();
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(annotationClass.getName());
            String uri = (String)annotationAttributes.get("uri");
            return uri;
        }
    };
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
    }
    
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet()
        throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required " + beanName);
        notNull(this.annotationClass, "Property 'annotationClass' is required " + beanName);
        XmlWebApplicationContext xmlContext = (XmlWebApplicationContext)applicationContext;
        BeanFactory parentBeanFactory = xmlContext.getParentBeanFactory();
        xmlContext = parentBeanFactory != null ? (XmlWebApplicationContext)parentBeanFactory : xmlContext;
        
        DefaultListableBeanFactory beanFoctory = (DefaultListableBeanFactory)xmlContext.getAutowireCapableBeanFactory();
        Field findField = ReflectionUtils.findField(beanFoctory.getClass(), "singletonObjects");
        ReflectionUtils.makeAccessible(findField);
        @SuppressWarnings("unchecked")
        Map<String, Object> field = (Map<String, Object>)ReflectionUtils.getField(findField, beanFoctory);
        
        for (Entry<String, Object> entry : field.entrySet()) {
            if (AopUtils.isAopProxy(entry.getValue())) {
                Class<?>[] actualInterfaces = getActualInterfaces(entry.getValue());
                for (Class<?> actualInterface : actualInterfaces) {
                    implClassContextName.put(actualInterface.getName(), entry.getKey());
                }
            }
        }
    }
    
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
        throws BeansException {
        HessianClassPathScanner scan = new HessianClassPathScanner(registry);
        scan.setResourceLoader(this.applicationContext);
        scan.setBeanNameGenerator(this.nameGenerator);
        // 引入注解配置
        scan.setIncludeAnnotationConfig(this.includeAnnotationConfig);
        scan.registerFilters();
        
        String[] basePackages =
            StringUtils.tokenizeToStringArray(this.basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        scan.scan(basePackages);
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public void setIncludeAnnotationConfig(boolean includeAnnotationConfig) {
        this.includeAnnotationConfig = includeAnnotationConfig;
    }
    
    public Class<?> getMarkerInterface() {
        return markerInterface;
    }
    
    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }
    
    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
    
    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }
    
    public BeanNameGenerator getNameGenerator() {
        return nameGenerator;
    }
    
    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }
    
    private Class<?>[] getActualInterfaces(Object obj) {
        try {
            return AopTargetUtils.getInterfaces(obj);
        }
        catch (Exception e) {
            logger.error(obj + " find Actual Interface error", e);
        }
        return new Class[0];
    }
    
    private class HessianClassPathScanner extends ClassPathBeanDefinitionScanner {
        
        public HessianClassPathScanner(BeanDefinitionRegistry registry) {
            super(registry, false);
        }
        
        @Override
        public Set<BeanDefinitionHolder> doScan(String... basePackages) {
            Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
            if (beanDefinitions.isEmpty()) {
                logger.warn("No hessian was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
            }
            else {
                for (BeanDefinitionHolder holder : beanDefinitions) {
                    GenericBeanDefinition definition = (GenericBeanDefinition)holder.getBeanDefinition();
                    
                    if (logger.isDebugEnabled()) {
                        logger.debug("Creating HessianServiceExporter with name '" + holder.getBeanName() + "' and '"
                            + definition.getBeanClassName() + "' serviceInterface");
                    }
                    
                    // the mapper interface is the original class of the bean
                    // but, the actual class of the bean is HessianServiceExporter
                    definition.getPropertyValues().add("serviceInterface", definition.getBeanClassName());
                    String beanNameRef = implClassContextName.get(definition.getBeanClassName());
                    definition.getPropertyValues().add("service", new RuntimeBeanReference(beanNameRef));
                    definition.setBeanClass(HessianServiceExporter.class);
                }
            }
            return beanDefinitions;
            
        }
        
        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
            throws IllegalStateException {
            String implBeanName = implClassContextName.get(beanDefinition.getBeanClassName());
            if (!StringUtils.isEmpty(implBeanName) && super.checkCandidate(beanName, beanDefinition)) {
                return true;
            }
            else {
                logger.warn("Skipping HessianServiceExporter with name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' serviceInterface "
                    + ". Bean already defined with the same name!");
                return false;
            }
        }
        
        public void registerFilters() {
            boolean acceptAllInterfaces = true;
            
            // if specified, use the given annotation and / or marker interface
            if (HessianServerScannerConfigurer.this.annotationClass != null) {
                addIncludeFilter(new AnnotationTypeFilter(HessianServerScannerConfigurer.this.annotationClass));
                acceptAllInterfaces = false;
            }
            
            // override AssignableTypeFilter to ignore matches on the actual marker interface
            if (HessianServerScannerConfigurer.this.markerInterface != null) {
                addIncludeFilter(new AssignableTypeFilter(HessianServerScannerConfigurer.this.markerInterface) {
                    @Override
                    protected boolean matchClassName(String className) {
                        return false;
                    }
                });
                acceptAllInterfaces = false;
            }
            
            if (acceptAllInterfaces) {
                // default include filter that accepts all classes
                addIncludeFilter(new TypeFilter() {
                    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                        throws IOException {
                        return true;
                    }
                });
            }
            
            // exclude package-info.java
            addExcludeFilter(new TypeFilter() {
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
                    throws IOException {
                    String className = metadataReader.getClassMetadata().getClassName();
                    return className.endsWith("package-info");
                }
            });
        }
    }
}