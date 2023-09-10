package io.github.pigeoncat.comicstar.controller.front;


import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.dto.req.ComicSearchReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import io.github.pigeoncat.comicstar.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台门户-搜索模块 API 控制器
 *
 */
@Tag(name = "SearchController", description = "前台门户-搜索模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_SEARCH_URL_PREFIX)
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 漫画搜索接口
     */
    @Operation(summary = "漫画搜索接口")
    @GetMapping("comics")
    public RestResp<PageRespDto<ComicInfoRespDto>> searchComics(
        @ParameterObject ComicSearchReqDto condition) {
        return searchService.searchComics(condition);
    }

}
