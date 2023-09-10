package io.github;

import io.github.spider.run.ComicSpider;
import io.github.spider.website.tencentcomic.TencentFreeComicSpider;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("io.github.spidercommon.mapper")
public class SpiderApplication {

    @Autowired
    private TencentFreeComicSpider tencentFreeComicSpider;

    public static void main(String[] args) {
        //容器初始化
        ConfigurableApplicationContext ctx = SpringApplication.run(SpiderApplication.class, args);
        //容器初始化完成
    }

    //spring容器初始化之后再开启爬虫，保证所有依赖bean都导入容器完毕
    @PostConstruct
    public void startSpider() {
        // 开启爬虫
        ComicSpider.run(tencentFreeComicSpider);
    }

}
