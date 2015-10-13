package org.yy.framework.service.hessian;

import static org.springframework.util.Assert.notNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.util.StringUtils;

/**
 *  hessian 接口服务端自动扫描注入生成BeanDefinition， 通过BeanDefinitionRegistryPostProcessor实现<br>
 *  配置实例：<br>
 *  1，配置扫描
     第一扫描实现类
    <context:component-scan base-package="org.yy.framework.service.hessian"></context:component-scan>
    第二扫描hessian服务
    <bean class="org.yy.framework.service.hessian.HessianServerScannerConfigurer">
        <property name="basePackage" value="org.yy.framework.service.hessian"></property>
    </bean>
    2，编码
    接口编码
    @Hessian(app = "test_app", uri = "/demo/testServer", bean = "testServer", overloadEnabled = false)
    public interface TestServer {
        public void sayHello();
    }
    实现编码
    @Service("testServer")
    public class TestServerImpl implements TestServer {
        @Override
        public void sayHello() {
            System.out.println("hello world");
        }
    }
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public class HessianServerScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean,
    ApplicationContextAware {
    
    protected final Logger logger = LoggerFactory.getLogger(HessianServerScannerConfigurer.class);
    
    //Hessian的配置注解
    private final Class<? extends Annotation> hessianAnnoClass = Hessian.class;
    
    //扫描hessian服务的包，多个包时使用“，;”分隔
    private String basePackage;
    
    //扫描类时支持元注解配置
    private boolean includeAnnotationConfig = true;
    
    private ApplicationContext applicationContext;
    
    //判断是否配置扫描包    
    @Override
    public void afterPropertiesSet()
        throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required ");
    }
    
    //BeanDefinition后置处理
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
        throws BeansException {
        
        //Step 1， Hessian扫描器初始化
        HessianClassPathScanner scan = new HessianClassPathScanner(registry);
        scan.setResourceLoader(this.applicationContext);
        scan.setBeanNameGenerator(this.nameGenerator);
        scan.setIncludeAnnotationConfig(this.includeAnnotationConfig);
        String[] basePackages =
            StringUtils.tokenizeToStringArray(this.basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        //Step 2， Hessian扫描，通过模板方法调用doScan实现
        scan.scan(basePackages);
    }
    
    private class HessianClassPathScanner extends ClassPathBeanDefinitionScanner {
        
        public HessianClassPathScanner(BeanDefinitionRegistry registry) {
            super(registry, false); //false表示不使用默认的元注解（例如：Controller, Service等）
            addIncludeFilter(new AnnotationTypeFilter(HessianServerScannerConfigurer.this.hessianAnnoClass)); // 只对Hessian元注解标识的类生成BeanDefine
        }
        
        @Override
        public Set<BeanDefinitionHolder> doScan(String... basePackages) {
            //Step 一， 获取Hessian注解标识的BeanDefine
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
                    
                    //Step 二， 重新定义通过 Hessain元注解描述的BeanDefine。主要是定义HessianServiceExporter的BeanDefine
                    AnnotationMetadata metadata = ((ScannedGenericBeanDefinition)definition).getMetadata();
                    Map<String, Object> annotationAttributes =
                        metadata.getAnnotationAttributes(hessianAnnoClass.getName());
                    String beanRef = (String)annotationAttributes.get("bean");
                    
                    definition.getPropertyValues().add("serviceInterface", definition.getBeanClassName());
                    definition.getPropertyValues().add("service", new RuntimeBeanReference(beanRef));
                    definition.setBeanClass(HessianServiceExporter.class);
                }
            }
            return beanDefinitions;
            
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
            if (!applicationContext.containsBean(beanName) && super.checkCandidate(beanName, beanDefinition)) {
                return true;
            }
            else {
                logger.warn("Skipping HessianServiceExporter with name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' serviceInterface "
                    + ". Bean already defined with the same name!");
                return false;
            }
        }
        
    }
    
    //根据Hessian的uri生成beanname
    private BeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator() {
        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            AnnotationMetadata metadata = ((ScannedGenericBeanDefinition)definition).getMetadata();
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(hessianAnnoClass.getName());
            String uri = (String)annotationAttributes.get("uri");
            return uri;
        }
    };
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
}