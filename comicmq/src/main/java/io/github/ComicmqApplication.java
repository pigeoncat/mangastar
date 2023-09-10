package io.github;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("io.github.spidercommon.mapper")
public class ComicmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicmqApplication.class, args);
        System.out.println("\u001B[32m................消息队列模块启动................\u001B[0m");
        System.out.println("\u001B[32m---------------启动ES搜索引擎模块--------------\u001B[0m");
        System.out.println("\u001B[32m--------------启动MySql数据库模块--------------\u001B[0m");
    }

}
