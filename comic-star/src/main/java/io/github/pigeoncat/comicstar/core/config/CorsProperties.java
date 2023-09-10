package io.github.pigeoncat.comicstar.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 跨域配置属性
 *
 */
@ConfigurationProperties(prefix = "comicstar.cors")
public record CorsProperties(List<String> allowOrigins) {

}
