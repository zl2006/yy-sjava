/*
* 文 件 名:  TypeEnum.java
* 版    权:  YY Technologies Co., Ltd. Copyright 1993-2012,  All rights reserved
* 描    述:  参数类型
* 修 改 人:  zhouliang
* 修改时间:  2013年11月11日
*/
package org.yy.framework.document.anno;

/**
* 参数类型
* 
* @author  zhouliang
* @version  [1.0, 2013年11月11日]
* @since  [framework-basedata/1.0]
*/
public enum TypeEnum {
    
    INTETER("integer", "整数"), DECIMAL("decimal", "小数"), STRING("string", "字符串"), DATE("date", "日期"), FILE("file", "文件"), OBJECT(
        "object", "对象");
    
    private String code;
    
    private String desc;
    
    private TypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return code + ":" + desc;
    }
    
    public static void main(String[] args) {
        System.out.println(TypeEnum.DATE);
    }
}
