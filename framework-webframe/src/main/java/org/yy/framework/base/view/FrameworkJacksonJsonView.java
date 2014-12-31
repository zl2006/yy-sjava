/*
* 文 件 名:  FrameworkMappingJacksonJsonView.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  约定格式的JSON处理
* 修 改 人:  zhouliang
* 修改时间:  2013年11月11日
*/
package org.yy.framework.base.view;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.yy.framework.basedata.Constants;

/**
* 约定格式的JSON处理
* 
* @author  zhouliang
* @version  [1.0, 2013年11月11日]
* @since  [framework-base/1.0]
*/
public class FrameworkJacksonJsonView extends MappingJackson2JsonView {
    
    /**
     * model, // 请求结果对应的key,  flag:结果标志, code: 结果编码, message:结果消息, data:结果数据, errors:出错时的名细, params:请求参数
     */
    /** {@inheritDoc} */
    @Override
    protected Object filterModel(Map<String, Object> model) {
        Map<String, Object> result = new HashMap<String, Object>(model.size());
        for (String item : Constants.RESULT_NAMES) {
            result.put(item, model.get(item));
        }
        return result;
    }
}
