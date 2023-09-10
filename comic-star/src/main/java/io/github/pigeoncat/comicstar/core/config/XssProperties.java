package io.github.pigeoncat.comicstar.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Xss 过滤配置属性
 *
 */
@ConfigurationProperties(prefix = "comicstar.xss")
public record XssProperties(Boolean enabled, List<String> excludes) {

}
