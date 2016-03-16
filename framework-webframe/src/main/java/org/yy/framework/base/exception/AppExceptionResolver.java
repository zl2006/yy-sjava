/*
 * 文 件 名:  AppExceptionResolver.java
 * 版    权:  yy Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  捕获所有的业务异常
 * 修 改 人:  zhouliang
 * 修改时间:  2013年11月12日
 */
package org.yy.framework.base.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.yy.framework.base.controller.AbstractController;
import org.yy.framework.basedata.Constants;
import org.yy.framework.basedata.exception.ControllerException;
import org.yy.framework.basedata.exception.DaoException;
import org.yy.framework.basedata.exception.ServiceException;

/**
 * 捕获Service, Dao, Controller等层的异常,不能捕获由spring框架抛出的标准异常
 * 
 * @author zhouliang
 * @version [1.0, 2013年11月12日]
 * @since [framework/1.0]
 */
public class AppExceptionResolver extends AbstractController implements HandlerExceptionResolver {
    
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionResolver.class);
    
    /** {@inheritDoc} */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception exception) {
        
        //Step 1:设置默认错误数据
        String code = Constants.ERROR_500_CODE;
        String message = "系统错误";
        String view = Constants.ERROR_500_PAGE;
        String errors = "系统错误";
        
        //Step 2:根据来源显示不同错误页面
        String source = source(request);
        if (Constants.PC_SOURCE.equals(source)) {
            view = Constants.ERROR_500_PAGE;
        }
        else if (Constants.M_SOURCE.equals(source)) {
            view = Constants.M_ERROR_500_PAGE;
        }
        
        //Step 3:如果为ERROR、WARN、INFO级别时，具体错误消息及错误堆信息不反馈到前台页面
        if (logger.isErrorEnabled() || logger.isWarnEnabled() || logger.isInfoEnabled()) {
            return processFailure(code, view, "", message, errors);
        }
        
        if (exception instanceof ServiceException) {
            ServiceException newName = (ServiceException)exception;
            code = newName.getCode();
            message = newName.getMessage();
        }
        else if (exception instanceof DaoException) {
            DaoException newName = (DaoException)exception;
            code = newName.getCode();
            message = newName.getMessage();
        }
        else if (exception instanceof ControllerException) {
            ControllerException newName = (ControllerException)exception;
            code = newName.getCode();
            message = newName.getMessage();
        }
        else {
            message = exception.getMessage();
        }
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        errors = sw.toString();
        
        return processFailure(code, view, "", message, errors);
    }
    
    /** {@inheritDoc} */
    @Override
    protected void setModuleName() {
    }
}
