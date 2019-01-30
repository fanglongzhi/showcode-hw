package com.msb.join.demo.utils;

import org.apache.tomcat.util.buf.HexUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @program:
 * @description: 加解密工具
 * @author: zhifanglong
 * @create: 2018-11-14 16:38
 */
public class EncipherTool {
    //MSB平台统一的属性值加解密密码
    private static final String PASSWORD="SSOMSBPLATFORM";
    //MSB平台统一的对称加密密钥salt
    private static final String SALT = "deadbeef";
    //MSB平台统一的加密组件
    private static final String CIPHER = "AES/CBC/PKCS5Padding";

    private static final String SECRET_KEY_FACTORY="PBKDF2WithHmacSHA1";

    //加密算法
    private static final String CIPHER_TYPE="AES";

    /**
     * 解密方法
     * @param contentStr
     * @return
     * @throws Exception
     */
    public static String decode(String contentStr) throws Exception{
        byte[] content = hexStringToByte(contentStr);
        Cipher cipher =Cipher.getInstance(CIPHER);

        PBEKeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), hexStringToByte(SALT), 1024, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        SecretKey secretKey =factory.generateSecret(keySpec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), CIPHER_TYPE);

        byte[] subarray = new byte[16];
        byte[] contentarray=new byte[content.length-16];

        System.arraycopy(content, 0, subarray, 0, 16);
        System.arraycopy(content, 16, contentarray, 0, content.length-16);

        IvParameterSpec iv =new IvParameterSpec(subarray);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec,iv);
        byte[] original = cipher.doFinal(contentarray);
        return new String(original);

    }

    /**
     * 加密方法
     * @param content
     * @return
     * @throws Exception
     */
    public static String encode(String content) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        IvParameterSpec iv =new IvParameterSpec(bytes);

        String msg="";
        Cipher cipher =Cipher.getInstance(CIPHER);

        PBEKeySpec keySpec = new PBEKeySpec(PASSWORD.toCharArray(), hexStringToByte(SALT), 1024, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY);
        SecretKey secretKey =factory.generateSecret(keySpec);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), CIPHER_TYPE);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec,iv);
        byte[] encontent =cipher.doFinal(content.getBytes("utf-8"));

        byte[] result =concatenate(new byte[][]{bytes,encontent});

        return HexUtils.toHexString(result);
    }

    /**
     * 将多维数组转换成一维数组
     * @param arrays
     * @return
     */
    public static byte[] concatenate(byte[]... arrays) {
        int length = 0;
        byte[][] var2 = arrays;
        int destPos = arrays.length;

        for(int var4 = 0; var4 < destPos; ++var4) {
            byte[] array = var2[var4];
            length += array.length;
        }

        byte[] newArray = new byte[length];
        destPos = 0;
        byte[][] var9 = arrays;
        int var10 = arrays.length;

        for(int var6 = 0; var6 < var10; ++var6) {
            byte[] array = var9[var6];
            System.arraycopy(array, 0, newArray, destPos, array.length);
            destPos += array.length;
        }

        return newArray;
    }
    /**
     * 把16进制字符串转换成字节数组
     * @param
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

}
