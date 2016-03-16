/*
* 文 件 名:  ValidateUtil.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  验证实体
* 修 改 人:  zhouliang
* 修改时间:  2013年12月1日
* 修改内容:  <修改内容>
*/
package org.yy.framework.base.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
* 验证实体
* 
* @author  zhouliang
* @version  [1.0, 2013年12月1日]
* @since  [framework-webframe/1.0]
*/
public class ValidateService {
    
    private static Validator validator; // 它是线程安全的 
    
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    /**
     * 验证对象所有被元注解配置的属性
     * @param object  验证对象
     * @return 错误消息
     */
    public static <T> List<ValidateError> validate(T object)
        throws ValidationException {
        List<ValidateError> errors = new ArrayList<ValidateError>();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                errors.add(new ValidateError(constraintViolation.getPropertyPath().toString(),
                    constraintViolation.getMessage()));
            }
        }
        return errors;
    }
    
    /**
     * 验证对象指定的属性
     * @param object 对象
     * @param propertyNames  属性列表
     * @return 错误消息
     */
    public static <T> List<ValidateError> validate(T object, String... propertyNames) {
        List<ValidateError> errors = new ArrayList<ValidateError>();
        for (String item : propertyNames) {
            Set<ConstraintViolation<T>> constraintViolations = validator.validateProperty(object, item);
            if (constraintViolations.size() > 0) {
                for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                    errors.add(new ValidateError(constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getMessage()));
                }
            }
        }
        return errors;
    }
}
