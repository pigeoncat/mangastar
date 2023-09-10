package io.github.pigeoncat.comicstar.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.CacheConsts;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dto.resp.ComicItemRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 漫画排行榜 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class ComicRankCacheManager {

    private final ComicInfoMapper comicInfoMapper;

    /**
     * 查询漫画点击榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
        value = CacheConsts.COMIC_VISIT_RANK_CACHE_NAME)
    public List<ComicItemRespDto> listVisitRankComics() {
        QueryWrapper<ComicInfo> comicInfoQueryWrapper = new QueryWrapper<>();
        comicInfoQueryWrapper.orderByDesc(DatabaseConsts.ComicTable.COLUMN_VISIT_COUNT);
        return listRankComics(comicInfoQueryWrapper);
    }

    /**
     * 查询漫画新书榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_NEWEST_RANK_CACHE_NAME)
    public List<ComicItemRespDto> listNewestRankComics() {
        QueryWrapper<ComicInfo> comicInfoQueryWrapper = new QueryWrapper<>();
        comicInfoQueryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName());
        return listRankComics(comicInfoQueryWrapper);
    }

    /**
     * 查询漫画更新榜列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_UPDATE_RANK_CACHE_NAME)
    public List<ComicItemRespDto> listUpdateRankComics() {
        QueryWrapper<ComicInfo> comicInfoQueryWrapper = new QueryWrapper<>();
        comicInfoQueryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        return listRankComics(comicInfoQueryWrapper);
    }


    //查询封装排行榜通用部分
    private List<ComicItemRespDto> listRankComics(QueryWrapper<ComicInfo> comicInfoQueryWrapper) {
        comicInfoQueryWrapper
            .gt(DatabaseConsts.ComicTable.COLUMN_CHAPTER_COUNT, 0)
            .last(DatabaseConsts.SqlEnum.LIMIT_30.getSql());
        return comicInfoMapper.selectList(comicInfoQueryWrapper).stream().map(v -> {
            ComicItemRespDto respDto = new ComicItemRespDto();
            respDto.setComicId(v.getId());
            respDto.setCategoryId(v.getCategoryId());
            respDto.setCategoryName(v.getCategoryName());
            respDto.setComicName(v.getComicName());
            respDto.setAuthorName(v.getAuthorName());
            respDto.setPicUrl(v.getPicUrl());
            respDto.setTags(v.getTags());
            respDto.setComicDesc(v.getComicDesc());
            respDto.setChapterCount(v.getChapterCount());
            respDto.setLastChapterNum(v.getLastChapterNum());
            respDto.setLastChapterName(v.getLastChapterName());
            respDto.setLastChapterUpdateTime(v.getLastChapterUpdateTime());

            return respDto;
        }).toList();
    }


}
