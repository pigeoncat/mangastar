package io.github.comicmq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.comicmq.common.constant.ComicConstant;
import io.github.spidercommon.dto.ChapterDto;
import io.github.spidercommon.dto.MysqlDto;
import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import io.github.spidercommon.entity.ComicInfo;
import io.github.spidercommon.mapper.ComicChapterMapper;
import io.github.spidercommon.mapper.ComicChapterPictureMapper;
import io.github.spidercommon.mapper.ComicInfoMapper;
import io.github.comicmq.service.ComicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author pigeoncat
 * @Date 2023/09/02  17:08
 * @TODO description
 */
@Service
@Slf4j
public class ComicServiceImpl implements ComicService {

    @Autowired
    private ComicChapterMapper comicChapterMapper;
    @Autowired
    private ComicChapterPictureMapper comicChapterPictureMapper;
    @Autowired
    private ComicInfoMapper comicInfoMapper;


    @Transactional
    @Override
    public void insert(MysqlDto mysqlDto) {
        //漫画详情入库
        addComicInfo(mysqlDto.getComicInfo());
        //所有章节入库
        List<ChapterDto> chapterDtoList = mysqlDto.getChapterDtoList();
        for (ChapterDto chapterDto : chapterDtoList) {
            //章节信息入库
            addComicChapter(chapterDto.getComicChapter());
            //章节图片入库
            addBatchComicChapterPicture(chapterDto.getPictureList());
        }
    }

    @Transactional
    @Override
    public void update(MysqlDto mysqlDto) {
        //更新漫画详情
        updateComicInfo(mysqlDto.getComicInfo());
        //插入新增章节
        List<ChapterDto> chapterDtoList = mysqlDto.getChapterDtoList();
        for (ChapterDto chapterDto : chapterDtoList) {
            //新的章节信息入库
            addComicChapter(chapterDto.getComicChapter());
            //章节图片入库
            addBatchComicChapterPicture(chapterDto.getPictureList());
        }
    }

    @Transactional
    @Override
    public void updateComicInfo(ComicInfo comicInfo) {
        log.info("更新漫画信息: {}",comicInfo.getComicName());
        comicInfoMapper.updateById(comicInfo);
    }

    @Transactional
    @Override
    public void addComicInfo(ComicInfo comicInfo) {
        log.info("开始添加漫画* {} *到数据库",comicInfo.getComicName());
        comicInfoMapper.insert(comicInfo);
    }

    @Transactional
    @Override
    public void addComicChapter(ComicChapter comicChapter) {
        log.info("添加第* {} *章到数据库",comicChapter.getChapterNum());
        comicChapterMapper.insert(comicChapter);
    }

    @Transactional
    @Override
    public void addBatchComicChapterPicture(List<ComicChapterPicture> pictureList) {
        log.info("批量添加漫画章节图片到数据库");
        pictureList.forEach(p-> comicChapterPictureMapper.insert(p));
    }


    @Override
    public ComicInfo getComicInfoByNameAndOrigin(String comicName,String origin) {
        QueryWrapper<ComicInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(ComicConstant.ComicInfoConstant.COMIC_NAME,comicName)
                .eq(ComicConstant.ComicInfoConstant.ORIGIN,origin);
        ComicInfo comicInfo = comicInfoMapper.selectOne(wrapper);
        return comicInfo;
    }
}
