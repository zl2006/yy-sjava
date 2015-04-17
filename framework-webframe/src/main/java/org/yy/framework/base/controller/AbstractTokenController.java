/*
* 文 件 名:  AbstractTokenController.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  提供token验证
* 修 改 人:  zhouliang
* 修改时间:  2015年4月17日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.yy.framework.base.token.TokenHandler;

/**
* 
* @author  zhouliang
* @version  [版本号, 2015年4月17日]
* @since  [产品/模块版本]
*/
public abstract class AbstractTokenController extends AbstractController {
    
    @Autowired
    protected TokenHandler tokenHandler;
}
