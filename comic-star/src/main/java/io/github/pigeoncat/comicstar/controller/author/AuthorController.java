package io.github.pigeoncat.comicstar.controller.author;

import io.github.pigeoncat.comicstar.core.auth.UserHolder;
import io.github.pigeoncat.comicstar.core.common.req.PageReqDto;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.core.constant.SystemConfigConsts;
import io.github.pigeoncat.comicstar.dto.req.AuthorRegisterReqDto;
import io.github.pigeoncat.comicstar.dto.req.ComicAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.ChapterAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.ChapterUpdateReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicChapterRespDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import io.github.pigeoncat.comicstar.service.AuthorService;
import io.github.pigeoncat.comicstar.service.ComicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

/**
 * 作家后台-作家模块 API 控制器
 *
 */
@Tag(name = "AuthorController", description = "作家后台-作者模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_AUTHOR_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private final ComicService comicService;

    /**
     * 作家注册接口
     */
    @Operation(summary = "作家注册接口")
    @PostMapping("register")
    public RestResp<Void> register(@Valid @RequestBody AuthorRegisterReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return authorService.register(dto);
    }

    /**
     * 查询作家状态接口
     */
    @Operation(summary = "作家状态查询接口")
    @GetMapping("status")
    public RestResp<Integer> getStatus() {
        return authorService.getStatus(UserHolder.getUserId());
    }

    /**
     * 漫画发布接口
     */
    @Operation(summary = "漫画发布接口")
    @PostMapping("comic")
    public RestResp<Void> publishComic(@Valid @RequestBody ComicAddReqDto dto) {
        return comicService.saveComic(dto);
    }

    /**
     * 漫画发布列表查询接口
     */
    @Operation(summary = "漫画发布列表查询接口")
    @GetMapping("comics")
    public RestResp<PageRespDto<ComicInfoRespDto>> listComics(@ParameterObject PageReqDto dto) {
        return comicService.listAuthorComics(dto);
    }

    /**
     * 漫画章节发布接口
     */
    @Operation(summary = "漫画章节发布接口")
    @PostMapping("comic/chapter/{comicId}")
    public RestResp<Void> publishComicChapter(
        @Parameter(description = "漫画ID") @PathVariable("comicId") Long comicId,
        @Valid @RequestBody ChapterAddReqDto dto) {
        dto.setComicId(comicId);
        return comicService.saveComicChapter(dto);
    }

    /**
     * 漫画章节删除接口
     */
    @Operation(summary = "漫画章节删除接口")
    @DeleteMapping("comic/chapter/{chapterId}")
    public RestResp<Void> deleteComicChapter(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId) {
        return comicService.deleteComicChapter(chapterId);
    }

    /**
     * 漫画章节查询接口
     */
    @Operation(summary = "漫画章节查询接口")
    @GetMapping("comic/chapter/{comicId}/{chapterNum}")
    public RestResp<ComicChapterRespDto> getComicChapter(
        @Parameter(description = "漫画ID") @PathVariable("comicId") Long comicId,
        @Parameter(description = "章节号") @PathVariable("chapterNum") Integer chapterNum
    ) {
        return comicService.getChapterAbout(comicId,chapterNum);
    }

    /**
     * 漫画章节更新接口
     */
    @Operation(summary = "漫画章节更新接口")
    @PutMapping("comic/chapter/{chapterId}")
    public RestResp<Void> updateComicChapter(
        @Parameter(description = "章节ID") @PathVariable("chapterId") Long chapterId,
        @Valid @RequestBody ChapterUpdateReqDto dto) {
        return comicService.updateComicChapter(chapterId, dto);
    }

    /**
     * 漫画章节发布列表查询接口
     */
    @Operation(summary = "漫画章节发布列表查询接口")
    @GetMapping("comic/chapters/{comicId}")
    public RestResp<PageRespDto<ComicChapterRespDto>> listComicChapters(
        @Parameter(description = "漫画ID") @PathVariable("comicId") Long comicId,
        @ParameterObject PageReqDto dto) {
        return comicService.listComicChapters(comicId, dto);
    }

}
