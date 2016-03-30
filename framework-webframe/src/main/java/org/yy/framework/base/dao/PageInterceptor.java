/*
* 文 件 名:  PageInterceptor.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  处理mybatis3中的Mapper形式分页
* 修 改 人:  zhouliang
* 修改时间:  2016年3月28日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.dao;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.session.SqlSession;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.beans.DirectFieldAccessor;
import org.yy.framework.base.dao.AbstractMyBatisDao;
import org.yy.framework.basedata.query.AbstractQueryDto;
import org.yy.framework.basedata.query.Pagination;
import org.yy.framework.basedata.query.ResultDto;

/**
* 处理mybatis3中的Mapper形式分页
* 支持的方法返回类型必须是ResultDto, 参数必须是AbstractQueryDto
*    Spring配置
    <bean id="pageInterceptor" class="org.yy.qrcodeseller.dao.product.PageInterceptor"></bean>
    <aop:config proxy-target-class="false">
        <aop:pointcut id="pagePointCut"
            expression="execution(* org.yy.qrcodeseller.dao..*(..))" />
        <aop:advisor advice-ref="pageInterceptor" pointcut-ref="pagePointCut" />
    </aop:config>
     JAVA代码
     public ResultDto<Product> findProductForPage(ProductDto productDto);
     mybatis XML配置
     <select id="findProductForPage" resultMap="RM-PRODUCT" parameterType="TA-PRODUCT-DTO">
        <include refid="SQL_SELECT_PRODUCT" /> WHERE 1 = 1
        <include refid="SQL_WHERE_PRODUCT" />
        <if test="pagination != null">LIMIT #{pagination.startNum}, #{pagination.pageSize}</if>
    </select>
    <select id="findProductForPage_PAGE_COUNT" resultType="java.lang.Integer"
        parameterType="TA-PRODUCT-DTO">
        SELECT COUNT(1) FROM TB_PQ_PRODUCT a WHERE 1 = 1
        <include refid="SQL_WHERE_PRODUCT" />
    </select>
* 
* @author  zhouliang
* @version  [版本号, 2016年3月28日]
* @since  [产品/模块版本]
*/
public class PageInterceptor implements MethodInterceptor {
    
    private static String JDKDYNAMIC_AOPPROXY_CLASSNAME = "org.springframework.aop.framework.JdkDynamicAopProxy";
    
    /** {@inheritDoc} */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object invoke(MethodInvocation methodInvocation)
        throws Throwable {
        
        //1, 通过spring aop:config配置才会使用
        if (!(methodInvocation instanceof ReflectiveMethodInvocation)) {
            return methodInvocation.proceed();
        }
        ReflectiveMethodInvocation reflectMI = (ReflectiveMethodInvocation)methodInvocation;
        
        //2, 判断DAO接口是否为MapperProxy形式实现,也就是mybatis的mapper方式实例
        Object proxy = reflectMI.getProxy();
        MapperProxy<?> mapperProxyTarget = getTarget(proxy);
        if (proxy == null) { //目标对象不为mapper方式时，直接调用dao对象
            return methodInvocation.proceed();
        }
        
        //3, 判断返回类型是否为ResultDto, 参数是否为AbstractQueryDto，来确定是否分页查询 
        Method m = reflectMI.getMethod();
        boolean isPageQuery =
            ResultDto.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes() != null
                && m.getParameterTypes().length > 0
                && AbstractQueryDto.class.isAssignableFrom(m.getParameterTypes()[0]);
        
        if (isPageQuery) {
            ResultDto<?> dto = new ResultDto(); //定义结果对象
            String sqlName = m.getDeclaringClass().getName() + "." + m.getName();
            SqlSession sqlsession = getSession(mapperProxyTarget);
            //查询列表
            List list =
                sqlsession.selectList(m.getDeclaringClass().getName() + "." + m.getName(), reflectMI.getArguments()[0]);
            dto.setResult(list);
            if (reflectMI.getArguments()[0] != null) {
                
                //有分页信息时查询记录总数
                Pagination page = getPage(reflectMI.getArguments()[0]);
                if (page != null) {
                    Integer count =
                        sqlsession.selectOne(sqlName + AbstractMyBatisDao.PAGINATION_SQL_SUFFIX,
                            reflectMI.getArguments()[0]);
                    dto.setPagination(page);
                    dto.getPagination().setTotal(count);
                }
            }
            return dto;
        }
        else {
            return methodInvocation.proceed();
        }
        
    }
    
    //获取最终target目标对象
    private MapperProxy<?> getTarget(Object proxy) {
        if (!(proxy instanceof Proxy)) {
            return null;
        }
        try {
            DirectFieldAccessor targetDFA = new DirectFieldAccessor(proxy);
            Object jdkDynamicAopProxy = targetDFA.getPropertyValue("h");
            if (!JDKDYNAMIC_AOPPROXY_CLASSNAME.equals(jdkDynamicAopProxy.getClass().getName())) {
                return null;
            }
            DirectFieldAccessor jdkDynamicAopProxyDFA = new DirectFieldAccessor(jdkDynamicAopProxy);
            AdvisedSupport ad = (AdvisedSupport)jdkDynamicAopProxyDFA.getPropertyValue("advised");
            
            DirectFieldAccessor mapperProxyDFA = new DirectFieldAccessor(ad.getTargetSource().getTarget());
            Object result = mapperProxyDFA.getPropertyValue("h");
            if (result instanceof MapperProxy) {
                return (MapperProxy<?>)result;
            }
        }
        catch (Exception e) {
            //不用处理错误 
        }
        return null;
    }
    
    //获取sqlsession
    private SqlSession getSession(MapperProxy<?> proxy) {
        DirectFieldAccessor dfa = new DirectFieldAccessor(proxy);
        return (SqlSession)dfa.getPropertyValue("sqlSession");
    }
    
    //获取pagination
    private Pagination getPage(Object queryDto) {
        DirectFieldAccessor param1DFA = new DirectFieldAccessor(queryDto);
        return (Pagination)param1DFA.getPropertyValue("pagination");
    }
    
}
