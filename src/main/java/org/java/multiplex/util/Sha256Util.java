package org.java.multiplex.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangpeng
 * @version 1.0
 * @description SHA-256加密
 * @create_time 2020/07/14 18:09:06
 */
public class Sha256Util {

    //sha256加密
    public static String encode(String data) {
        try {
            //创建加密对象MessageDigest 并传入加密类型SHA-256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            //传入要加密的字符串data
            messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
            //得到byte[]类型结果digest
            byte[] digest = messageDigest.digest();
            StringBuilder strHexString = new StringBuilder();
            //遍历digest将digest转换为string
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            //得到返回结果
            return strHexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //SHA256加密
    public static String apacheEncode(String data) {
        //创建加密对象MessageDigest
        MessageDigest newDigest = DigestUtils.getSha256Digest();
        //传入要加密的字符串data
        newDigest.update(data.getBytes());
        //得到byte[]类型结果digest
        byte[] digest = newDigest.digest();
        //将digest转换为string得到返回结果
        return Hex.encodeHexString(digest);
    }

}
