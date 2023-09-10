package io.github.comicmq.service;


import io.github.spidercommon.dto.MysqlDto;
import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import io.github.spidercommon.entity.ComicInfo;

import java.util.List;


/**
 * 漫画模块
 *
 */
public interface ComicService {

    void insert(MysqlDto mysqlDto);

    void update(MysqlDto mysqlDto);

    /**
     * 更新漫画信息
     */
    void updateComicInfo(ComicInfo comicInfo);

    /**
     * 添加漫画信息
     */
    void addComicInfo(ComicInfo comicInfo);

    /**
     * 添加漫画章节
     */
    void addComicChapter(ComicChapter comicChapter);

    /**
     * 批量添加漫画章节图片
     */
    void addBatchComicChapterPicture(List<ComicChapterPicture> pictureList);

    /**
     * 根据漫画名称和来源查询漫画信息
     */
    ComicInfo getComicInfoByNameAndOrigin(String comicName,String origin);
}
