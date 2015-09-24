/*
* 文 件 名:  AbstractCache.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  <描述>
* 修 改 人:  zhouliang
* 修改时间:  2015年9月24日
* 修改内容:  <修改内容>
*/
package org.yy.framework.cache;

import org.apache.commons.codec.binary.Base64;

/**
* 抽象缓存类
* 
* @author  zhouliang
* @version [1.0, 2015年9月24日]
* @since [framework-cache/1.0]
*/
public abstract class AbstractCache {
    
    /**
     * 缓存的别名
     */
    private String name;
    
    /**
     * 缓存过期时间，默认是1小时 自定义的属性
     */
    private int exp = 3600;
    
    /**
     * 前缀名
     */
    private String prefix = "";
    
    /**
     * 是否对key进行base64加密
     */
    private boolean base64Key = false;
    
    /**
    * @return 返回 name
    */
    public String getName() {
        return name;
    }
    
    /**
    * @param 对name进行赋值
    */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
    * @return 返回 exp
    */
    public int getExp() {
        return exp;
    }
    
    /**
    * @param 对exp进行赋值
    */
    public void setExp(int exp) {
        this.exp = exp;
    }
    
    /**
    * @return 返回 prefix
    */
    public String getPrefix() {
        return prefix;
    }
    
    /**
    * @param 对prefix进行赋值
    */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /**
    * @return 返回 base64Key
    */
    public boolean isBase64Key() {
        return base64Key;
    }
    
    /**
    * @param 对base64Key进行赋值
    */
    public void setBase64Key(boolean base64Key) {
        this.base64Key = base64Key;
    }
    
    /**
     * 处理key
     */
    protected String handleKey(final String key) {
        if (base64Key) {
            return this.prefix + Base64.encodeBase64String(key.getBytes());
        }
        
        return this.prefix + key;
    }
    
    /**
     * 转换key，去掉空格
     * 
     * @param object
     * @return
     */
    protected String objectToString(Object object) {
        if (object == null) {
            return null;
        }
        else if (object instanceof String) {
            return ((String)object).trim();
        }
        else {
            return object.toString();
        }
    }
}
