/*
* 文 件 名:  FileItem.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  文件元数据。
* 修 改 人:  zhouliang
* 修改时间:  2012-5-24
*/
package org.yy.framework.util.lang;

/**
 * 
 * 文件工具类
 * 
 * @author  zhouliang
 * @version  [1.0, 2013年12月23日]
 * @since  [framework-util/1.0]
 */
public final class FileUtils {
    
    private FileUtils() {
    }
    
    /**
     * 获取文件的真实后缀名。目前只支持JPG, GIF, PNG, BMP四种图片文件。
     * 
     * @param bytes 文件字节流
     * @return JPG, GIF, PNG or null
     */
    public static String getFileSuffix(byte[] bytes) {
        if (bytes == null || bytes.length < 10) {
            return null;
        }
        
        if (bytes[0] == 'G' && bytes[1] == 'I' && bytes[2] == 'F') {
            return "GIF";
        }
        else if (bytes[1] == 'P' && bytes[2] == 'N' && bytes[3] == 'G') {
            return "PNG";
        }
        else if (bytes[6] == 'J' && bytes[7] == 'F' && bytes[8] == 'I' && bytes[9] == 'F') {
            return "JPG";
        }
        else if (bytes[0] == 'B' && bytes[1] == 'M') {
            return "BMP";
        }
        else {
            return null;
        }
    }
    
    /**
     * 获取文件的真实媒体类型。目前只支持JPG, GIF, PNG, BMP四种图片文件。
     * 
     * @param bytes 文件字节流
     * @return 媒体类型(MEME-TYPE)
     */
    public static String getMimeType(byte[] bytes) {
        String suffix = getFileSuffix(bytes);
        String mimeType;
        
        if ("JPG".equals(suffix)) {
            mimeType = "image/jpeg";
        }
        else if ("GIF".equals(suffix)) {
            mimeType = "image/gif";
        }
        else if ("PNG".equals(suffix)) {
            mimeType = "image/png";
        }
        else if ("BMP".equals(suffix)) {
            mimeType = "image/bmp";
        }
        else {
            mimeType = "application/octet-stream";
        }
        
        return mimeType;
    }
    
}