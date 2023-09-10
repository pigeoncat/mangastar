package io.github.spider.service;

import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import io.github.spidercommon.entity.ComicInfo;

import java.util.List;


/**
 * 漫画模块
 *
 */
public interface ComicService {


    /**
     * 根据漫画名称和来源查询漫画信息
     */
    ComicInfo getComicInfoByNameAndOrigin(String comicName,String origin);
}
