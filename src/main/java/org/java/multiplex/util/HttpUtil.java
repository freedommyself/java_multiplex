package org.java.multiplex.util;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpEntity;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 目前JAVA实现HTTP请求的方法用的最多的有两种：
 * 1.一种是通过HTTPClient这种第三方的开源框架去实现。
 * HTTPClient对HTTP的封装性比较不错，通过它基本上能够满足我们大部分的需求，
 * HttpClient3.1是org.apache.commons.httpclient下操作远程url的工具包，
 * 虽然已不再更新，但实现工作中使用httpClient3.1的代码还是很多，
 * {@link #apacheHttpClient3DoGet(String, Map, Map),#apacheHttpClient3DoPost(String, Map, Map)}
 * HttpClient4.5是org.apache.http.client下操作远程url的工具包，最新的；
 * 2.通过HttpURLConnection去实现{@link #javaHttpDoGet(String, Map, Map),#javaHttpDoPost(String, Map, Map)} }
 * java.net.HttpURLConnection是JAVA的标准类，是JAVA比较原生的一种实现方式。
 *
 * @author wangpeng
 * @version 1.0
 * @description HTTP[S]请求
 * @create_time 2020/7/15 12:55:04
 */
public class HttpUtil {
//==============================================================java原生================================================================

    /**
     * 向指定URL发送HTTP.GET方法的请求
     *
     * @param httpUrl http请求路径
     * @param params  http请求参数
     *                形式{@code name1=value1&name2=value2}
     * @param headers http请求头
     * @return URL 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String javaHttpDoGet(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        String result = null;
        try {
            //创建远程url连接对象
            URL url = new URL(setParams(httpUrl, params));
            //通过远程url连接对象打开一个连接，强转成httpURLConnection类
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接方式：get
            connection.setRequestMethod("GET");
            //设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            //设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            //设置请求头
            setHeaders(connection, headers);
            connection.connect();
            //发送请求
            result = response(connection);
            //关闭远程连接
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向指定URL发送HTTPS.GET方法的请求
     * <p>
     * 注意：
     * https请求的时候会验证证书，有2种方法解决：
     * 1.信任所有证书
     * 2.手动导入证书
     *
     * @param httpUrl https请求路径
     * @param params  https请求参数
     *                形式{@code name1=value1&name2=value2}
     * @param headers https请求头
     * @return URL 所代表远程资源的响应结果
     * @throws IOException,NoSuchAlgorithmException,KeyManagementException
     */
    public static String javaHttpsDoGet(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        String result = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            //初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            sslContext.init(null, tm, new SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            //创建远程url连接对象
            URL url = new URL(setParams(httpUrl, params));
            //通过远程url连接对象打开一个连接，强转成httpsURLConnection类
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //设置连接方式：get
            conn.setRequestMethod("GET");
            //设置连接主机服务器的超时时间：15000毫秒
            conn.setConnectTimeout(15000);
            //设置读取远程返回的数据时间：60000毫秒
            conn.setReadTimeout(60000);
            //设置当前实例使用的SSLSocKetFactory
            conn.setSSLSocketFactory(ssf);
            //设置请求头
            setHeaders(conn, headers);
            conn.connect();
            //发送请求
            result = response(conn);
            //关闭远程连接
            conn.disconnect();
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向指定URL发送HTTP.POST方法的请求
     *
     * <p>
     * OutputStream os = connection.getOutputStream();
     * os.write(param.getBytes());
     *
     * @param httpUrl http请求路径
     * @param params  http请求参数
     *                形式{@code name1=value1&name2=value2}
     * @param headers http请求头
     * @return URL 所代表远程资源的响应结果
     * @throws IOException
     */
    public static String javaHttpDoPost(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        String result = null;
        try {
            URL url = new URL(httpUrl);
            //通过远程url连接对象打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //设置连接请求方式
            connection.setRequestMethod("POST");
            //设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            //设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            //默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            //默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            //设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            setHeaders(connection, headers);
            //通过连接对象获取一个输出流
            //通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            out.println(setParams(null, params));
            out.flush();
            out.close();
            result = response(connection);
            //断开与远程地址url的连接
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 向指定URL发送HTTPS.POST方法的请求
     *
     * @param httpUrl https请求路径
     * @param params  https请求参数
     *                形式{@code name1=value1&name2=value2}
     * @param headers https请求头
     * @throws IOException,NoSuchAlgorithmException,KeyManagementException
     */
    public static String javaHttpsDoPost(String httpUrl, Map<String, String> params, Map<String, String> headers) {
        String result = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(httpUrl);
            //通过远程url连接对象打开连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //设置连接请求方式
            conn.setRequestMethod("POST");
            //设置连接主机服务器超时时间：15000毫秒
            conn.setConnectTimeout(15000);
            //设置读取主机服务器返回数据超时时间：60000毫秒
            conn.setReadTimeout(60000);
            //默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            conn.setDoOutput(true);
            //默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            conn.setDoInput(true);
            //设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            conn.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            setHeaders(conn, headers);
            //设置当前实例使用的SSLSocKetFactory
            conn.setSSLSocketFactory(ssf);
            //通过连接对象获取一个输出流
            //通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
            PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), StandardCharsets.UTF_8));
            out.println(setParams(null, params));
            out.flush();
            out.close();
            result = response(conn);
            //断开与远程地址url的连接
            conn.disconnect();
        } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 设置HTTP[S]请求参数
     * <p>
     * 注意：
     * 1.对于get请求，返回url和参数拼接成的新的请求路径
     * 形式：{@code http://www.baidu.com?name1=value1&name2=value2}
     * 2.对于post请求，返回封装好的请求参数字符串
     * 形式：{@code name1=value1&name2=value2}
     *
     * @param url    请求路径
     * @param params 请求参数
     * @return 封装好的URL+参数|参数
     */
    private static String setParams(String url, Map<String, String> params) {
        String paramsJoin = "";
        if (params != null && params.size() > 0) {
            paramsJoin = params.keySet().stream()
                    .map(key -> key + "=" + params.get(key) + "&")
                    .reduce("", (x, y) -> x + y);
            paramsJoin = paramsJoin.substring(0, paramsJoin.length() - 1);
        }
        if (url != null && !paramsJoin.equals(""))
            return url + "?" + paramsJoin;
        else if (url != null)
            return url;
        else
            return paramsJoin;
    }

    /**
     * 设置HTTP[S]请求头
     * <p>
     * 注意：
     * 1.HttpsURLConnection向上造型复用此方法
     * 2.assert只是为了代码结构好看，无任何意义
     *
     * @param connection {@link HttpURLConnection,HttpsURLConnection}
     * @param headers    http[s]请求头
     */
    private static void setHeaders(HttpURLConnection connection, Map<String, String> headers) {
        if (headers != null && headers.size() > 0) {
            for (String key : headers.keySet()) {
                connection.setRequestProperty(key, headers.get(key));
            }
        }
        assert 1 == 1;
    }

    /**
     * HTTP[S]请求响应
     * <p>
     * 注意：
     * 状态码位200则获取返回的数据，否则返回失败信息
     *
     * @param connection {@link HttpURLConnection,HttpsURLConnection}
     * @return 远程资源的响应结果
     * @throws IOException
     */
    private static String response(HttpURLConnection connection) {
        String result = "响应失败";
        try {
            if (connection.getResponseCode() == 200) {
                //通过connection连接，获取输入流
                InputStream is = connection.getInputStream();
                //封装输入流is，并指定字符集
                BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                //获取结果
                StringBuilder sbf = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
                //关闭资源
                br.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

//==============================================================apache httpClient3================================================================

    /**
     * 向指定URL发送HTTP.GET方法的请求
     *
     * @param httpUrl    httpGet请求地址
     * @param paramsMap  httpGet请求参数
     * @param headersMap httpGet请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpClient3DoGet(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        //创建httpClient实例
        HttpClient httpClient = new HttpClient();
        //设置http连接主机服务超时时间：15000毫秒（先获取连接管理器对象，再获取参数对象,再进行参数的赋值）
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        //创建一个Get方法实例对象
        GetMethod getMethod = new GetMethod(setApacheHttpClient3GetUrl(httpUrl, paramsMap));
        //设置请求头
        setApacheHttpClient3GetRequestHeader(getMethod, headersMap);
        //设置get请求超时为60000毫秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        //设置请求重试机制，默认重试次数：3次，参数设置为true，重试机制可用，false相反
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        try {
            //执行Get方法
            int statusCode = httpClient.executeMethod(getMethod);
            //判断返回码
            if (statusCode != HttpStatus.SC_OK) {
                //如果状态码返回的不是ok,说明失败了,打印错误信息
                System.err.println("Method failed: " + getMethod.getStatusLine());
            } else {
                result = getMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放连接
        getMethod.releaseConnection();
        return result;
    }

    /**
     * 向指定URL发送HTTPS.GET方法的请求
     *
     * @param httpUrl    httpGet请求地址
     * @param paramsMap  httpGet请求参数
     * @param headersMap httpGet请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpsClient3DoGet(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        //声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        //加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        //创建httpClient实例
        HttpClient httpClient = new HttpClient();
        //设置http连接主机服务超时时间：15000毫秒（先获取连接管理器对象，再获取参数对象,再进行参数的赋值）
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        //创建一个Get方法实例对象
        GetMethod getMethod = new GetMethod(setApacheHttpClient3GetUrl(httpUrl, paramsMap));
        //设置请求头
        setApacheHttpClient3GetRequestHeader(getMethod, headersMap);
        //设置get请求超时为60000毫秒
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        //设置请求重试机制，默认重试次数：3次，参数设置为true，重试机制可用，false相反
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        try {
            //执行Get方法
            int statusCode = httpClient.executeMethod(getMethod);
            //判断返回码
            if (statusCode != HttpStatus.SC_OK) {
                //如果状态码返回的不是ok,说明失败了,打印错误信息
                System.err.println("Method failed: " + getMethod.getStatusLine());
            } else {
                result = getMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放连接
        getMethod.releaseConnection();
        return result;
    }

    /**
     * 向指定URL发送HTTP.POST方法的请求
     *
     * @param httpUrl    httpPost请求地址
     * @param paramsMap  httpPost请求参数
     * @param headersMap httpPost请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpClient3DoPost(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        //创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        //设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        //创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(httpUrl);
        //设置请求头
        setApacheHttpClient3PostRequestHeader(postMethod, headersMap);
        //设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        //设置请求重试机制，默认重试次数：3次，参数设置为true，重试机制可用，false相反
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        setApacheHttpClient3PostPrams(postMethod, headersMap);
        try {
            //执行POST方法
            int statusCode = httpClient.executeMethod(postMethod);
            //判断是否成功
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + postMethod.getStatusLine());
            } else {
                result = postMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放连接
        postMethod.releaseConnection();
        return result;
    }

    /**
     * 向指定URL发送HTTPS.POST方法的请求
     *
     * @param httpUrl    httpPost请求地址
     * @param paramsMap  httpPost请求参数
     * @param headersMap httpPost请求头
     * @return 请求地址服务器返回的数据
     */
    public static String apacheHttpsClient3DoPost(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        String result = null;
        //声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        //加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        //创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        //设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        //创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(httpUrl);
        //设置请求头
        setApacheHttpClient3PostRequestHeader(postMethod, headersMap);
        //设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        //设置请求重试机制，默认重试次数：3次，参数设置为true，重试机制可用，false相反
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, true));
        setApacheHttpClient3PostPrams(postMethod, headersMap);
        try {
            //执行POST方法
            int statusCode = httpClient.executeMethod(postMethod);
            //判断是否成功
            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + postMethod.getStatusLine());
            } else {
                result = postMethod.getResponseBodyAsString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放连接
        postMethod.releaseConnection();
        return result;
    }

    /**
     * 拼接httpGet请求地址
     *
     * @param httpUrl   httpGet请求地址
     * @param paramsMap httpGet请求参数
     * @return 拼接后的地址
     */
    private static String setApacheHttpClient3GetUrl(String httpUrl, Map<String, String> paramsMap) {
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
     * @param postMethod {@link PostMethod}
     * @param paramsMap  httpPost请求参数
     */
    private static void setApacheHttpClient3PostPrams(PostMethod postMethod, Map<String, String> paramsMap) {
        NameValuePair[] nvp = null;
        //判断参数map集合paramMap是否为空
        if (paramsMap != null && paramsMap.size() > 0) {
            //lambda获取参数数组表示
            nvp = paramsMap.keySet().stream()
                    .map(key -> new NameValuePair(key, paramsMap.get(key)))
                    .toArray(NameValuePair[]::new);
        }
        //判断nvp数组是否为空
        if (nvp != null && nvp.length > 0) {
            //将参数存放到requestBody对象中
            postMethod.setRequestBody(nvp);
        }
    }

    /**
     * 设置httpGet请求头
     *
     * @param getMethod  {@link GetMethod}
     * @param headersMap httpGet请求头
     */
    private static void setApacheHttpClient3GetRequestHeader(GetMethod getMethod, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                getMethod.setRequestHeader(key, headersMap.get(key));
        }
    }

    /**
     * 设置httpPost请求头
     *
     * @param postMethod {@link PostMethod}
     * @param headersMap httpPost请求头
     */
    private static void setApacheHttpClient3PostRequestHeader(PostMethod postMethod, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                postMethod.setRequestHeader(key, headersMap.get(key));
        }
    }

//==============================================================apache httpClient4================================================================

    public static String apacheHttpClient4DoGet(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        try {
            //通过默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            //创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(httpUrl);
            //设置请求头信息，鉴权
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            //设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom()
                    //连接主机服务超时时间
                    .setConnectTimeout(35000)
                    //请求超时时间
                    .setConnectionRequestTimeout(35000)
                    //数据读取超时时间
                    .setSocketTimeout(60000)
                    .build();
            //为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            //执行get请求得到返回对象
            httpResponse = httpClient.execute(httpGet);
            //通过返回对象获取返回数据
            HttpEntity entity = httpResponse.getEntity();
            //通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResource(httpResponse, httpClient);
        }
        return result;
    }


    public static String apacheHttpClient4DoPost(String httpUrl, Map<String, String> paramsMap, Map<String, String> headersMap) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        //创建httpClient实例
        httpClient = HttpClients.createDefault();
        //创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(httpUrl);
        //设置请求头
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom()
                //设置连接主机服务超时时间
                .setConnectTimeout(35000)
                //设置连接请求超时时间
                .setConnectionRequestTimeout(35000)
                //设置读取数据连接超时时间
                .setSocketTimeout(60000)
                .build();
        //为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 封装post请求参数
        try {
            if (paramsMap != null && paramsMap.size() > 0) {
                // 通过map集成entrySet方法获取entity
                // 循环遍历，获取迭代器
                List<org.apache.http.NameValuePair> nvps = paramsMap.keySet().stream()
                        .map(key -> new BasicNameValuePair(key, paramsMap.get(key)))
                        .collect(Collectors.toList());
                // 为httpPost设置封装好的请求参数
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            }
            //httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            //从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            closeResource(httpResponse, httpClient);
        }
        return result;
    }

    private static String setApacheHttpClient4GetUrl(String httpUrl, Map<String, String> paramsMap) {
        if (paramsMap != null && paramsMap.size() > 0) {
            httpUrl += paramsMap.keySet().stream()
                    .map(key -> key + "=" + paramsMap.get(key) + "&")
                    .reduce("?", (x, y) -> x + y);
            httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
        }
        return httpUrl;
    }

    private static void setApacheHttpClient4GetRequestHeader(HttpGet httpGet, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                httpGet.addHeader(key, headersMap.get(key));
        }
    }

    private static void setApacheHttpClient4PostRequestHeader(HttpPost httpPost, Map<String, String> headersMap) {
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet())
                httpPost.addHeader(key, headersMap.get(key));
        }
    }

    private static void closeResource(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient) {
        if (httpResponse != null) {
            try {
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
