package io.github.spider.run.test;

import io.github.spider.util.HttpClientUtils;
import io.github.spider.util.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

/**
 * @Author pigeoncat
 * @Date 2023/08/27  23:13
 * @TODO description
 */
public class HttpSpiderTest01 {
    public static void main(String[] args) throws IOException {

//        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpClient httpClient = HttpClientUtils.getHttpClient();


        // 设置目标 URL
        String url = "https://ac.qq.com/Comic/all/search/hot/vip/1/page/1";

        // 创建 HttpGet 请求
        HttpGet httpGet = HttpUtils.createHttpGet();
        httpGet.setURI(URI.create(url));


        // 发送请求并获取响应
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);
        
        // 获取响应实体
        HttpEntity entity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
            
            // 解析响应实体
            String responseBody = EntityUtils.toString(entity);
//            System.out.println(responseBody);

            Document doc = Jsoup.parse(responseBody);
            //获得li集合
            Elements lis = doc.selectXpath("//div[@class=\"ret-search-result\"]/ul/li");

            //条目信息
            lis.forEach(li->{
                //个别xpath没解析出来，换成css选择器
                //封面url
                String coverUrl = li.select("div.ret-works-cover > a > img").attr("data-original");
                //漫画详情url后缀
                String comicDetailSuffix = li.select("div.ret-works-cover > a").attr("href");
                //漫画名称
                String comicName = li.select("div.ret-works-cover > a").attr("title");

                //更新至几话或全多少话
                String chapterCount = li.selectXpath("./div[@class=\"ret-works-cover\"]//span[@class=\"mod-cover-list-text\"]").text();
                //签约
                String comicSign = li.selectXpath("./div[@class=\"ret-works-info\"]/h3/i[@class=\"ui-icon-sign\"]").text();
                //独家
                String comicExclusive = li.selectXpath("./div[@class=\"ret-works-info\"]/h3/i[@class=\"ui-icon-exclusive\"]").text();
                //漫画作者--笔名
                String author = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-author\"]").text();
                //作品标签，热血、人气、妖怪....
                Elements comicTagsEl = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-tags\"]/span[@target=\"_blank\"]");
                ArrayList<String> comicTags = new ArrayList<>();
                comicTagsEl.forEach(tag->comicTags.add(tag.text()));
                //人气---多少多少亿或者万
                String popularity = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-tags\"]//em").text();
                //漫画描述
                String comicDesc = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-decs\"]").text();


                System.out.println(coverUrl);
                System.out.println(chapterCount);
                System.out.println(comicDetailSuffix);
                System.out.println(comicName);
                System.out.println(comicSign);
                System.out.println(comicExclusive);
                System.out.println(author);
                System.out.println(comicTags);
                System.out.println(popularity);
                System.out.println(comicDesc);
                System.out.println("*******************************************************");

            });

//            System.out.println(lis.get(0));





        }else {
            System.out.println("请求失败");
        }

    }
}
