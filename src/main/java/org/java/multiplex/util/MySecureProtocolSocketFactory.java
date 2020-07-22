package org.java.multiplex.util;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

/**
 * @author wangpeng
 * @version 1.0
 * @description https相关
 * @create_time 2020/7/17 10:59:44
 */
public class MySecureProtocolSocketFactory implements SecureProtocolSocketFactory {

    //这里添加一个属性，主要目的就是来获取ssl跳过验证
    private SSLContext sslContext = null;

    //这个创建一个获取SSLContext的方法，导入MyX509TrustManager进行初始化
    private static SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{new MyX509TrustManager()},
                    new SecureRandom());
            return context;
        } catch (Exception e) {
            throw new HttpClientError(e.toString());
        }
    }

    //判断获取SSLContext
    private SSLContext getSSLContext() {
        if (this.sslContext == null) {
            this.sslContext = createEasySSLContext();
        }
        return this.sslContext;
    }

    //后面的方法基本上就是带入相关参数就可以了
    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        } else {
            return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }
}
