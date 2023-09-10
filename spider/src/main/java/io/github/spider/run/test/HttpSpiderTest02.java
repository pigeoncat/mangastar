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

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  15:38
 * @TODO description
 */
public class HttpSpiderTest02 {

    public static void main(String[] args) throws URISyntaxException, IOException {

        HttpClient httpClient = HttpClientUtils.getHttpClient();

        HttpGet httpGet = HttpUtils.createHttpGet();
//        httpGet.setURI(new URI("https://ac.qq.com/Comic/comicInfo/id/545320"));
        httpGet.setURI(new URI("https://ac.qq.com/Comic/comicInfo/id/654079"));


        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        System.err.println(response.getStatusLine().getStatusCode());
        System.out.println("*************************************");

        if (response.getStatusLine().getStatusCode() == 200 && entity != null) {
            Document doc = Jsoup.parse(EntityUtils.toString(entity));
//            System.out.println(doc);

            //起始章节
            String chapterStart = doc.select("div.works-chapter-list-wr.ui-left > ol.works-chapter-list > li > p > span.works-chapter-item > a").attr("href");
            //漫画详情页章节展示目录展示的最后一条
            String lastNumInChapterList = doc.select("div.works-chapter-list-wr.ui-left > ol.works-chapter-list > li > p:last-child > span:last-child > a").attr("href");

            String[]split = chapterStart.split("/");
            String chapterStartNum = split[split.length - 1];
            System.out.println(chapterStartNum);
            System.out.println(chapterStart);
            System.out.println(lastNumInChapterList);
            //显示的所有章节
//            Elements allChapter = doc.select("div.chapter-page-pager > div.chapter-page-more > a");
//            System.out.println(allChapter);

            System.out.println("*******************************阅读页面***********************************");
            //阅读页面(所有章节都在这里，章节目录包含了所有章节li标签)
            String readPageUrl = "https://ac.qq.com/ComicView/index/id/" + split[split.length - 3] + "/cid/" + split[split.length - 1];
            httpGet.setURI(URI.create(readPageUrl));
            HttpEntity entity1 = httpClient.execute(httpGet).getEntity();
            String html = EntityUtils.toString(entity1);
            Document doc1 = Jsoup.parse(html);
            Elements elements = doc1.selectXpath("//ul[@id=\"catalogueList\"]/li");
            System.out.println(elements);

        } else {
            System.out.println("失败。。。。");
        }


    }


}
