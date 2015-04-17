/*
* 文 件 名:  TokenTag.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述: 用于生成token的标签
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.token;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.yy.framework.basedata.Constants;

/**
* 用于生成token的标签 , 需要在application中配置TokenHandler
* <jsp-config>
    <taglib>
        <taglib-uri>/mytaglib</taglib-uri>
        <taglib-location>/WEB-INF/yy.tld</taglib-location>
    </taglib>
  </jsp-config>
* @author  zhouliang
* @version  [1.0, 2015年4月17日]
* @since  [webframe/1.0]
*/
public class TokenTag extends TagSupport {
    /**
    * 注释内容
    */
    private static final long serialVersionUID = 6504632749633091307L;
    
    private static final Logger logger = LoggerFactory.getLogger(TokenTag.class);
    
    /** {@inheritDoc} */
    @Override
    public int doStartTag()
        throws JspException {
        
        ApplicationContext ctx =
            WebApplicationContextUtils.getWebApplicationContext(this.pageContext.getServletContext());
        TokenHandler tokenHandler = ctx.getBean(TokenHandler.class);
        JspWriter out = this.pageContext.getOut();
        try {
            out.print("<input value=\"" + tokenHandler.generateToken() + "\"  type=\"hidden\" name=\""
                + Constants.MVC_TOKEN_KEY + "\" />");
        }
        catch (IOException e) {
            logger.error("make token error", e);
        }
        return SKIP_BODY;
    }
    
}
