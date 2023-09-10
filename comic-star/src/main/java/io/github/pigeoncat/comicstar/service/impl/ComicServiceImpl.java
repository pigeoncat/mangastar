package io.github.pigeoncat.comicstar.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pigeoncat.comicstar.core.annotation.Key;
import io.github.pigeoncat.comicstar.core.annotation.Lock;
import io.github.pigeoncat.comicstar.core.auth.UserHolder;
import io.github.pigeoncat.comicstar.core.common.constant.ErrorCodeEnum;
import io.github.pigeoncat.comicstar.core.common.req.PageReqDto;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.*;
import io.github.pigeoncat.comicstar.dao.mapper.ComicChapterMapper;
import io.github.pigeoncat.comicstar.dao.mapper.ComicChapterPictureMapper;
import io.github.pigeoncat.comicstar.dao.mapper.ComicCommentMapper;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dto.AuthorInfoDto;
import io.github.pigeoncat.comicstar.dto.req.ChapterAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.ChapterUpdateReqDto;
import io.github.pigeoncat.comicstar.dto.req.ComicAddReqDto;
import io.github.pigeoncat.comicstar.dto.req.UserCommentReqDto;
import io.github.pigeoncat.comicstar.dto.resp.*;
import io.github.pigeoncat.comicstar.manager.cache.*;
import io.github.pigeoncat.comicstar.manager.dao.UserDaoManager;
import io.github.pigeoncat.comicstar.manager.mq.AmqpMsgManager;
import io.github.pigeoncat.comicstar.service.ComicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 漫画模块 服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComicServiceImpl implements ComicService {

    //缓存管理器相关
    private final ComicCategoryCacheManager comicCategoryCacheManager;

    private final ComicItemCacheManager comicItemCacheManager;

    private final ComicRankCacheManager comicRankCacheManager;

    private final ComicInfoCacheManager comicInfoCacheManager;

    private final ComicChapterCacheManager comicChapterCacheManager;

    private final AuthorInfoCacheManager authorInfoCacheManager;

    //数据库相关
    private final ComicInfoMapper comicInfoMapper;

    private final ComicChapterMapper comicChapterMapper;

    private final ComicChapterPictureMapper comicChapterPictureMapper;

    private final ComicCommentMapper comicCommentMapper;


    //其他管理器
    private final UserDaoManager userDaoManager;

    private final AmqpMsgManager amqpMsgManager;

    private static final Integer REC_COMIC_COUNT = 4;

    /**
     * 漫画点击榜查询接口
     */
    @Override
    public RestResp<List<ComicItemRespDto>> listVisitRankComics() {
        return RestResp.ok(comicRankCacheManager.listVisitRankComics());
    }

    /**
     * 漫画新书榜查询接口
     */
    @Override
    public RestResp<List<ComicItemRespDto>> listNewestRankComics() {
        return RestResp.ok(comicRankCacheManager.listNewestRankComics());
    }

    /**
     * 漫画更新榜查询接口
     */
    @Override
    public RestResp<List<ComicItemRespDto>> listUpdateRankComics() {
        return RestResp.ok(comicRankCacheManager.listUpdateRankComics());
    }

    /**
     * 根据漫画id查询漫画信息
     */
    @Override
    public RestResp<ComicInfoRespDto> getComicById(Long comicId) {
        return RestResp.ok(comicInfoCacheManager.getComicInfo(comicId));
    }

    /**
     * 查询漫画章节信息
     */
    @Override
    public RestResp<ComicChapterRespDto> getChapterAbout(Long comicId, Integer chapterNum) {
        return RestResp.ok(comicChapterCacheManager.getChapter(comicId, chapterNum));
    }

    /**
     * 根据章节id查询章节信息
     * @param chapterId
     * @return
     */
    @Override
    public RestResp<ComicChapterRespDto> getChapterContent(Long chapterId) {
        return RestResp.ok(comicChapterCacheManager.getChapter(chapterId));
    }


    /**
     * 漫画最新章节信息查询接口
     */
    @Override
    public RestResp<ComicChapterRespDto> getLastChapterAbout(Long comicId) {
        // 查询漫画信息
        ComicInfoRespDto comicInfo = comicInfoCacheManager.getComicInfo(comicId);

        // 查询最新章节信息
        ComicChapterRespDto comicChapter = comicChapterCacheManager.getChapter(comicId,comicInfo.getLastChapterNum());

        // 数据返回
        return RestResp.ok(comicChapter);

    }


    /**
     * 漫画推荐列表查询接口
     * 通过漫画id，查找相同类型的漫画
     */
    @Override
    public RestResp<List<ComicItemRespDto>> listRecComics(Long comicId)
            throws NoSuchAlgorithmException {
        //类别id
        Long categoryId = comicInfoCacheManager.getComicInfo(comicId).getCategoryId();
        //500个同类型的最新漫画id列表
        List<Long> lastUpdateIdList = comicInfoCacheManager.getLastUpdateIdList(categoryId);
        //封装 REC_COMIC_COUNT 个推荐漫画条目返回
        List<ComicItemRespDto> comicItemList = new ArrayList<>();
        List<Integer> recIdIndexList = new ArrayList<>();
        int count = 0;
        Random rand = SecureRandom.getInstanceStrong();
        while (count < REC_COMIC_COUNT) {
            int recIdIndex = rand.nextInt(lastUpdateIdList.size());
            if (!recIdIndexList.contains(recIdIndex)) {
                recIdIndexList.add(recIdIndex);
                comicId = lastUpdateIdList.get(recIdIndex);
                ComicItemRespDto comicItem = comicItemCacheManager.getComicItem(comicId);
                comicItemList.add(comicItem);
                count++;
            }
        }
        return RestResp.ok(comicItemList);
    }

    /**
     * 增加漫画点击量接口
     */
    @Override
    public RestResp<Void> addVisitCount(Long comicId) {
        comicInfoMapper.addVisitCount(comicId);
        return RestResp.ok();
    }

    /**
     * 获取上一章节ID接口
     */
    @Override
    public RestResp<Long> getPreChapterId(Long chapterId) {
        // 查询漫画ID 和 章节号
        ComicChapterRespDto chapter = comicChapterCacheManager.getChapter(chapterId);
        Long comicId = chapter.getComicId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询上一章信息并返回章节ID
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
                .lt(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByDesc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
                Optional.ofNullable(comicChapterMapper.selectOne(queryWrapper))
                        .map(ComicChapter::getId)
                        .orElse(null)
        );
    }

    /**
     * 获取下一章节ID接口
     */
    @Override
    public RestResp<Long> getNextChapterId(Long chapterId) {
        // 查询漫画ID 和 章节号
        ComicChapterRespDto chapter = comicChapterCacheManager.getChapter(chapterId);
        Long comicId = chapter.getComicId();
        Integer chapterNum = chapter.getChapterNum();

        // 查询下一章信息并返回章节ID
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
                .gt(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByAsc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        return RestResp.ok(
                Optional.ofNullable(comicChapterMapper.selectOne(queryWrapper))
                        .map(ComicChapter::getId)
                        .orElse(null)
        );
    }

    /**
     * 删除漫画章节---
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> deleteComicChapter(Long chapterId) {
        // 1.查询章节信息和漫画信息
        ComicChapterRespDto chapter = comicChapterCacheManager.getChapter(chapterId);
        ComicInfoRespDto comicInfo = comicInfoCacheManager.getComicInfo(chapter.getComicId());
        // 2.删除章节信息
        comicChapterMapper.deleteById(chapterId);
        // 3.删除章节图片地址信息
        QueryWrapper<ComicChapterPicture> comicContentQueryWrapper = new QueryWrapper<>();
        comicContentQueryWrapper
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, chapter.getComicId())
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM, chapter.getChapterNum());
        comicChapterPictureMapper.delete(comicContentQueryWrapper);
        // 4. 更新其他章节的章节号
        comicChapterMapper.allChapterNumGtNumSubtractOne(chapter.getChapterNum());
        comicChapterMapper.allPictureChapterNumGtNumSubtractOne(chapter.getChapterNum());
        // 4.更新漫画信息
        ComicInfo newComicInfo = new ComicInfo();
        newComicInfo.setId(chapter.getComicId());
        QueryWrapper<ComicChapter> comicChapterQueryWrapper = new QueryWrapper<>();
        comicChapterQueryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, chapter.getComicId())
                .orderByDesc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        ComicChapter lastComicChapter = comicChapterMapper.selectOne(comicChapterQueryWrapper);
        if (Objects.nonNull(lastComicChapter)){
            newComicInfo.setLastChapterNum(lastComicChapter.getChapterNum());
            newComicInfo.setLastChapterName(lastComicChapter.getChapterName());
            newComicInfo.setLastChapterUpdateTime(lastComicChapter.getUpdateTime());
            Integer newChapterCount = comicInfo.getChapterCount() - 1;
            newComicInfo.setChapterCount(newChapterCount >= 0 ? newChapterCount : 0);
        }
        comicInfoMapper.updateById(newComicInfo);
        // 5.清理章节信息缓存
        comicChapterCacheManager.evictComicChapterCache(chapterId);
        // 6.清理漫画条目缓存
        comicItemCacheManager.evictComicItemCache(chapter.getComicId());
        // 7.清理漫画信息缓存
        comicInfoCacheManager.evictComicInfoCache(chapter.getComicId());
        // 8.发送漫画信息更新的 MQ 消息
        amqpMsgManager.sendComicChangeMsg(chapter.getComicId());
        return RestResp.ok();
    }


    /**
     * 更新漫画章节---
     */
    @Transactional
    @Override
    public RestResp<Void> updateComicChapter(Long chapterId, ChapterUpdateReqDto dto) {
        // 1.查询章节信息
        ComicChapterRespDto chapter = comicChapterCacheManager.getChapter(chapterId);
        // 2.查询漫画信息
        ComicInfoRespDto comicInfo = comicInfoCacheManager.getComicInfo(chapter.getComicId());
        // 3.更新章节信息
        ComicChapter newChapter = new ComicChapter();
        newChapter.setId(chapterId);
        newChapter.setChapterName(dto.getChapterName());
        newChapter.setChapterDesc(dto.getChapterContent());
        newChapter.setIsVip(dto.getIsVip());
        newChapter.setUpdateTime(LocalDateTime.now());
        comicChapterMapper.updateById(newChapter);
        // 4.更新漫画信息
        ComicInfo newComicInfo = new ComicInfo();
        newComicInfo.setId(chapter.getComicId());
        newComicInfo.setUpdateTime(LocalDateTime.now());
        //判断是否是最新章节
        if (Objects.equals(comicInfo.getLastChapterNum(), chapter.getChapterNum())) {
            // 更新最新章节信息
            newComicInfo.setLastChapterName(dto.getChapterName());
            newComicInfo.setLastChapterUpdateTime(LocalDateTime.now());
        }
        comicInfoMapper.updateById(newComicInfo);
        // 5.清理章节信息缓存
        comicChapterCacheManager.evictComicChapterCache(chapterId);
        // 6.清理漫画条目缓存
        comicItemCacheManager.evictComicItemCache(chapter.getComicId());
        // 7.清理漫画信息缓存
        comicInfoCacheManager.evictComicInfoCache(chapter.getComicId());
        // 8.发送漫画信息更新的 MQ 消息
        amqpMsgManager.sendComicChangeMsg(chapter.getComicId());
        return RestResp.ok();
    }

    /**
     * 漫画章节列表查询接口
     */
    @Override
    public RestResp<List<ComicChapterRespDto>> listChapters(Long comicId) {
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
                .orderByAsc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM);

        return RestResp.ok(comicChapterMapper.selectList(queryWrapper).stream()
                .map(v -> ComicChapterRespDto.builder()
                        .comicId(v.getComicId())
                        .chapterId(v.getId())
                        .chapterNum(v.getChapterNum())
                        .chapterName(v.getChapterName())
                        .isVip(v.getIsVip())
                        .build()
                ).toList());
    }

    /**
     * 查询漫画第一章节
     * @param comicId
     * @return
     */
    @Override
    public RestResp<ComicChapterRespDto> getFirstChapter(Long comicId){
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
                .orderByAsc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        ComicChapter firstChapter = comicChapterMapper.selectOne(queryWrapper);
        return RestResp.ok(
                ComicChapterRespDto.builder()
                        .comicId(firstChapter.getComicId())
                        .chapterId(firstChapter.getId())
                        .chapterNum(firstChapter.getChapterNum())
                        .chapterName(firstChapter.getChapterName())
                        .isVip(firstChapter.getIsVip())
                        .build()
        );
    }

    /**
     * 漫画分类列表(男频、女频)查询接口
     */
    @Override
    public RestResp<List<ComicCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(comicCategoryCacheManager.listCategory(workDirection));
    }


    /**
     * 保存漫画接口---
     */
    @Override
    public RestResp<Void> saveComic(ComicAddReqDto dto) {
        // 校验漫画名是否已存在
        QueryWrapper<ComicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicTable.COLUMN_COMIC_NAME, dto.getComicName());
        if (comicInfoMapper.selectCount(queryWrapper) > 0) {
            return RestResp.fail(ErrorCodeEnum.AUTHOR_COMIC_NAME_EXIST);
        }
        //不存在则保存
        ComicInfo comicInfo = new ComicInfo();
        // 设置作家信息
        AuthorInfoDto author = authorInfoCacheManager.getAuthor(UserHolder.getUserId());
        comicInfo.setAuthorId(author.getId());
        comicInfo.setAuthorName(author.getPenName());
        // 设置其他信息
        comicInfo.setWorkDirection(dto.getWorkDirection());
        comicInfo.setCategoryId(dto.getCategoryId());
        comicInfo.setCategoryName(dto.getCategoryName());
        comicInfo.setComicName(dto.getComicName());
        comicInfo.setPicUrl(dto.getPicUrl());
        comicInfo.setComicDesc(dto.getComicDesc());
        comicInfo.setTags(dto.getTagsString());
        comicInfo.setIsVip(dto.getIsVip());
        comicInfo.setComicStatus(dto.getComicStatus());
        comicInfo.setScore(0);
        comicInfo.setCreateTime(LocalDateTime.now());
        comicInfo.setUpdateTime(LocalDateTime.now());
        // 保存漫画信息
        comicInfoMapper.insert(comicInfo);
        return RestResp.ok();
    }

    /**
     * 保存漫画章节信息---
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public RestResp<Void> saveComicChapter(ChapterAddReqDto dto) {
        // 校验该作品是否属于当前作家
        ComicInfo comicInfo = comicInfoMapper.selectById(dto.getComicId());
        if (!Objects.equals(comicInfo.getAuthorId(), UserHolder.getAuthorId())) {
            return RestResp.fail(ErrorCodeEnum.USER_UN_AUTH);
        }
        // 1) 保存章节相关信息到漫画章节表
        //  a) 查询最新章节号
        int chapterNum = 0;
        QueryWrapper<ComicChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, dto.getComicId())
                .orderByDesc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());
        ComicChapter comicChapter = comicChapterMapper.selectOne(chapterQueryWrapper);
        if (Objects.nonNull(comicChapter)) {
            chapterNum = comicChapter.getChapterNum() + 1;
        }
        //  b) 设置章节相关信息并保存
        ComicChapter newComicChapter = new ComicChapter();
        newComicChapter.setComicId(dto.getComicId());
        newComicChapter.setChapterName(dto.getChapterName());
        newComicChapter.setChapterNum(chapterNum);
        newComicChapter.setChapterDesc(dto.getChapterContent());
        newComicChapter.setIsVip(dto.getIsVip());
        newComicChapter.setCreateTime(LocalDateTime.now());
        newComicChapter.setUpdateTime(LocalDateTime.now());
        comicChapterMapper.insert(newComicChapter);

        // 2) 更新漫画表信息
        //  a) 更新漫画表关于最新章节的信息
        ComicInfo newComicInfo = new ComicInfo();
        newComicInfo.setId(dto.getComicId());
        newComicInfo.setChapterCount(comicInfo.getChapterCount() + 1);
        newComicInfo.setLastChapterNum(newComicChapter.getChapterNum());
        newComicInfo.setLastChapterName(newComicChapter.getChapterName());
        newComicInfo.setLastChapterUpdateTime(LocalDateTime.now());
        newComicChapter.setUpdateTime(LocalDateTime.now());
        comicInfoMapper.updateById(newComicInfo);
        //  b) 清除漫画信息缓存和章节信息缓存
        comicInfoCacheManager.evictComicInfoCache(dto.getComicId());
        //  c) 发送漫画信息更新的 MQ 消息
        amqpMsgManager.sendComicChangeMsg(dto.getComicId());
        return RestResp.ok();
    }

    /**
     * 查询作家漫画列表---暂未使用
     */
    @Override
    public RestResp<PageRespDto<ComicInfoRespDto>> listAuthorComics(PageReqDto dto) {
        IPage<ComicInfo> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<ComicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicTable.AUTHOR_ID, UserHolder.getAuthorId())
                .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        IPage<ComicInfo> comicInfoPage = comicInfoMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
                comicInfoPage.getRecords().stream().map(v -> ComicInfoRespDto.builder()
                                .comicId(v.getId())
                                .comicName(v.getComicName())
                                .picUrl(v.getPicUrl())
                                .categoryName(v.getCategoryName())
                                .chapterCount(v.getChapterCount())
                                .visitCount(v.getVisitCount())
                                .updateTime(v.getUpdateTime())
                                .build())
                        .toList()));
    }

    /**
     * 查询漫画发布章节列表---暂未使用
     */
    @Override
    public RestResp<PageRespDto<ComicChapterRespDto>> listComicChapters(Long comicId, PageReqDto dto) {
        IPage<ComicChapter> page = new Page<>();
        page.setCurrent(dto.getPageNum());
        page.setSize(dto.getPageSize());
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
                .orderByDesc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM);
        IPage<ComicChapter> comicChapterPage = comicChapterMapper.selectPage(page, queryWrapper);
        return RestResp.ok(PageRespDto.of(dto.getPageNum(), dto.getPageSize(), page.getTotal(),
                comicChapterPage.getRecords().stream().map(v -> ComicChapterRespDto.builder()
                                .chapterId(v.getId())
                                .chapterName(v.getChapterName())
                                .chapterUpdateTime(v.getUpdateTime())
                                .isVip(v.getIsVip())
                                .build())
                        .toList()));
    }

    /**
     * 发表评论接口
     */
    @Lock(prefix = "userComment")
    @Override
    public RestResp<Void> saveComment(
            @Key(expr = "#{userId + '::' + comicId}") UserCommentReqDto dto) {
        // 校验用户是否已发表评论
        QueryWrapper<ComicComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicCommentTable.COLUMN_USER_ID, dto.getUserId())
                .eq(DatabaseConsts.ComicCommentTable.COLUMN_COMIC_ID, dto.getComicId());
//        if (comicCommentMapper.selectCount(queryWrapper) > 0) {
//            // 用户已发表评论
//            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
//        }
        ComicComment comicComment = new ComicComment();
        comicComment.setComicId(dto.getComicId());
        comicComment.setUserId(dto.getUserId());
        comicComment.setCommentContent(dto.getCommentContent());
        comicComment.setCreateTime(LocalDateTime.now());
        comicComment.setUpdateTime(LocalDateTime.now());
        comicCommentMapper.insert(comicComment);
        return RestResp.ok();
    }

    /**
     * 删除评论接口
     */
    @Override
    public RestResp<Void> deleteComment(Long commentId) {
        QueryWrapper<ComicComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), commentId);
        comicCommentMapper.delete(queryWrapper);
        return RestResp.ok();
    }

    /**
     * 修改评论接口
     */
    @Override
    public RestResp<Void> updateComment(Long commentId, String content) {
        QueryWrapper<ComicComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), commentId);
        ComicComment comicComment = new ComicComment();
        comicComment.setCommentContent(content);
        comicCommentMapper.update(comicComment, queryWrapper);
        return RestResp.ok();
    }



    /**
     * 漫画最新评论查询接口
     */
    @Override
    public RestResp<ComicCommentRespDto> listNewestComments(Long comicId) {
        // 查询评论总数
        QueryWrapper<ComicComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.ComicCommentTable.COLUMN_COMIC_ID, comicId);
        Long commentTotal = comicCommentMapper.selectCount(commentCountQueryWrapper);
        ComicCommentRespDto comicCommentRespDto = ComicCommentRespDto.builder()
                .commentTotal(commentTotal).build();
        //评论总数大于一条
        if (commentTotal > 0) {
            // 查询最新的评论列表
            QueryWrapper<ComicComment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq(DatabaseConsts.ComicCommentTable.COLUMN_COMIC_ID, comicId)
                    .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                    .last(DatabaseConsts.SqlEnum.LIMIT_10.getSql());
            List<ComicComment> comicComments = comicCommentMapper.selectList(commentQueryWrapper);

            // 查询评论用户信息，并设置需要返回的评论用户名
            List<Long> userIds = comicComments.stream().map(ComicComment::getUserId).toList();
            List<UserInfo> userInfos = userDaoManager.listUsers(userIds);
            Map<Long, UserInfo> userInfoMap = userInfos.stream()
                    .collect(Collectors.toMap(UserInfo::getId, Function.identity()));
            List<ComicCommentRespDto.CommentInfo> commentInfos = comicComments.stream()
                    .map(v -> ComicCommentRespDto.CommentInfo.builder()
                            .commentId(v.getId())
                            .commentUserId(v.getUserId())
                            .commentUser(userInfoMap.get(v.getUserId()).getUsername())
                            .commentUserPhoto(userInfoMap.get(v.getUserId()).getUserPhoto())
                            .commentContent(v.getCommentContent())
                            .commentTime(v.getCreateTime())
                            .build())
                    .toList();
            comicCommentRespDto.setComments(commentInfos);
        } else {
            comicCommentRespDto.setComments(Collections.emptyList());
        }
        return RestResp.ok(comicCommentRespDto);
    }

    /**
     * 分页查询评论
     */
    @Override
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto) {
        IPage<ComicComment> page = new Page<>();
        page.setCurrent(pageReqDto.getPageNum());
        page.setSize(pageReqDto.getPageSize());
        QueryWrapper<ComicComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicCommentTable.COLUMN_USER_ID, userId)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        IPage<ComicComment> comicCommentPage = comicCommentMapper.selectPage(page, queryWrapper);
        List<ComicComment> comments = comicCommentPage.getRecords();
        if (!CollectionUtils.isEmpty(comments)) {
            List<Long> comicIds = comments.stream().map(ComicComment::getComicId).toList();
            QueryWrapper<ComicInfo> comicInfoQueryWrapper = new QueryWrapper<>();
            comicInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), comicIds);
            Map<Long, ComicInfo> comicInfoMap = comicInfoMapper.selectList(comicInfoQueryWrapper).stream()
                    .collect(Collectors.toMap(ComicInfo::getId, Function.identity()));
            return RestResp.ok(PageRespDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
                    comments.stream().map(v -> UserCommentRespDto.builder()
                            .commentContent(v.getCommentContent())
                            .commentComic(comicInfoMap.get(v.getComicId()).getComicName())
                            .commentComicPic(comicInfoMap.get(v.getComicId()).getPicUrl())
                            .commentTime(v.getCreateTime())
                            .build()).toList()));

        }
        return RestResp.ok(PageRespDto.of(pageReqDto.getPageNum(), pageReqDto.getPageSize(), page.getTotal(),
                Collections.emptyList()));
    }




}
