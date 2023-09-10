package io.github.spider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.spider.common.constant.ComicConstant;
import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import io.github.spidercommon.entity.ComicInfo;
import io.github.spider.service.ComicService;
import io.github.spidercommon.mapper.ComicChapterMapper;
import io.github.spidercommon.mapper.ComicChapterPictureMapper;
import io.github.spidercommon.mapper.ComicInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author pigeoncat
 * @Date 2023/09/02  17:08
 * @TODO description
 */
@Service
public class ComicServiceImpl implements ComicService {

    @Autowired
    private ComicInfoMapper comicInfoMapper;


    @Override
    public ComicInfo getComicInfoByNameAndOrigin(String comicName,String origin) {
        QueryWrapper<ComicInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(ComicConstant.ComicInfoConstant.COMIC_NAME,comicName)
                .eq(ComicConstant.ComicInfoConstant.ORIGIN,origin);
        ComicInfo comicInfo = comicInfoMapper.selectOne(wrapper);
        return comicInfo;
    }
}
