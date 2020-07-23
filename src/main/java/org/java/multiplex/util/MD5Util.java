package org.java.multiplex.util;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangpeng
 * @version 1.0
 * @description
 * @create_time 2020/7/23 0023 14:24:32
 */
public class MD5Util {

    //=================================================最简单原生实现===================================================
    public static String encode1(String origin) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(origin.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8字节。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位字节转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
        return new BigInteger(1, md.digest()).toString(16);
    }

    //==================================================复杂原生实现====================================================
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    //这里是针对单个byte，256的byte通过16拆分为d1和d2
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    //这里主要是遍历8个byte，转化为16位进制的字符，即0-F
    private static String byteArrayToHexString(byte b[]) {
        StringBuilder resultSb = new StringBuilder();
        for (byte value : b) {
            resultSb.append(byteToHexString(value));
        }
        return resultSb.toString();
    }

    //MD5加密
    public static String encode(String origin, String charset) {
        try {
            String md5String;
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (charset == null || "".equals(charset)) {
                md5String = byteArrayToHexString(md.digest(origin.getBytes()));
            } else {
                md5String = byteArrayToHexString(md.digest(origin.getBytes(charset)));
            }
            return md5String;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    //==================================================spring工具类实现================================================

    //生成MD5加密
    public static String getMD5(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

}
