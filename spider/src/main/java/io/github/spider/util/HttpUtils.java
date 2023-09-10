package io.github.spider.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  09:16
 * @TODO description
 */
public class HttpUtils {

    private static final int CONNECTION_TIMEOUT = 5000; // 连接超时时间
    private static final int SOCKET_TIMEOUT = 5000; // 读取数据超时时间


    private HttpUtils() {
    }


    public static HttpGet createHttpGet(){
        HttpGet httpGet0 = new HttpGet();
        setHttpGetHeaders(httpGet0);
        return httpGet0;
    }


    private static void setHttpGetHeaders(HttpGet httpGet){
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        // 配置超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();
        httpGet.setConfig(requestConfig);
    }

}
