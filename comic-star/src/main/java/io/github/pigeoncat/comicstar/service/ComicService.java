package io.github.pigeoncat.comicstar.service;


import io.github.pigeoncat.comicstar.core.common.req.PageReqDto;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.dto.req.ChapterAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.ChapterUpdateReqDto;
import io.github.pigeoncat.comicstar.dto.req.ComicAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.UserCommentReqDto;
import io.github.pigeoncat.comicstar.dto.resp.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 漫画模块 服务类
 *
 */
public interface ComicService {

    /**
     * 漫画点击榜查询
     *
     * @return 漫画点击排行列表
     */
    RestResp<List<ComicItemRespDto>> listVisitRankComics();

    /**
     * 漫画新书榜查询
     *
     * @return 漫画新书排行列表
     */
    RestResp<List<ComicItemRespDto>> listNewestRankComics();

    /**
     * 漫画更新榜查询
     *
     * @return 漫画更新排行列表
     */
    RestResp<List<ComicItemRespDto>> listUpdateRankComics();

    /**
     * 根据漫画id查询漫画信息
     *
     * @param comicId 漫画ID
     * @return 漫画信息
     */
    RestResp<ComicInfoRespDto> getComicById(Long comicId);


    /**
     * 查询漫画章节信息
     * @param comicId
     * @param chapterNum
     * @return
     */
    RestResp<ComicChapterRespDto> getChapterAbout(Long comicId,Integer chapterNum);


    /**
     * 漫画最新章节相关信息查询
     *
     * @param comicId 漫画ID
     * @return 章节相关联的信息
     */
    RestResp<ComicChapterRespDto> getLastChapterAbout(Long comicId);

    /**
     * 漫画推荐列表查询
     * 通过漫画id，查找相同类型的漫画
     * @param comicId 漫画ID
     * @return 漫画信息列表
     */
    RestResp<List<ComicItemRespDto>> listRecComics(Long comicId) throws NoSuchAlgorithmException;

    /**
     * 增加漫画点击量
     *
     * @param comicId 漫画ID
     * @return 成功状态
     */
    RestResp<Void> addVisitCount(Long comicId);

    /**
     * 获取上一章节ID
     *
     * @param chapterId 章节ID
     * @return 上一章节ID
     */
    RestResp<Long> getPreChapterId(Long chapterId);

    /**
     * 获取下一章节ID
     *
     * @param chapterId 章节ID
     * @return 下一章节ID
     */
    RestResp<Long> getNextChapterId(Long chapterId);

    /**
     * 漫画章节列表查询
     *
     * @param comicId 漫画ID
     * @return 漫画章节列表
     */
    RestResp<List<ComicChapterRespDto>> listChapters(Long comicId);

    /**
     * 查询漫画第一章节
     * @param comicId
     * @return
     */
    RestResp<ComicChapterRespDto> getFirstChapter(Long comicId);

    /**
     * 漫画分类列表查询
     *
     * @param workDirection 作品方向;0-男频 1-女频
     * @return 分类列表
     */
    RestResp<List<ComicCategoryRespDto>> listCategory(Integer workDirection);

    /**
     * 漫画章节删除
     *
     * @param chapterId 章节ID
     * @return void
     */
    RestResp<Void> deleteComicChapter(Long chapterId);


    /**
     * 漫画章节更新
     *
     * @param chapterId 章节ID
     * @param dto       更新内容
     * @return void
     */
    RestResp<Void> updateComicChapter(Long chapterId, ChapterUpdateReqDto dto);


    /**
     * 漫画信息保存
     *
     * @param dto 漫画信息
     * @return void
     */
    RestResp<Void> saveComic(ComicAddReqDto dto);

    /**
     * 漫画章节信息保存
     *
     * @param dto 章节信息
     * @return void
     */
    RestResp<Void> saveComicChapter(ChapterAddReqDto dto);

    /**
     * 查询作家发布漫画列表
     *
     * @param dto 分页请求参数
     * @return 漫画分页列表数据
     */
    RestResp<PageRespDto<ComicInfoRespDto>> listAuthorComics(PageReqDto dto);

    /**
     * 查询漫画发布章节列表
     *
     * @param comicId 漫画ID
     * @param dto    分页请求参数
     * @return 章节分页列表数据
     */
    RestResp<PageRespDto<ComicChapterRespDto>> listComicChapters(Long comicId, PageReqDto dto);



    /**
     * 发表评论
     *
     * @param dto 评论相关 DTO
     * @return void
     */
    RestResp<Void> saveComment(UserCommentReqDto dto);

    /**
     * 最新评论查询
     *
     * @param comicId 漫画ID
     * @return 漫画最新评论数据
     */
    RestResp<ComicCommentRespDto> listNewestComments(Long comicId);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return void
     */
    RestResp<Void> deleteComment(Long commentId);

    /**
     * 修改评论
     *
     * @param commentId      评论ID
     * @param content 修改后的评论内容
     * @return void
     */
    RestResp<Void> updateComment(Long commentId, String content);


    /**
     * 分页查询评论
     *
     * @param userId     会员ID
     * @param pageReqDto 分页参数
     * @return 评论分页列表数据
     */
    RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto);

    /**
     * 根据章节id查询章节信息
     * @param chapterId
     * @return
     */
    RestResp<ComicChapterRespDto> getChapterContent(Long chapterId);
}
