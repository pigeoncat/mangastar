package io.github.pigeoncat.comicstar.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.CacheConsts;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicCategory;
import io.github.pigeoncat.comicstar.dao.mapper.ComicCategoryMapper;
import io.github.pigeoncat.comicstar.dto.resp.ComicCategoryRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 漫画分类 缓存管理类
 *
 */
@Component
@RequiredArgsConstructor
public class ComicCategoryCacheManager {

    private final ComicCategoryMapper comicCategoryMapper;

    /**
     * 根据作品方向(男频、女频)查询漫画分类列表，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.REDIS_CACHE_MANAGER,
        value = CacheConsts.COMIC_CATEGORY_LIST_CACHE_NAME, unless = "#result == null")
    public List<ComicCategoryRespDto> listCategory(Integer workDirection) {
        QueryWrapper<ComicCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.ComicCategoryTable.COLUMN_WORK_DIRECTION, workDirection);
        return comicCategoryMapper.selectList(queryWrapper)
                .stream()
                .map(v -> ComicCategoryRespDto.builder().id(v.getId()).name(v.getName()).build())
                .toList();
    }

}
