/*
 * 文 件 名:  AbstractController.java
 * 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
 * 描    述:  Spring的基础控制器
 * 修 改 人:  zhouliang
 * 修改时间:  2013年11月11日
 */
package org.yy.framework.base.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import static org.yy.framework.basedata.Constants.*;

/**
 * Spring的基础控制器
 * 
 * @author zhouliang
 * @version [1.0, 2013年11月11日]
 * @since [web-framework/1.0]
 */
public abstract class AbstractController {
    
    /**
     * 模块名称，一般为页面路径，如：/moduleName/add.jsp    , /useradmin/add.jsp
     */
    protected String moduleName;
    
    protected abstract void setModuleName();
    
    public AbstractController() {
        setModuleName();
    }
    
    /**Constants
     * 处理返回结果
     * 
     * @param flag
     *            结果标志
     * @param code
     *            错误编码
     * @param view
     *            视图
     * @param result
     *            结果
     * @param params
     *            请求参数
     * @param message
     *            描述
     * @param errors
     *            错误消息, 可以字符串或对象
     */
    private ModelAndView processResult(String flag, String code, String view, Object result, Object params,
        String message, Object errors) {
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject(RESULT_FLAG_NAME, flag);
        modelAndView.addObject(RESULT_CODE_NAME, code);
        modelAndView.addObject(RESULT_DATA_NAME, result);
        modelAndView.addObject(RESULT_PARAM_NAME, params);
        modelAndView.addObject(RESULT_MESSAGE_NAME, message);
        modelAndView.addObject(RESULT_ERRORS_NAME, errors);
        return modelAndView;
    }
    
    /**
     * 处理返回结果
     * 
     * @param view
     *            视图
     * @param result
     *            结果
     */
    protected ModelAndView processSuccess(String view, Object result) {
        return processSuccess(view, result, "");
    }
    
    /**
     * 处理返回结果
     * 
     * @param view
     *            视图
     * @param result
     *            结果
     * @param params
     *            参数
     */
    protected ModelAndView processSuccess(String view, Object result, Object params) {
        return processResult(SUCCESS_FLAG, "", view, result, params, "", "");
    }
    
    /**
     * 处理返回结果
     * 
     * @param code
     *            状态码
     * @param view
     *            视图
     */
    protected ModelAndView processFailure(String code, String view) {
        return processFailure(code, view, "", "", "");
    }
    
    /**
     * 处理返回结果
     * 
     * @param code
     *            状态码
     * @param view
     *            视图
     * @param message
     *            描述
     */
    protected ModelAndView processFailure(String code, String view, Object params) {
        return processFailure(code, view, params, "", "");
    }
    
    /**
     * 处理返回结果
     * 
     * @param code
     *            状态码
     * @param view
     *            视图
     * @param params
     *            请求参数
     * @param message
     *            描述
     */
    protected ModelAndView processFailure(String code, String view, Object params, String message) {
        return processFailure(code, view, params, message, "");
    }
    
    /**
     * 处理返回结果
     * 
     * @param code
     *            状态码
     * @param view
     *            视图
     * @param params
     *            请求参数
     * @param message
     *            描述
     * @param errors
     *            错误
     */
    protected ModelAndView processFailure(String code, String view, Object params, String message, Object errors) {
        return processResult(FAILURE_FLAG, code, view, "", params, message, errors);
    }
    
    /**
     * 处理错误, 格式为（字段：错误消息Map）
     */
    protected Map<String, String> convertErrorMap(BindingResult result) {
        Map<String, String> fieldErrors = new HashMap<String, String>();
        if (!result.hasErrors()) {
            return fieldErrors;
        }
        for (FieldError error : result.getFieldErrors()) {
            if (fieldErrors.get(error.getField()) == null) {
                fieldErrors.put(error.getField(), error.getDefaultMessage());
            }
            else {
                fieldErrors.put(error.getField(), fieldErrors.get(error.getField()) + ";" + error.getDefaultMessage());
            }
        }
        return fieldErrors;
    }
    
    /**
     * 处理错误, 格式为(错误消息String)
     */
    protected String convertErrorString(BindingResult result) {
        if (!result.hasErrors()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (FieldError error : result.getFieldErrors()) {
            sb.append(error.getDefaultMessage());
            sb.append(";");
        }
        return sb.toString();
    }
    
    /**
     * 处理错误, 格式为(错误消息String)
     */
    protected String convertErrorString(Map<String, Object> errors) {
        StringBuilder sb = new StringBuilder();
        Set<String> keys = errors.keySet();
        for (String key : keys) {
            sb.append(errors.get(key));
            sb.append(";");
        }
        return sb.toString();
    }
    
    /**
     * 获取请求来源
     * @param request
     * @return
     */
    protected String source(HttpServletRequest request) {
        return request.getParameter(REQ_FROM_SOURCE);
    }
    
    /**
     * 校验验证码
     */
    public boolean checkKaptcha(HttpServletRequest request, String kaptchaReceived) {
        //用户输入的验证码的值  
        String kaptchaExpected =
            (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //校验验证码是否正确  
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {
            return false;//返回验证码错误  
        }
        return true;
    }
    
    /**
     * 处理Spring中的标准异常, DefaultHandlerExceptionResolver
     */
    @ExceptionHandler
    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex)
        throws IOException {
        
        String code = ERROR_500_CODE;
        String view = ERROR_500_PAGE;
        String message = "系统错误";
        String errors = "";
        
        if (ex instanceof NoSuchRequestHandlingMethodException) {
            code = "404";
            message = "NoSuchRequestHandlingMethodException";
        }
        else if (ex instanceof HttpRequestMethodNotSupportedException) {
            code = "405";
            message = "HttpRequestMethodNotSupportedException";
        }
        else if (ex instanceof HttpMediaTypeNotSupportedException) {
            code = "415";
            message = "HttpMediaTypeNotSupportedException";
        }
        else if (ex instanceof HttpMediaTypeNotAcceptableException) {
            code = "406";
            message = "HttpMediaTypeNotAcceptableException";
        }
        else if (ex instanceof MissingServletRequestParameterException) {
            code = "400";
            message = "MissingServletRequestParameterException";
        }
        else if (ex instanceof ServletRequestBindingException) {
            code = "400";
            message = "ServletRequestBindingException";
        }
        else if (ex instanceof ConversionNotSupportedException) {
            code = "500";
            message = "ConversionNotSupportedException";
        }
        else if (ex instanceof TypeMismatchException) {
            code = "400";
            message = "TypeMismatchException";
        }
        else if (ex instanceof HttpMessageNotReadableException) {
            code = "400";
            message = "HttpMessageNotReadableException";
        }
        else if (ex instanceof HttpMessageNotWritableException) {
            code = "500";
            message = "HttpMessageNotWritableException";
        }
        else if (ex instanceof MethodArgumentNotValidException) {
            code = "400";
            message = "MethodArgumentNotValidException";
        }
        else if (ex instanceof MissingServletRequestPartException) {
            code = "400";
            message = "MissingServletRequestPartException";
        }
        else if (ex instanceof BindException) {
            code = "400";
            message = "BindException";
            message = " bind exception";
        }
        else if (ex instanceof Throwable) {
            message = ex.getMessage();
        }
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        errors = sw.toString();
        
        String source = source(request);
        if (PC_SOURCE.equals(source)) {
            view = ERROR_500_PAGE;
        }
        else if (M_SOURCE.equals(source)) {
            view = M_ERROR_500_PAGE;
        }
        
        return processFailure(code, view, "", message, errors);
    }
}
