package io.github.spider.config;

import io.github.spider.util.idgenerator.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pigeoncat
 * @Date 2023/09/05  20:42
 * @TODO description
 */
@Configuration
public class IdWorkerConfig {

    @Bean
    public SnowflakeIdGenerator idWorker(){
        return SnowflakeIdGenerator.getSingletonIdGenerator(1,1);
    }

}
