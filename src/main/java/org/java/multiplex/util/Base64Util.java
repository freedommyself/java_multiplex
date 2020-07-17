package org.java.multiplex.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author wangpeng
 * @version 1.0
 * @description base64编码
 * @create_time 2020年07月13日 星期一 11:20:20
 */
public class Base64Util {
    public static String encode(String originString) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(originString.getBytes("utf-8"));
    }

    public static String decode(String encodeString) throws UnsupportedEncodingException {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, "utf-8");
    }

    public static String urlEncode(String originString) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(originString.getBytes("utf-8"));
    }

    public static String urlDecode(String encodeString) throws UnsupportedEncodingException {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, "utf-8");
    }

    public static String mimeEncode(String originString) throws UnsupportedEncodingException {
        return Base64.getMimeEncoder().encodeToString(originString.getBytes("utf-8"));
    }

    public static String mimeDecode(String encodeString) throws UnsupportedEncodingException {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, "utf-8");
    }
}
