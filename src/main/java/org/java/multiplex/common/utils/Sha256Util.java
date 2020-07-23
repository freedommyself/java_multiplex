package org.java.multiplex.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Java SHA-256加密的两种实现方法：
 * 1.利用Java自带的方法实现加密{@link #encode(String)}
 * 2.利用Apache的工具类实现加密
 *
 * @author wangpeng
 * @version 1.0
 * @description SHA-256加密
 * @create_time 2020/07/14 18:09:06
 */
public class Sha256Util {

    /**
     * java原生sha256加密
     * <p>
     * 创建加密对象MessageDigest 并传入加密类型SHA-256
     * 传入要加密的字符串data
     * 得到byte[]类型结果digest
     * 遍历digest将digest转换为string
     * 得到返回结果
     *
     * @param data 待加密字符串
     * @return 加密后字符串
     * @throws NoSuchAlgorithmException MessageDigest.getInstance("SHA-256")
     */
    public static String encode(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] digest = messageDigest.digest();
        StringBuilder strHexString = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                strHexString.append('0');
            }
            strHexString.append(hex);
        }
        return strHexString.toString();
    }

}
