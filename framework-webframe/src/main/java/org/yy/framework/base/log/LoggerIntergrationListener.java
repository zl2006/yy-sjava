package org.yy.framework.base.log;

import javax.servlet.ServletContextEvent;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.util.Log4jConfigListener;

/** 
 * 扩展Spring的Log4jConfigListener，在容器启动的时候，桥接JDK14的输出到slf4j-logger 
 *  需要配置在web.xml中
 *      <!--Intergration log4j/slf4j/commons-logger/jdk14-logger to log4j -->  
    <listener>  
        <listener-class>com.feinno.framework.common.web.support.LoggerIntergrationListener</listener-class>  
    </listener>  
 * @author zhouliang 
 */
public class LoggerIntergrationListener extends Log4jConfigListener {
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        installJulToSlf4jBridge();
        event.getServletContext().log("Install Jdk-util-logger to slf4j success.");
        super.contextInitialized(event);
    }
    
    private void installJulToSlf4jBridge() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
    
}