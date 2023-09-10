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
 * 漫画条目 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class ComicItemCacheManager {

    private final ComicInfoMapper comicInfoMapper;


    /**
     * 查询漫画条目，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.COMIC_ITEM_CACHE_NAME)
    public ComicItemRespDto getComicItem(Long comicId) {
        ComicInfo comicInfo = comicInfoMapper.selectById(comicId);
        ComicItemRespDto comicItem = new ComicItemRespDto();
        comicItem.setComicId(comicInfo.getId());
        comicItem.setCategoryId(comicInfo.getCategoryId());
        comicItem.setCategoryName(comicInfo.getCategoryName());
        comicItem.setComicName(comicInfo.getComicName());
        comicItem.setAuthorName(comicInfo.getAuthorName());
        comicItem.setPicUrl(comicInfo.getPicUrl());
        comicItem.setTags(comicInfo.getTags());
        comicItem.setComicDesc(comicInfo.getComicDesc());
        comicItem.setChapterCount(comicInfo.getChapterCount());
        comicItem.setLastChapterNum(comicInfo.getLastChapterNum());
        comicItem.setLastChapterName(comicInfo.getLastChapterName());
        comicItem.setLastChapterUpdateTime(comicInfo.getLastChapterUpdateTime());
        return comicItem;
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.COMIC_ITEM_CACHE_NAME)
    public void evictComicItemCache(Long comicId) {
        // 调用此方法自动清除漫画条目的缓存
    }



}
