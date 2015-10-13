package org.yy.framework.service.hessian;

import static org.springframework.util.Assert.notNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.util.StringUtils;
import org.yy.framework.service.util.MapUtils;

/**
 *  hessian 接口客户端自动扫描注入<br>
 *  配置：
 *  1，
 *  <bean class="com.xxx.HessianClientScannerConfigurer">
    <property name="basePackage" value="org.yy.demo.**.service"></property>
    <property name="locations">
        <list>
            <value>classpath:hessianurl.properties</value>
        </list>
    </property>
</bean>
    2，url properties配置文件 hessianurl.properties    
           api.v2.remote.url=http://localhost:8004/remote
    3，客户端代码
         @Controller
        @RequestMapping("v2/hh")
        public class HhController  {
         
            @Autowired
            private TestServer testServer;
         
            @ResponseBody
            @RequestMapping(value = "getList", method = RequestMethod.GET)
            public ResponseEntity getList(
                    HttpServletRequest request, 
                    HttpServletResponse response,
                    @RequestParam(value = "ids", required=true) List<Long> ids
                    ) {
                hhService.getList(ids);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
 * 
 * @author zhouliang
 * @version [1.0, 2015年10月11日]
 * @since [framework-service/1.0]
 */
public class HessianClientScannerConfigurer implements BeanDefinitionRegistryPostProcessor, InitializingBean,
    ApplicationContextAware {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    //Hessian的配置注解
    private final Class<? extends Annotation> hessianAnnoClass = Hessian.class;
    
    //扫描hessian服务的包，多个包时使用“，;”分隔
    private String basePackage;
    
    //扫描类时支持元注解配置
    private boolean includeAnnotationConfig = true;
    
    //访问hessian服务的远程地址
    private Map<String, String> remoteUrls = new HashMap<String, String>();
    
    private ApplicationContext applicationContext;
    
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
            addIncludeFilter(new AnnotationTypeFilter(HessianClientScannerConfigurer.this.hessianAnnoClass)); // 只对Hessian元注解标识的类生成BeanDefine
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
                        logger.debug("Creating HessianFactoryBean with name '" + holder.getBeanName() + "' and '"
                            + definition.getBeanClassName() + "' hessianInterface");
                    }
                    
                    //Step 二， 重新定义通过 Hessain元注解描述的BeanDefine。主要是定义HessianServiceExporter的BeanDefine
                    AnnotationMetadata metadata = ((ScannedGenericBeanDefinition)definition).getMetadata();
                    Map<String, Object> annotationAttributes =
                        metadata.getAnnotationAttributes(hessianAnnoClass.getName());
                    String app = annotationAttributes.get("app").toString();
                    String uri = (String)annotationAttributes.get("uri");
                    Boolean overloadEnabled = MapUtils.getBoolean(annotationAttributes, "overloadEnabled", false);
                    definition.getPropertyValues().add("serviceUrl", remoteUrls.get(app) + uri);
                    definition.getPropertyValues().add("serviceInterface", definition.getBeanClassName());
                    //                  新增overloadEnabled属性，并把它的值设置为true，默认是false，则Hessian就能支持方法的重载了
                    definition.getPropertyValues().add("overloadEnabled", overloadEnabled);
                    definition.setBeanClass(HessianProxyFactoryBean.class);
                }
            }
            return beanDefinitions;
            
        }
        
        //判断beanDefine是否为接口
        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
        }
        
        /**
         * {@inheritDoc}
         */
        //判断beanDefine是否重名
        @Override
        protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition)
            throws IllegalStateException {
            if (!applicationContext.containsBean(beanName) && super.checkCandidate(beanName, beanDefinition)) {
                return true;
            }
            else {
                logger.warn("Skipping HessianFactoryBean with name '" + beanName + "' and '"
                    + beanDefinition.getBeanClassName() + "' hessianInterface"
                    + ". Bean already defined with the same name!");
                return false;
            }
        }
        
    }
    
    //根据Hessian的bean生成beanname
    private BeanNameGenerator nameGenerator = new AnnotationBeanNameGenerator() {
        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            AnnotationMetadata metadata = ((ScannedGenericBeanDefinition)definition).getMetadata();
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(hessianAnnoClass.getName());
            String name = (String)annotationAttributes.get("bean");
            return name;
        }
    };
    
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
        throws BeansException {
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @Override
    public void afterPropertiesSet()
        throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required !");
    }
    
    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
    
    public void setRemoteUrls(Map<String, String> remoteUrls) {
        this.remoteUrls = remoteUrls;
    }
    
}