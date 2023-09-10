package io.github.spider.util;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  09:10
 * @TODO description
 */
public class HttpClientUtils {
    //连接池最大连接数
    private static final int MAX_TOTAL_CONNECTIONS = Runtime.getRuntime().availableProcessors();
    //每个路由的最大连接数
    private static final int MAX_CONNECTIONS_PER_ROUTE = Runtime.getRuntime().availableProcessors();
    //如果多个请求的目标主机、端口和协议都相同，那么它们就可以被认为是属于同一个路由
    //如下路由相同：
    //请求1：https://www.example.com:443
    //请求2：https://www.example.com:443/index.html

    private static final CloseableHttpClient httpClient;

    static {
        // 创建连接池管理器
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        connectionManager.setDefaultMaxPerRoute(MAX_CONNECTIONS_PER_ROUTE);

        // 创建 HttpClient 对象
         httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();
    }


    private HttpClientUtils() {
    }

    public static HttpClient getHttpClient(){
        return httpClient;
    }

}
