package org.java.multiplex.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Java实现Base64加解密的方式主要有以下四种:
 * 1：JDK中的sun.misc套件
 * 2：第三方扩展包 bouncy castle
 * 3：第三方扩展包 commons codec
 * 4：JDK8及更高版本中的 java.util.Base64{@link Base64Util}
 * <a href="https://blog.csdn.net/u012187452/article/details/83239117">相关链接</a>
 *
 * @author wangpeng
 * @version 1.0
 * @description base64编码
 * @create_time 2020/07/13  11:20:20
 */
public class Base64Util {

    /**
     * 基本：输出被映射到一组字符A-Za-z0-9+/，编码不添加任何行标，输出的解码仅支持A-Za-z0-9+/
     *
     * @param originString 待编码字符串
     * @return base64编码后的字符串
     */
    public static String encode(String originString) {
        byte[] asBytes = Base64.getEncoder().encode(originString.getBytes(StandardCharsets.UTF_8));
        return new String(asBytes, StandardCharsets.UTF_8);
    }

    /**
     * 基本编码解密
     *
     * @param encodeString base64编码后的字符串
     * @return 原字符串
     */
    public static String decode(String encodeString) {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, StandardCharsets.UTF_8);
    }

    /**
     * URL：输出映射到一组字符A-Za-z0-9+_，输出是URL和文件
     *
     * @param originString 待编码字符串
     * @return base64编码后的字符串
     */
    public static String urlEncode(String originString) {
        byte[] asBytes = Base64.getEncoder().encode(originString.getBytes(StandardCharsets.UTF_8));
        return new String(asBytes, StandardCharsets.UTF_8);
    }

    /**
     * URL编码解密
     *
     * @param encodeString base64编码后的字符串
     * @return 原字符串
     */
    public static String urlDecode(String encodeString) {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, StandardCharsets.UTF_8);
    }

    /**
     * MIME：输出映射到MIME友好格式。输出每行不超过76字符，并且使用’\r’并跟随’\n’作为分割。编码输出最后没有行分割
     *
     * @param originString 待编码字符串
     * @return base64编码后的字符串
     */
    public static String mimeEncode(String originString) {
        byte[] asBytes = Base64.getMimeEncoder().encode(originString.getBytes(StandardCharsets.UTF_8));
        return new String(asBytes, StandardCharsets.UTF_8);
    }

    /**
     * MIME编码解密
     *
     * @param encodeString base64编码后的字符串
     * @return 原字符串
     */
    public static String mimeDecode(String encodeString) {
        byte[] asBytes = Base64.getDecoder().decode(encodeString);
        return new String(asBytes, StandardCharsets.UTF_8);
    }
}
