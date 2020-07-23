package org.java.multiplex.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Java MD5加密的两种实现方法:
 * 1.java原生{@link #encode(String, String)}
 * 2.使用Spring中的工具类
 *
 * @author wangpeng
 * @version 1.0
 * @description MD5加密
 * @create_time 2020/7/23 14:24:32
 */
public class MD5Util {

    /**
     * 初始化一个字符串数组,用来存放每个16进制字符
     */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 这里是针对单个byte,256的byte通过16拆分为d1和d2
     *
     * @param b 待转换成2个16进制字符的字节
     * @return 转换后的16进制字符
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 这里主要是遍历byte，转化为16位进制的字符，即0-F
     *
     * @param b MD5转换后的字节数组
     * @return 字节数组转换成的字符串
     */
    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    /**
     * MD5加密
     *
     * @param origin  待加密字符串
     * @param charset 字符编码
     * @return 加密后的字符串
     * @throws Exception MessageDigest.getInstance("MD5");origin.getBytes(charset)
     */
    public static String encode(String origin, String charset) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        String md5String;
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (charset == null || "".equals(charset)) {
            md5String = byteArrayToHexString(md.digest(origin.getBytes()));
        } else {
            md5String = byteArrayToHexString(md.digest(origin.getBytes(charset)));
        }
        return md5String;
    }

}
