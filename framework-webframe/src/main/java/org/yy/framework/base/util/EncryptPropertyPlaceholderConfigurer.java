package org.yy.framework.base.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 
 * spring配置文件加密处理，例如加密数据库用户名及密码
 * 
 *  用法
    <bean class="com.cyou.web.common.EncryptPropertyPlaceholderConfigurer" p:location="classpath:/config.properties"></bean>  
    代替  
    <context:property-placeholder location="classpath:/config.properties" />  
    
 * @author  zhouliang
 * @version  [1.0, 2016年4月6日]
 * @since  [framework-webframe/1.0]
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    
    private static String[] encryptPropNames = {"jdbc.username", "jdbc.password"};
    
    private static final String ENCRYPT_TYPE = "AES";
    
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        //如果在加密属性名单中发现该属性
        if (isEncryptProp(propertyName)) {
            String decryptValue = Encrypt.encrypt(propertyValue, "D#Sd$2dafG2p32");
            return decryptValue;
        }
        else {
            return propertyValue;
        }
        
    }
    
    private boolean isEncryptProp(String propertyName) {
        for (String encryptName : encryptPropNames) {
            if (encryptName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args)
        throws Exception {
        System.out.println(Encrypt.encrypt("admin", "D#Sd$2dafG2p32"));
        System.out.println(Encrypt.decrypt("MLmymfLb/YtgD0s2XfKADg==", "D#Sd$2dafG2p32"));
    }
    
    private static class Encrypt {
        
        /**
         * AES加密
         * 
         * @param content
         *            待加密的内容
         * @param encryptKey
         *            加密密钥
         * @return 加密后的byte[]
         * @throws Exception
         */
        public static byte[] encrypt(byte[] content, String encryptKey)
            throws RuntimeException {
            SecureRandom secureRandom;
            try {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(encryptKey.getBytes());
                
                KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPT_TYPE);
                kgen.init(secureRandom);
                
                Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), ENCRYPT_TYPE));
                
                return cipher.doFinal(content);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            
        }
        
        /**
         * AES加密为base 64 code
         * 
         * @param content
         *            待加密的内容
         * @param encryptKey
         *            加密密钥
         * @return 加密后的base 64 code
         * @throws Exception
         */
        public static String encrypt(String content, String encryptKey) {
            try {
                return Base64.encodeBase64String(encrypt(content.getBytes("utf-8"), encryptKey));
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        /**
         * AES解密
         * 
         * @param encryptBytes
         *            待解密的byte[]
         * @param decryptKey
         *            解密密钥
         * @return 解密后的String
         * @throws Exception
         */
        public static String decrypt(byte[] encryptBytes, String decryptKey)
            throws Exception {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(decryptKey.getBytes());
            
            KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPT_TYPE);
            kgen.init(secureRandom);
            
            Cipher cipher = Cipher.getInstance(ENCRYPT_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), ENCRYPT_TYPE));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            
            return new String(decryptBytes);
        }
        
        /**
         * 将base 64 code AES解密
         * 
         * @param encryptStr
         *            待解密的base 64 code
         * @param decryptKey
         *            解密密钥
         * @return 解密后的string
         * @throws Exception
         */
        public static String decrypt(String encryptStr, String decryptKey)
            throws Exception {
            return decrypt(Base64.decodeBase64(encryptStr), decryptKey);
        }
    }
}