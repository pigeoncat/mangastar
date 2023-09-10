package io.github.pigeoncat.comicstar.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author pigeoncat
 * @Date 2023/09/01  17:40
 * @TODO description
 */

@Configuration
public class RedissonConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.121.121:6379")
                .setPassword("root");
        return Redisson.create(config);
    }
}
