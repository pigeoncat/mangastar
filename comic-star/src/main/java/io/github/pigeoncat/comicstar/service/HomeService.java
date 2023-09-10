package io.github.pigeoncat.comicstar.service;


import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.dto.resp.HomeComicRespDto;
import io.github.pigeoncat.comicstar.dto.resp.HomeFriendLinkRespDto;

import java.util.List;

/**
 * 首页模块 服务类
 *
 */
public interface HomeService {

    /**
     * 查询首页漫画推荐列表
     *
     * @return 首页漫画推荐列表的 rest 响应结果
     */
    RestResp<List<HomeComicRespDto>> listHomeComics();

    /**
     * 首页友情链接列表查询
     *
     * @return 友情链接列表
     */
    RestResp<List<HomeFriendLinkRespDto>> listHomeFriendLinks();
}
