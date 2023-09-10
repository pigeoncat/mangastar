package io.github.pigeoncat.comicstar.core.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pigeoncat.comicstar.core.auth.AuthStrategy;
import io.github.pigeoncat.comicstar.core.auth.UserHolder;
import io.github.pigeoncat.comicstar.core.common.exception.BusinessException;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.core.constant.SystemConfigConsts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 认证授权 拦截器：为了注入其它的 Spring beans，需要通过 @Component 注解将该拦截器注册到 Spring 上下文
 *
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final Map<String, AuthStrategy> authStrategy;

    private final ObjectMapper objectMapper;

    /**
     * handle 执行前调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        // 获取登录 JWT
        String token = request.getHeader(SystemConfigConsts.HTTP_AUTH_HEADER_NAME);

        // 获取请求的 URI
        String requestUri = request.getRequestURI();

        // 根据请求的 URI 得到认证策略
        String subUri = requestUri.substring(ApiRouterConsts.API_URL_PREFIX.length() + 1);
        String systemName = subUri.substring(0, subUri.indexOf("/"));
        String authStrategyName = String.format("%sAuthStrategy", systemName);

        // 开始认证----失败会抛出异常
        try {
            authStrategy.get(authStrategyName).auth(token, requestUri);
            return HandlerInterceptor.super.preHandle(request, response, handler);
        } catch (BusinessException exception) {
            // 认证失败
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                objectMapper.writeValueAsString(RestResp.fail(exception.getErrorCodeEnum())));
            return false;
        }
    }

    /**
     * handler 执行后调用，出现异常不调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * DispatcherServlet 完全处理完请求后调用，出现异常照常调用
     */
    @SuppressWarnings("NullableProblems")
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        // 清理当前线程保存的用户数据
        UserHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
