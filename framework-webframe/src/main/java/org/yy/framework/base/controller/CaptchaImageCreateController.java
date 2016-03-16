package org.yy.framework.base.controller;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

/**
 * 
 * 验证码生成控制器， 也可用于非spring mvc模式下，请参考网上文档
 * 
 * 1， 配置
 *      <!-- 配置kaptcha验证码 -->  
    <bean id="captchaProducer" class="com.google.code.kaptcha.impl.DefaultKaptcha">  
        <property name="config">  
            <bean class="com.google.code.kaptcha.util.Config">  
                <constructor-arg type="java.util.Properties">  
                    <props>  
                        <prop key="kaptcha.image.width">100</prop>  
                        <prop key="kaptcha.image.height">50</prop>  
                        <prop key="kaptcha.noise.impl">com.google.code.kaptcha.impl.NoNoise</prop>  
                        <prop key="kaptcha.textproducer.char.string">0123456789abcdefghijklmnopqrstuvwxyz</prop>  
                        <prop key="kaptcha.textproducer.char.length">4</prop>  
                    </props>  
                </constructor-arg>  
            </bean>  
        </property>  
    </bean>  
 * 
 * 2，验证
 * @RequestMapping(value = "check", method = RequestMethod.POST)  
    @ResponseBody  
    public String loginCheck(HttpServletRequest request,  
            @RequestParam(value = "username", required = true) String username,  
            @RequestParam(value = "password", required = true) String password,  
            @RequestParam(value = "kaptcha", required = true) String kaptchaReceived){  
        //用户输入的验证码的值  
        String kaptchaExpected = (String) request.getSession().getAttribute(  
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);  
        //校验验证码是否正确  
        if (kaptchaReceived == null || !kaptchaReceived.equals(kaptchaExpected)) {  
            return "kaptcha_error";//返回验证码错误  
        }  
        //校验用户名密码  
        // ……  
        // ……  
        return "success"; //校验通过返回成功  
    }  
 * 
 * @author zhouliang
 * @version [1.0, 2015年9月28日]
 * @since [web-framewor/1.0]
 */
@Controller
public class CaptchaImageCreateController {
    private Producer captchaProducer = null;
    
    @Autowired
    public void setCaptchaProducer(Producer captchaProducer) {
        this.captchaProducer = captchaProducer;
    }
    
    @RequestMapping("/kaptcha")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.taobao-base
        response.setHeader("Pragma", "no-cache");
        
        // return a jpeg
        response.setContentType("image/jpeg");
        
        // create the text for the image
        String capText = captchaProducer.createText();
        
        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        
        ServletOutputStream out = response.getOutputStream();
        
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        }
        finally {
            out.close();
        }
        return null;
    }
}