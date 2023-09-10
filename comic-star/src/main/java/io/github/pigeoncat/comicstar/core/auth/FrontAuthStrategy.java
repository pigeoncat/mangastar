package io.github.pigeoncat.comicstar.core.auth;


import io.github.pigeoncat.comicstar.core.common.exception.BusinessException;
import io.github.pigeoncat.comicstar.core.util.JwtUtils;
import io.github.pigeoncat.comicstar.manager.cache.UserInfoCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 前台门户系统 认证授权策略
 *
 */
@Component
@RequiredArgsConstructor
public class FrontAuthStrategy implements AuthStrategy {

    private final JwtUtils jwtUtils;

    private final UserInfoCacheManager userInfoCacheManager;

    @Override
    public void auth(String token, String requestUri) throws BusinessException {
        // 统一账号认证
        authSSO(jwtUtils, userInfoCacheManager, token);
    }

}