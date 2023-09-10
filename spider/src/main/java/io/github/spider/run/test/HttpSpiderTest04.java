package io.github.spider.run.test;

import io.github.spider.util.HttpClientUtils;
import io.github.spider.util.HttpUtils;
import io.github.spider.util.tencentutil.TencentDataParseUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  15:38
 * @TODO description
 */
public class HttpSpiderTest04 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        HttpClient httpClient = HttpClientUtils.getHttpClient();

        HttpGet httpGet = HttpUtils.createHttpGet();
        httpGet.setURI(new URI("https://ac.qq.com/ComicView/index/id/545320/cid/4"));

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
            String doc = EntityUtils.toString(entity);
            String varData = TencentDataParseUtils.getVarData(doc);
            String data = TencentDataParseUtils.base64Decode(varData);

            System.out.println(TencentDataParseUtils.getChapterId(data));
            System.out.println(TencentDataParseUtils.getChapterName(data));
            System.out.println(TencentDataParseUtils.getChapterSeq(data));
            TencentDataParseUtils.getChapterPictures(data).forEach(System.out::println);

        }else {
            System.out.println("失败。。。。");
        }


    }


}
