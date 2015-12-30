/*
* 文 件 名:  RadixUtil.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2015,  All rights reserved
* 描    述:  进制转换工具类
* 修 改 人:  zhouliang
* 修改时间:  2015年12月23日
* 修改内容:  <修改内容>
*/
package org.yy.framework.util.lang;

import java.math.BigDecimal;

import org.yy.framework.util.string.GenerateConstants;

/**
* 进制转换工具类
* 
* @author  zhouliang
* @version  [1.0, 2015年12月24日]
* @since  [framework-util/1.0]
*/
public final class RadixUtils {
	
	private RadixUtils(){}
    
    /**
     * 把10进制的数字转换成指定进制字符
     * 
     * @param number 十进制数
     * @param radix 进制
     * @return
     */
    public static String convertNumber(long number, int radix) {
        if (radix > GenerateConstants.RADIX || radix <= 0) {
            throw new IllegalArgumentException("非法的进制, 只支持1-64. 参数为:[" + radix + "]");
        }
        StringBuilder result = new StringBuilder();
        long temp = number;
        do {
            result.append(GenerateConstants.RADIX_STR[(int)(temp % radix)]);
            temp = temp / radix;
        } while (temp != 0);
        
        return result.reverse().toString();
    }
    
    /**
     * 把10进制的数字转换成指定进制字符
     * 
     * @param number 十进制数
     * @param radix 进制
     * @return
     */
    public static String convertBigDecimal(String number, int radix) {
        if (radix > GenerateConstants.RADIX || radix <= 0) {
            throw new IllegalArgumentException("非法的进制, 只支持1-64. 参数为:[" + radix + "]");
        }
        StringBuilder result = new StringBuilder();
        BigDecimal temp = new BigDecimal(number);
        BigDecimal radixBD = new BigDecimal("" + radix);
        do {
            BigDecimal[] r = temp.divideAndRemainder(radixBD);
            temp = r[0];
            result.append(GenerateConstants.RADIX_STR[(int)(r[1].intValue())]);
        } while (temp.compareTo(BigDecimal.ZERO) > 0);
        
        return result.reverse().toString();
    }
    
    /**
     * 把字符串转换成十进制数
     * 
     * @param number 十进制数
     * @param radix 进制
     * @return
     */
    public static long unconvertNumber(String str, int radix) {
        if (radix > GenerateConstants.RADIX || radix <= 0) {
            throw new IllegalArgumentException("非法的进制, 只支持1-64. 参数为:[" + radix + "]");
        }
        if (str == null || "".equals(str.trim())) {
            throw new IllegalArgumentException("非法的字符串, 不能为空值");
        }
        
        long result = 0;
        int v;
        int len = str.length();
        for (int i = 0; i < str.length(); ++i) {
            v = getCharIndexNum(str.charAt(i));
            result += v * Math.pow(radix, len - i - 1);
        }
        return result;
    }
    
    /**
     * 把字符串转换成十进制数
     * 
     * @param number 十进制数
     * @param radix 进制
     * @return
     */
    public static BigDecimal unconvertBigDecimal(String str, int radix) {
        if (radix > GenerateConstants.RADIX || radix <= 0) {
            throw new IllegalArgumentException("非法的进制, 只支持1-64. 参数为:[" + radix + "]");
        }
        if (str == null || "".equals(str.trim())) {
            throw new IllegalArgumentException("非法的字符串, 不能为空值");
        }
        
        BigDecimal result = BigDecimal.valueOf(0);
        BigDecimal radixBD = new BigDecimal("" + radix);
        int v;
        int len = str.length();
        for (int i = 0; i < str.length(); ++i) {
            v = getCharIndexNum(str.charAt(i));
            result = result.add(radixBD.pow(len - i - 1).multiply(BigDecimal.valueOf(v)));
        }
        return result;
    }
    
    /**
     * 获取字符对应的十进制数
     */
    public static int getCharIndexNum(char ch) {
        int num = ((int)ch);
        if (num >= '0' && num <= '9') {
            return num - 48;
        }
        else if (num >= 'a' && num <= 'z') {
            return num - 87;
        }
        else if (num >= 'A' && num <= 'Z') {
            return num - 29;
        }
        else if (num == GenerateConstants.special1) { //处理@字符 
            return 62;
        }
        else if (num == GenerateConstants.special2) { //处理+字符
            return 63;
        }
        throw new RuntimeException("not support char!");
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(RadixUtils.convertNumber(0, 64));
        System.out.println(RadixUtils.convertNumber(1, 64));
        System.out.println(RadixUtils.convertNumber(62, 10));
        System.out.println(RadixUtils.convertNumber(63, 64));
        System.out.println(RadixUtils.convertNumber(64, 64));
        System.out.println(RadixUtils.convertNumber(127, 64));
        System.out.println(RadixUtils.convertNumber(128, 64));
        System.out.println(RadixUtils.convertBigDecimal("0", 64));
        System.out.println(RadixUtils.convertBigDecimal("1", 64));
        System.out.println(RadixUtils.convertBigDecimal("62", 10));
        System.out.println(RadixUtils.convertBigDecimal("63", 64));
        System.out.println(RadixUtils.convertBigDecimal("64", 64));
        System.out.println(RadixUtils.convertBigDecimal("127", 64));
        System.out.println(RadixUtils.convertBigDecimal("128", 64));
        System.out.println(RadixUtils.convertNumber(Long.MAX_VALUE, 64));
        System.out.println(RadixUtils.convertBigDecimal("9223372036854775807", 64));
        System.out.println(RadixUtils.unconvertNumber("189", 64));
        System.out.println(RadixUtils.unconvertNumber(RadixUtils.convertNumber(Long.MAX_VALUE, 64), 64));
        System.out.println(RadixUtils.unconvertBigDecimal(RadixUtils.convertNumber(Long.MAX_VALUE, 64), 64));
        System.out.println(RadixUtils.unconvertBigDecimal("80000000000", 64));
        
        BigDecimal b = new BigDecimal(64);
        System.out.println(b.pow(2));
        b = null;
        
    }
}
