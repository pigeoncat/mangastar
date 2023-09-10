package io.github.pigeoncat.comicstar.service.impl;

import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.dto.resp.HomeComicRespDto;
import io.github.pigeoncat.comicstar.dto.resp.HomeFriendLinkRespDto;
import io.github.pigeoncat.comicstar.manager.cache.FriendLinkCacheManager;
import io.github.pigeoncat.comicstar.manager.cache.HomeComicCacheManager;
import io.github.pigeoncat.comicstar.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 首页模块 服务实现类
 *
 */
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeComicCacheManager homeComicCacheManager;

    private final FriendLinkCacheManager friendLinkCacheManager;

    @Override
    public RestResp<List<HomeComicRespDto>> listHomeComics() {
        return RestResp.ok(homeComicCacheManager.listHomeComics());
    }

    @Override
    public RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks() {
        return RestResp.ok(friendLinkCacheManager.listFriendLinks());
    }
}
