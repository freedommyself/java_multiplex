package org.java.multiplex.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangpeng
 * @version 1.0
 * @description apacheHttpClient4
 * @create_time 2020/7/16 0016 17:04:32
 */
public class HttpUtil {

    private static CloseableHttpClient httpClient = null;

    private static RequestConfig requestConfig = null;

    private static CloseableHttpClient httpsClient = null;


    /**
     *
     * 初始化一个httpClient实例（公用）设置请求相关配置
     */
    static {
        httpClient = HttpClients.createDefault();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(35000)
                .setConnectionRequestTimeout(35000)
                .setSocketTimeout(60000)
                .build();
        httpsClient = createSSLInsecureClient();
        //httpsClient=createSSLCertInsecureClient();
    }

    /**
     * 发送httpGet请求
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
     * 发送httpsGet请求
     *
     * @param httpUrl    httpGet请求地址
     * @param paramsMap  httpGet请求参数
     * @param headersMap httpGet请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpsClient4DoGet(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        HttpGet httpGet = new HttpGet(setApacheHttpClient4GetUrl(httpUrl, paramsMap));
        httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        setApacheHttpClient4GetRequestHeader(httpGet, headersMap);
        httpGet.setConfig(requestConfig);
        try (CloseableHttpResponse httpResponse = httpsClient.execute(httpGet)) {
            HttpEntity httpEntity = httpResponse.getEntity();
            result = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送httpPost请求
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
     * 发送httpsPost请求
     *
     * @param httpUrl    httpPost请求地址
     * @param paramsMap  httpPost请求参数
     * @param headersMap httpPost请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpsClient4DoPost(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        HttpPost httpPost = new HttpPost(httpUrl);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        setApacheHttpClient4PostRequestHeader(httpPost, headersMap);
        httpPost.setConfig(requestConfig);
        setApacheHttpClient4PostPrams(httpPost, paramsMap);
        try (CloseableHttpResponse httpResponse = httpsClient.execute(httpPost)) {
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
            for (String key : headersMap.keySet()) {
                httpGet.addHeader(key, headersMap.get(key));
            }
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
            for (String key : headersMap.keySet()) {
                httpPost.addHeader(key, headersMap.get(key));
            }
        }
    }

    /**
     * httpsClient证书初始化
     *
     * @param certPath     证书路径
     * @param certPassword 证书密码
     */
    @SuppressWarnings("unused")
    private static CloseableHttpClient createSSLCertInsecureClient(String certPath, String certPassword) {
        SSLContext sslcontext = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream inputStream = new FileInputStream(new File(certPath));
            keyStore.load(inputStream, certPassword.toCharArray());
            sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPassword.toCharArray()).build();
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | CertificateException
                | KeyManagementException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory sslCsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"},
                null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return HttpClients.custom().setSSLSocketFactory(sslCsf).build();
    }

    /**
     * 信任任意证书 (简单，但不建议用于生产代码)
     *
     * @return httpsClient对象
     */
    @SuppressWarnings("deprecation")
    public static CloseableHttpClient createSSLInsecureClient() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(factory).build();
    }
}
