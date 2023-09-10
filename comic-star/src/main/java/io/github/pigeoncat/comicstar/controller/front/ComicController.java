package io.github.pigeoncat.comicstar.controller.front;


import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.dto.resp.*;
import io.github.pigeoncat.comicstar.service.ComicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 前台门户-漫画模块 API 控制器
 *
 */
@Tag(name = "ComicController", description = "前台门户-漫画模块")
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_COMIC_URL_PREFIX)
@RequiredArgsConstructor
public class ComicController {

    private final ComicService comicService;

    /**
     * 漫画分类列表查询接口
     */
    @Operation(summary = "漫画分类列表查询接口")
    @GetMapping("category/list")
    public RestResp<List<ComicCategoryRespDto>> listCategory(
        @Parameter(description = "作品方向", required = true) Integer workDirection) {
        return comicService.listCategory(workDirection);
    }

    /**
     * 根据漫画id查询漫画信息
     */
    @Operation(summary = "漫画信息查询接口")
    @GetMapping("{id}")
    public RestResp<ComicInfoRespDto> getComicById(
        @Parameter(description = "漫画 ID") @PathVariable("id") Long comicId) {
        return comicService.getComicById(comicId);
    }

    /**
     * 增加漫画点击量接口
     */
    @Operation(summary = "增加漫画点击量接口")
    @PostMapping("visit")
    public RestResp<Void> addVisitCount(@Parameter(description = "漫画ID") Long comicId) {
        return comicService.addVisitCount(comicId);
    }

    /**
     * 漫画章节信息查询接口
     */
    @Operation(summary = "漫画章节信息查询接口")
    @GetMapping("chapter/about/{comicId}/{chapterNum}")
    public RestResp<ComicChapterRespDto> getChapterAbout(
            @Parameter(description = "漫画ID") @PathVariable("chapterId") Long comicId,
            @Parameter(description = "章节号") @PathVariable("chapterNum") Integer chapterNum
    ) {
        return comicService.getChapterAbout(comicId,chapterNum);
    }

    /**
     * 根据章节id查询漫画章节内容
     */
    @Operation(summary = "根据章节id查询漫画章节内容")
    @GetMapping("content/{chapterId}")
    public RestResp<ComicChapterRespDto> getChapterContent(
            @Parameter(description = "章节id") @PathVariable("chapterId") Long chapterId
    ) {
        return comicService.getChapterContent(chapterId);
    }

    /**
     * 漫画最新章节相关信息查询接口
     */
    @Operation(summary = "漫画最新章节相关信息查询接口")
    @GetMapping("last_chapter/about")
    public RestResp<ComicChapterRespDto> getLastChapterAbout(
        @Parameter(description = "漫画ID") Long comicId) {
        return comicService.getLastChapterAbout(comicId);
    }

    /**
     * 同类型漫画推荐列表查询接口
     */
    @Operation(summary = "同类型漫画推荐列表查询接口")
    @GetMapping("rec_list")
    public RestResp<List<ComicItemRespDto>> listRecComics(
        @Parameter(description = "漫画ID") Long comicId) throws NoSuchAlgorithmException {
        return comicService.listRecComics(comicId);
    }

    /**
     * 漫画章节列表查询接口
     */
    @Operation(summary = "漫画章节列表查询接口")
    @GetMapping("chapter/list")
    public RestResp<List<ComicChapterRespDto>> listChapters(
        @Parameter(description = "漫画ID") Long comicId) {
        return comicService.listChapters(comicId);
    }

    /**
     * 查询漫画第一章节
     */
    @Operation(summary = "查询漫画第一章节")
    @GetMapping("chapter/first")
    public RestResp<ComicChapterRespDto> getFirstChapter(
            @Parameter(description = "漫画ID") Long comicId) {
        return comicService.getFirstChapter(comicId);
    }



    /**
     * 获取上一章节ID接口
     */
    @Operation(summary = "获取上一章节ID接口")
    @GetMapping("pre_chapter_id/{chapterId}")
    public RestResp<Long> getPreChapterId(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return comicService.getPreChapterId(chapterId);
    }

    /**
     * 获取下一章节ID接口
     */
    @Operation(summary = "获取下一章节ID接口")
    @GetMapping("next_chapter_id/{chapterId}")
    public RestResp<Long> getNextChapterId(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return comicService.getNextChapterId(chapterId);
    }

    /**
     * 漫画点击榜查询接口
     */
    @Operation(summary = "漫画点击榜查询接口")
    @GetMapping("visit_rank")
    public RestResp<List<ComicItemRespDto>> listVisitRankComics() {
        return comicService.listVisitRankComics();
    }

    /**
     * 漫画新书榜查询接口
     */
    @Operation(summary = "漫画新书榜查询接口")
    @GetMapping("newest_rank")
    public RestResp<List<ComicItemRespDto>> listNewestRankComics() {
        return comicService.listNewestRankComics();
    }

    /**
     * 漫画更新榜查询接口
     */
    @Operation(summary = "漫画更新榜查询接口")
    @GetMapping("update_rank")
    public RestResp<List<ComicItemRespDto>> listUpdateRankComics() {
        return comicService.listUpdateRankComics();
    }

    /**
     * 漫画最新评论查询接口
     */
    @Operation(summary = "漫画最新评论查询接口")
    @GetMapping("comment/newest_list")
    public RestResp<ComicCommentRespDto> listNewestComments(
        @Parameter(description = "漫画ID") Long comicId) {
        return comicService.listNewestComments(comicId);
    }



}
