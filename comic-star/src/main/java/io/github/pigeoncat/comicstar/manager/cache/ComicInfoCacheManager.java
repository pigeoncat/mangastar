package io.github.pigeoncat.comicstar.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.CacheConsts;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapter;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapterDigest;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.mapper.ComicChapterMapper;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicItemRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 漫画信息 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class ComicInfoCacheManager {

    private final ComicInfoMapper comicInfoMapper;

    private final ComicChapterMapper comicChapterMapper;

    /**
     * 从缓存中查询漫画信息（先判断缓存中是否已存在，存在则直接从缓存中取，否则执行方法体中的逻辑后缓存结果）
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_INFO_CACHE_NAME)
    public ComicInfoRespDto getComicInfo(Long comicId) {
        return cachePutComicInfo(comicId);
    }

    /**
     * 缓存漫画信息（不管缓存中是否存在都执行方法体中的逻辑，然后缓存起来）
     */
    @CachePut(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_INFO_CACHE_NAME)
    public ComicInfoRespDto cachePutComicInfo(Long comicId) {
        // 查询基础信息
        ComicInfo comicInfo = comicInfoMapper.selectById(comicId);
        // 查询所有章节
        QueryWrapper<ComicChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper
            .eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID, comicId)
            .orderByAsc(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM);
        List<ComicChapter> comicChapters = comicChapterMapper.selectList(queryWrapper);
        List<ComicChapterDigest> allChapterList = comicChapters.stream()
                .map(c -> {
                    ComicChapterDigest comicChapterDigest = new ComicChapterDigest();
                    comicChapterDigest.setChapterId(c.getId());
                    comicChapterDigest.setChapterNum(c.getChapterNum());
                    comicChapterDigest.setChapterName(c.getChapterName());
                    return comicChapterDigest;
                })
                .collect(Collectors.toList());

        // 组装响应对象
        return ComicInfoRespDto.builder()
            .comicId(comicInfo.getId())
            .comicName(comicInfo.getComicName())
            .comicDesc(comicInfo.getComicDesc())
            .comicStatus(comicInfo.getComicStatus())
            .authorId(comicInfo.getAuthorId())
            .authorName(comicInfo.getAuthorName())
            .categoryId(comicInfo.getCategoryId())
            .categoryName(comicInfo.getCategoryName())
            .visitCount(comicInfo.getVisitCount())
            .chapterCount(comicInfo.getChapterCount())
            .commentCount(comicInfo.getCommentCount())
            .picUrl(comicInfo.getPicUrl())
            .allChapterList(allChapterList)
            .lastChapterNum(comicInfo.getLastChapterNum())
            .lastChapterName(comicInfo.getLastChapterName())
            .updateTime(comicInfo.getUpdateTime())
            .build();
    }


    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_INFO_CACHE_NAME)
    public void evictComicInfoCache(Long comicId) {
        // 调用此方法自动清除漫画信息的缓存
    }

    /**
     * 查询每个类别下最新更新的 500 个漫画ID列表，并放入缓存中 1 个小时
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.LAST_UPDATE_COMIC_ID_LIST_CACHE_NAME)
    public List<Long> getLastUpdateIdList(Long categoryId) {

        QueryWrapper<ComicInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicTable.COLUMN_CATEGORY_ID, categoryId)
            .gt(DatabaseConsts.ComicTable.COLUMN_CHAPTER_COUNT, 0)
            .orderByDesc(DatabaseConsts.ComicTable.COLUMN_LAST_CHAPTER_UPDATE_TIME)
            .last(DatabaseConsts.SqlEnum.LIMIT_500.getSql());
        return comicInfoMapper.selectList(queryWrapper).stream().map(ComicInfo::getId).toList();
    }

}
