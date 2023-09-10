package io.github.comicmq.config;

import io.github.comicmq.util.idgenerator.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pigeoncat
 * @Date 2023/09/05  17:27
 * @TODO description
 */
@Configuration
public class IdWorkerConfig {

    @Bean
    public SnowflakeIdGenerator idWorker(){
        return SnowflakeIdGenerator.getSingletonIdGenerator(2,2);
    }

}
