package io.github.pigeoncat.comicstar.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.CacheConsts;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapter;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapterPicture;
import io.github.pigeoncat.comicstar.dao.mapper.ComicChapterMapper;
import io.github.pigeoncat.comicstar.dao.mapper.ComicChapterPictureMapper;
import io.github.pigeoncat.comicstar.dto.resp.ComicChapterRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 漫画章节 缓存管理类
 */
@Component
@RequiredArgsConstructor
public class ComicChapterCacheManager {

    private final ComicChapterMapper comicChapterMapper;

    private final ComicChapterPictureMapper comicChapterPictureMapper;



    /**
     * 查询漫画章节信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_CHAPTER_CACHE_NAME)
    public ComicChapterRespDto getChapter(Long comicId,Integer chapterNum) {
        //章节基础信息
        QueryWrapper<ComicChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID,comicId)
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM,chapterNum);
        ComicChapter comicChapter = comicChapterMapper.selectOne(chapterQueryWrapper);
        //章节图片地址信息
        QueryWrapper<ComicChapterPicture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID,comicId)
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM,chapterNum);
        List<ComicChapterPicture> chapterPictures = comicChapterPictureMapper.selectList(pictureQueryWrapper);

        return ComicChapterRespDto.builder()
            .comicId(comicChapter.getComicId())
            .chapterId(comicChapter.getId())
            .chapterNum(comicChapter.getChapterNum())
            .chapterName(comicChapter.getChapterName())
            .chapterContent(comicChapter.getChapterDesc())
            .chapterPictureCount(comicChapter.getPageCount())
            .chapterPictures(chapterPictures)
            .isVip(comicChapter.getIsVip())
            .chapterUpdateTime(comicChapter.getUpdateTime())
            .build();
    }

    /**
     * 根据漫画id和章节号查询漫画章节信息，并放入缓存中
     */
    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.COMIC_CHAPTER_CACHE_NAME)
    public ComicChapterRespDto getChapter(Long chapterId) {
        //章节基础信息
        ComicChapter comicChapter = comicChapterMapper.selectById(chapterId);
        //章节图片地址信息
        QueryWrapper<ComicChapterPicture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_COMIC_ID,comicChapter.getComicId())
                .eq(DatabaseConsts.ComicChapterTable.COLUMN_CHAPTER_NUM,comicChapter.getChapterNum());
        List<ComicChapterPicture> chapterPictures = comicChapterPictureMapper.selectList(pictureQueryWrapper);

        return ComicChapterRespDto.builder()
                .comicId(comicChapter.getComicId())
                .chapterId(comicChapter.getId())
                .chapterNum(comicChapter.getChapterNum())
                .chapterName(comicChapter.getChapterName())
                .chapterContent(comicChapter.getChapterDesc())
                .chapterPictureCount(comicChapter.getPageCount())
                .chapterPictures(chapterPictures)
                .isVip(comicChapter.getIsVip())
                .chapterUpdateTime(comicChapter.getUpdateTime())
                .build();
    }



    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
            value = CacheConsts.COMIC_CHAPTER_CACHE_NAME)
    public void evictComicChapterCache(Long comicId,Integer chapterNum) {
        // 调用此方法自动清除漫画章节信息的缓存
    }

    @CacheEvict(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER,
        value = CacheConsts.COMIC_CHAPTER_CACHE_NAME)
    public void evictComicChapterCache(Long chapterId) {
        // 调用此方法自动清除漫画章节信息的缓存
    }

}
