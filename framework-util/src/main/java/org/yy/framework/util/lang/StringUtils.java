package org.yy.framework.util.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.yy.framework.basedata.Constants;

/**
 * 字符串工具类。
 * 
 * @author carver.gu
 * @since 1.0, Sep 12, 2009
 */
public abstract class StringUtils {
    
    private StringUtils() {
    }
    
    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     * 
     * @param value 待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
    public static boolean isNumeric(Object obj) {
        if (obj == null) {
            return false;
        }
        char[] chars = obj.toString().toCharArray();
        int length = chars.length;
        if (length < 1)
            return false;
        
        int i = 0;
        if (length > 1 && chars[0] == '-')
            i = 1;
        
        for (; i < length; i++) {
            if (!Character.isDigit(chars[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查指定的字符串列表是否不为空。
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        }
        else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }
    
    /**
     * 把通用字符编码的字符串转化为汉字编码。
     */
    public static String unicodeToChinese(String unicode) {
        StringBuilder out = new StringBuilder();
        if (!isEmpty(unicode)) {
            for (int i = 0; i < unicode.length(); i++) {
                out.append(unicode.charAt(i));
            }
        }
        return out.toString();
    }
    
    public static String toUnderlineStyle(String name) {
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    newName.append("_");
                }
                newName.append(Character.toLowerCase(c));
            }
            else {
                newName.append(c);
            }
        }
        return newName.toString();
    }
    
    public static String convertString(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        }
        else {
            try {
                return new String(data, offset, length, Constants.CHARSET_UTF8);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static byte[] convertBytes(String data) {
        if (data == null) {
            return null;
        }
        else {
            try {
                return data.getBytes(Constants.CHARSET_UTF8);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static Date parseDateTime(String str) {
        DateFormat format = new SimpleDateFormat(Constants.DATE_TIME_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(Constants.DATE_TIMEZONE));
        try {
            return format.parse(str);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 左填充pad字符，以达到length长度
     * 
     * @param str
     *            原字符串
     * @param length
     *            填充后的长度
     * @param pad
     *            填充字符
     * @return
     */
    public static String leftPad(String str, int length, String pad) {
        
        if (str == null) {
            return str;
        }
        if (str.length() >= length) {
            return str;
        }
        int padlen = length - str.length();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padlen; ++i) {
            if (str.length() + pad.length() > length) {
                break;
            }
            sb.append(pad);
        }
        sb.append(str);
        return sb.toString();
    }
    
}
