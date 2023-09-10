package io.github.pigeoncat.comicstar.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.CacheConsts;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.entity.HomeComic;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dao.mapper.HomeComicMapper;
import io.github.pigeoncat.comicstar.dto.resp.HomeComicRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 首页推荐漫画 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class HomeComicCacheManager {

    private final HomeComicMapper homeComicMapper;

    private final ComicInfoMapper comicInfoMapper;

    /**
     * 查询首页漫画推荐，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.HOME_COMIC_CACHE_NAME)
    public List<HomeComicRespDto> listHomeComics() {
        // 从首页漫画推荐表中查询出需要推荐的漫画,sort表示排序的序号
        QueryWrapper<HomeComic> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(DatabaseConsts.CommonColumnEnum.SORT.getName());
        List<HomeComic> homeComics = homeComicMapper.selectList(queryWrapper);

        // 获取推荐漫画ID列表
        if (!CollectionUtils.isEmpty(homeComics)) {
            List<Long> comicIds = homeComics.stream()
                    .map(HomeComic::getComicId)
                    .toList();

            // 根据漫画ID列表查询相关的漫画信息列表
            QueryWrapper<ComicInfo> comicInfoQueryWrapper = new QueryWrapper<>();
            comicInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), comicIds);
            List<ComicInfo> comicInfos = comicInfoMapper.selectList(comicInfoQueryWrapper);

            // 组装 HomeComicRespDto 列表数据并返回
            if (!CollectionUtils.isEmpty(comicInfos)) {
                Map<Long, ComicInfo> comicInfoMap = comicInfos.stream()
                        .collect(Collectors.toMap(ComicInfo::getId, Function.identity()));

                return homeComics.stream().map(v -> {
                    ComicInfo comicInfo = comicInfoMap.get(v.getComicId());
                    HomeComicRespDto comicRespDto = new HomeComicRespDto();
                    comicRespDto.setType(v.getType());
                    comicRespDto.setComicId(v.getComicId());
                    comicRespDto.setComicName(comicInfo.getComicName());
                    comicRespDto.setCategoryId(comicInfo.getCategoryId());
                    comicRespDto.setCategoryName(comicInfo.getCategoryName());
                    comicRespDto.setAuthorName(comicInfo.getAuthorName());
                    comicRespDto.setPicUrl(comicInfo.getPicUrl());
                    comicRespDto.setTags(comicInfo.getTags());
                    comicRespDto.setComicDesc(comicInfo.getComicDesc());
                    comicRespDto.setChapterCount(comicInfo.getChapterCount());
                    comicRespDto.setLastChapterNum(comicInfo.getLastChapterNum());
                    comicRespDto.setLastChapterName(comicInfo.getLastChapterName());
                    comicRespDto.setLastChapterUpdateTime(comicInfo.getLastChapterUpdateTime());
                    return comicRespDto;
                }).toList();

            }

        }

        return Collections.emptyList();
    }



    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.HOME_COMIC_CACHE_NAME)
    public void evictListHomeComicsCache() {
        // 调用此方法自动清除缓存
    }

}
