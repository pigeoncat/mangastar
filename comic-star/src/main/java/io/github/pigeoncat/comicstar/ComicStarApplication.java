package io.github.pigeoncat.comicstar;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("io.github.pigeoncat.comicstar.dao.mapper")
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@Slf4j
public class ComicStarApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComicStarApplication.class, args);

    }

}
