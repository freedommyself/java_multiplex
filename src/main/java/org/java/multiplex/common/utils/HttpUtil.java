package org.java.multiplex.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * HttpClient4.5是org.apache.http.client下操作远程url的工具包
 *
 * @author wangpeng
 * @version 1.0
 * @description apacheHttpClient4
 * @create_time 2020/7/16 0016 17:04:32
 */
public class HttpUtil {

    private static CloseableHttpClient httpClient = null;

    private static RequestConfig requestConfig = null;

    /**
     * 1.通过默认配置创建一个httpClient实例
     * 2.设置配置请求参数
     *      2.1.连接主机服务超时时间
     *      2.2.请求超时时间
     *      2.3.数据读取超时时间
     */
    static {
        httpClient = HttpClients.createDefault();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
    }

    /**
     * 1.创建httpGet远程连接实例
     * 2.设置请求头信息,鉴权
     * 3.为httpGet实例设置配置
     * 4.执行get请求得到返回对象
     * 5.通过返回对象获取返回数据
     * 6.通过EntityUtils中的toString方法将结果转换为字符串
     *
     * @param httpUrl    httpGet请求地址
     * @param paramsMap  httpGet请求参数
     * @param headersMap httpGet请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpClient4DoGet(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        HttpGet httpGet = new HttpGet(setApacheHttpClient4GetUrl(httpUrl, paramsMap));
        httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        setApacheHttpClient4GetRequestHeader(httpGet, headersMap);
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 1.创建httpPost远程连接实例
     * 2.设置请求头
     * 3.为httpPost实例设置配置
     * 4.封装post请求参数
     * 5.为httpPost设置封装好的请求参数
     * 6.httpClient对象执行post请求,并返回响应参数对象
     * 7.从响应对象中获取响应内容
     *
     * @param httpUrl    httpPost请求地址
     * @param paramsMap  httpPost请求参数
     * @param headersMap httpPost请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpClient4DoPost(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        setApacheHttpClient4PostRequestHeader(httpPost, headersMap);
        httpPost.setConfig(requestConfig);
        setApacheHttpClient4PostPrams(httpPost, paramsMap);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 拼接httpGet请求地址
     *
     * @param httpUrl   httpGet请求地址
     * @param paramsMap httpGet请求参数
     * @return 拼接后的地址
     */
    private static String setApacheHttpClient4GetUrl(String httpUrl, Map<String, String> paramsMap) {
        if (paramsMap != null && paramsMap.size() > 0) {
            httpUrl += paramsMap.keySet().stream()
                    .map(key -> key + "=" + paramsMap.get(key) + "&")
                    .reduce("?", (x, y) -> x + y);
            httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
        }
        return httpUrl;
    }

    /**
     * 封装httpPost请求参数
     *
     * @param httpPost  httpPost请求地址
     * @param paramsMap httpPost请求参数
     */
    private static void setApacheHttpClient4PostPrams(HttpPost httpPost, Map<String, String> paramsMap) {
        try {
            if (paramsMap != null && paramsMap.size() > 0) {
                List<NameValuePair> nvpList = paramsMap
                        .keySet()
                        .stream()
                        .map(key -> new BasicNameValuePair(key, paramsMap.get(key)))
                        .collect(Collectors.toList());
                httpPost.setEntity(new UrlEncodedFormEntity(nvpList, "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置httpGet请求头
     *
     * @param httpGet    {@link HttpGet}
     * @param headersMap httpGet请求头
     */
    private static void setApacheHttpClient4GetRequestHeader(HttpGet httpGet, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                httpGet.addHeader(key, headersMap.get(key));
        }
    }

    /**
     * 设置httpPost请求头
     *
     * @param httpPost   {@link HttpPost}
     * @param headersMap httpPost请求头
     */
    private static void setApacheHttpClient4PostRequestHeader(HttpPost httpPost, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                httpPost.addHeader(key, headersMap.get(key));
        }
    }

}
