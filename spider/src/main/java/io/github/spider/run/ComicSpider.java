package io.github.spider.run;

import io.github.spider.website.ISpider;
import io.github.spider.website.tencentcomic.TencentFreeComicSpider;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  23:37
 * @TODO description
 */
public class ComicSpider {
    public static void run(ISpider spider){
        System.out.println("\u001B[32m爬虫启动.......\u001B[0m");//绿色文字显示
        spider.doSpider();
    }
}
