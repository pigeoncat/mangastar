package io.github.pigeoncat.comicstar.controller.front;

import io.github.pigeoncat.comicstar.core.auth.UserHolder;
import io.github.pigeoncat.comicstar.core.common.req.PageReqDto;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.ApiRouterConsts;
import io.github.pigeoncat.comicstar.core.constant.SystemConfigConsts;
import io.github.pigeoncat.comicstar.dto.req.UserCommentReqDto;
import io.github.pigeoncat.comicstar.dto.req.UserInfoUptReqDto;
import io.github.pigeoncat.comicstar.dto.req.UserLoginReqDto;
import io.github.pigeoncat.comicstar.dto.req.UserRegisterReqDto;
import io.github.pigeoncat.comicstar.dto.resp.UserCommentRespDto;
import io.github.pigeoncat.comicstar.dto.resp.UserInfoRespDto;
import io.github.pigeoncat.comicstar.dto.resp.UserLoginRespDto;
import io.github.pigeoncat.comicstar.dto.resp.UserRegisterRespDto;
import io.github.pigeoncat.comicstar.service.ComicService;
import io.github.pigeoncat.comicstar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 前台门户-用户模块 API 控制器
 *
 */
@Tag(name = "UserController", description = "前台门户-用户模块")
@SecurityRequirement(name = SystemConfigConsts.HTTP_AUTH_HEADER_NAME)
@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_USER_URL_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ComicService comicService;

    /**
     * 用户注册接口
     */
    @Operation(summary = "用户注册接口")
    @PostMapping("register")
    public RestResp<UserRegisterRespDto> register(@Valid @RequestBody UserRegisterReqDto dto) {
        return userService.register(dto);
    }

    /**
     * 用户登录接口
     */
    @Operation(summary = "用户登录接口")
    @PostMapping("login")
    public RestResp<UserLoginRespDto> login(@Valid @RequestBody UserLoginReqDto dto) {
        return userService.login(dto);
    }

    /**
     * 用户信息查询接口
     */
    @Operation(summary = "用户信息查询接口")
    @GetMapping
    public RestResp<UserInfoRespDto> getUserInfo() {
        return userService.getUserInfo(UserHolder.getUserId());
    }

    /**
     * 用户信息修改接口
     */
    @Operation(summary = "用户信息修改接口")
    @PutMapping
    public RestResp<Void> updateUserInfo(@Valid @RequestBody UserInfoUptReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return userService.updateUserInfo(dto);
    }

    /**
     * 用户反馈提交接口
     */
    @Operation(summary = "用户反馈提交接口")
    @PostMapping("feedback")
    public RestResp<Void> submitFeedback(@RequestBody String content) {
        return userService.saveFeedback(UserHolder.getUserId(), content);
    }

    /**
     * 用户反馈删除接口
     */
    @Operation(summary = "用户反馈删除接口")
    @DeleteMapping("feedback/{id}")
    public RestResp<Void> deleteFeedback(@Parameter(description = "反馈ID") @PathVariable Long id) {
        return userService.deleteFeedback(UserHolder.getUserId(), id);
    }

    /**
     * 发表评论接口
     */
    @Operation(summary = "发表评论接口")
    @PostMapping("comment")
    public RestResp<Void> saveComment(@Valid @RequestBody UserCommentReqDto dto) {
        dto.setUserId(UserHolder.getUserId());
        return comicService.saveComment(dto);
    }

    /**
     * 修改评论接口
     */
    @Operation(summary = "修改评论接口")
    @PutMapping("comment/{id}")
    public RestResp<Void> updateComment(@Parameter(description = "评论ID") @PathVariable Long id,
        String content) {
        return comicService.updateComment(id, content);
    }

    /**
     * 删除评论接口
     */
    @Operation(summary = "删除评论接口")
    @DeleteMapping("comment/{id}")
    public RestResp<Void> deleteComment(@Parameter(description = "评论ID") @PathVariable Long id) {
        return comicService.deleteComment(id);
    }

    /**
     * 查询书架状态接口 0-不在书架 1-已在书架
     */
    @Operation(summary = "查询书架状态接口")
    @GetMapping("bookshelf_status")
    public RestResp<Integer> getBookshelfStatus(@Parameter(description = "漫画ID") String comicId) {
        return userService.getBookshelfStatus(UserHolder.getUserId(), comicId);
    }

    /**
     * 分页查询评论
     */
    @Operation(summary = "查询会员评论列表接口")
    @GetMapping("comments")
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(PageReqDto pageReqDto) {
        return comicService.listComments(UserHolder.getUserId(), pageReqDto);
    }

}
