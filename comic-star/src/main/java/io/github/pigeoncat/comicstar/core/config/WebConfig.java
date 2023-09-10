package io.github.pigeoncat.comicstar.core.config;


import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.core.constant.SystemConfigConsts;
import io.github.pigeoncat.comicstar.core.interceptor.AuthInterceptor;
import io.github.pigeoncat.comicstar.core.interceptor.FileInterceptor;
import io.github.pigeoncat.comicstar.core.interceptor.FlowLimitInterceptor;
import io.github.pigeoncat.comicstar.core.interceptor.TokenParseInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web Mvc 相关配置不要加 @EnableWebMvc 注解，否则会导致 jackson 的全局配置失效。因为 @EnableWebMvc 注解会导致
 * WebMvcAutoConfiguration 自动配置失效
 *
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FlowLimitInterceptor flowLimitInterceptor;

    private final AuthInterceptor authInterceptor;

    private final FileInterceptor fileInterceptor;

    private final TokenParseInterceptor tokenParseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 流量限制拦截器
        registry.addInterceptor(flowLimitInterceptor)
            .addPathPatterns("/**")
            .order(0);

        // 文件访问拦截
        registry.addInterceptor(fileInterceptor)
            .addPathPatterns(SystemConfigConsts.IMAGE_UPLOAD_DIRECTORY + "**")
            .order(1);

        // 权限认证拦截
        registry.addInterceptor(authInterceptor)
            // 拦截会员中心相关请求接口
            .addPathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/**",
                // 拦截作家后台相关请求接口
                ApiRouterConsts.API_AUTHOR_URL_PREFIX + "/**",
                // 拦截平台后台相关请求接口
                ApiRouterConsts.API_ADMIN_URL_PREFIX + "/**")
            // 放行登录注册相关请求接口
            .excludePathPatterns(ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/register",
                ApiRouterConsts.API_FRONT_USER_URL_PREFIX + "/login",
                ApiRouterConsts.API_ADMIN_URL_PREFIX + "/login")
            .order(2);

        // Token 解析拦截器
        registry.addInterceptor(tokenParseInterceptor)
            // 拦截漫画内容查询接口，需要解析 token 以判断该用户是否有权阅读该章节（付费章节是否已购买）
            .addPathPatterns(ApiRouterConsts.API_FRONT_COMIC_URL_PREFIX + "/content/*")
            .order(3);

    }
}
