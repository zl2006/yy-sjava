/*
 * 文 件 名:  AbstractSpringTest.java
 * 版    权:  Tydic Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
 * 描    述:  单元测试基类
 * 修 改 人:  agan
 * 修改时间:  2012-9-4
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package org.yy.framework.base.test;

import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:spring/test-applicationContext*.xml" })
//public class AbstractSpringTest extends AbstractTransactionalJUnit4SpringContextTests {
public class AbstractSpringTest implements ApplicationContextAware{

    protected ApplicationContext applicationContext;
    
     /** {@inheritDoc} */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

}
