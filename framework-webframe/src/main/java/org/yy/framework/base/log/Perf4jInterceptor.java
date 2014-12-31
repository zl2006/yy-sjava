/*
* 文 件 名:  Perf4jInterceptor.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  性能日志拦截器
* 修 改 人:  zhouliang
* 修改时间:  2012-9-5
*/

package org.yy.framework.base.log;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.*;
import java.util.*;

/**
 * 
 * 性能日志拦截器
 * 
 * @author  zhouliang
 * @version  [1.0, 2012-9-7]
 * @since  [framework-base/1.0]
 */
public class Perf4jInterceptor implements MethodBeforeAdvice, AfterReturningAdvice {
    
    private Map<String, StopWatch> watches = new HashMap<String, StopWatch>();
    
    public void before(Method method, Object[] args, Object target)
        throws Throwable {
        String completeMethodName = getCompleteMethodName(target, method);
        
        // 创建性能日志记录器
        StopWatch stopWatch;
        if (watches.containsKey(completeMethodName)) {
            stopWatch = watches.get(completeMethodName);
            stopWatch.start();
        }
        else {
            stopWatch = new Slf4JStopWatch(completeMethodName, Arrays.toString(args));
            watches.put(completeMethodName, stopWatch);
        }
        
    }
    
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
        throws Throwable {
        String completeMethodName = getCompleteMethodName(target, method);
        
        // 记录性能
        if (watches.containsKey(completeMethodName)) {
            StopWatch stopWatch = watches.get(completeMethodName);
            stopWatch.stop();
        }
    }
    
    /**
     * 根据目标对象与方法获取方法完整名称.
     * 
     * @param target
     *            目标对象
     * @param method
     *            方法
     * @return 方法完整名称
     */
    private String getCompleteMethodName(Object target, Method method) {
        String className = "";
        if (target != null) {
            className = target.toString();
            int loc = className.indexOf("@");
            if (loc >= 0) {
                className = className.substring(0, loc);
            }
        }
        return className + "." + method.getName();
    }
}
