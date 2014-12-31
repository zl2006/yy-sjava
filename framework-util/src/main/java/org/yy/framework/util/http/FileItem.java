/*
* 文 件 名:  FileItem.java
* 版    权:  YY Technologies Co., Ltd. Copyright 2012-2013,  All rights reserved
* 描    述:  文件元数据。
* 修 改 人:  zhouliang
* 修改时间:  2012-5-24
*/
package org.yy.framework.util.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * 文件元数据。
 * 
 * @author  zhouliang
 * @version  [1.0, 2013年12月23日]
 * @since  [framework-util/1.0]
 */
public class FileItem {
    
    private String fileName;
    
    private String mimeType;
    
    private byte[] content;
    
    private File file;
    
    /**
     * 基于本地文件的构造器。
     * 
     * @param file 本地文件
     */
    public FileItem(File file) {
        this.file = file;
    }
    
    /**
     * 基于文件绝对路径的构造器。
     * 
     * @param filePath 文件绝对路径
     */
    public FileItem(String filePath) {
        this(new File(filePath));
    }
    
    /**
     * 基于文件名和字节流的构造器。
     * 
     * @param fileName 文件名
     * @param content 文件字节流
     */
    public FileItem(String fileName, byte[] content) {
        this.fileName = fileName;
        this.content = content;
    }
    
    /**
     * 基于文件名、字节流和媒体类型的构造器。
     * 
     * @param fileName 文件名
     * @param content 文件字节流
     * @param mimeType 媒体类型
     */
    public FileItem(String fileName, byte[] content, String mimeType) {
        this(fileName, content);
        this.mimeType = mimeType;
    }
    
    public String getFileName() {
        if (this.fileName == null && this.file != null && this.file.exists()) {
            this.fileName = file.getName();
        }
        return this.fileName;
    }
    
    public String getMimeType()
        throws IOException {
        if (this.mimeType == null) {
            this.mimeType = FileUtils.getMimeType(getContent());
        }
        return this.mimeType;
    }
    
    public byte[] getContent()
        throws IOException {
        if (this.content == null && this.file != null && this.file.exists()) {
            InputStream in = null;
            ByteArrayOutputStream out = null;
            
            try {
                in = new FileInputStream(this.file);
                out = new ByteArrayOutputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    out.write(ch);
                }
                this.content = out.toByteArray();
            }
            finally {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        }
        return this.content;
    }
    
}