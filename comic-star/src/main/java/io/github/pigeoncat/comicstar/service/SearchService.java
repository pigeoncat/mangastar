package io.github.pigeoncat.comicstar.service;


import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.dto.req.ComicSearchReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;


/**
 * 搜索 服务类
 *
 */
public interface SearchService {

    /**
     * 漫画搜索
     *
     * @param condition 搜索条件
     * @return 搜索结果
     */
    RestResp<PageRespDto<ComicInfoRespDto>> searchComics(ComicSearchReqDto condition);

}
