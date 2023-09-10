package io.github.spider.run.test;

import io.github.spider.util.HttpClientUtils;
import io.github.spider.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  15:38
 * @TODO description
 */
public class HttpSpiderTest03 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        HttpClient httpClient = HttpClientUtils.getHttpClient();

        HttpGet httpGet = HttpUtils.createHttpGet();
        httpGet.setURI(new URI("https://ac.qq.com/ComicView/index/id/545320/cid/4"));

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        System.err.println(response.getStatusLine().getStatusCode());
        System.out.println("*************************************");

        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
            String doc = EntityUtils.toString(entity);
//            System.out.println(doc);
            String pattern = "var DATA = '(.*?)',";

            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(doc);

            if (matcher.find()) {
                String data = matcher.group(1);
//                System.out.println("漫画图片地址：" + data);
                //DATA数据尝试去掉盐然后base64解码
                String target = null;
                for (int i = 0; i < data.length(); i++) {
                    String substring = data.substring(i);
                    try {
                        byte[] decode = Base64.getDecoder().decode(substring);
                        target = new String(decode, StandardCharsets.UTF_8);
                    }catch (IllegalArgumentException e){
                        continue;
                    }
                    //解析没有出错则判断有无正确的数据，正常数据应该包含chapter
                    if (target!=null && !target.contains("chapter")){
                        continue;
                    }
                    break;
                }

                System.out.println(target);
            } else {
                System.out.println("找不到数据变量");
            }


        }else {
            System.out.println("失败。。。。");
        }


    }


}
