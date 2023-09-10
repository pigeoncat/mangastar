package io.github.pigeoncat.comicstar.core.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.pigeoncat.comicstar.core.constant.DatabaseConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.entity.HomeComic;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dao.mapper.HomeComicMapper;
import io.github.pigeoncat.comicstar.manager.cache.HomeComicCacheManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/09/06  18:34
 * @TODO 定时任务每隔一段时间更新漫画首页表
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class HomeComicListener {

    private final ComicInfoMapper comicInfoMapper;

    private final HomeComicMapper homeComicMapper;

    private final HomeComicCacheManager homeComicCacheManager;


    /**
     * 每隔 12个小时执行一次
     */
    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    public void updateHomeComicTable(){
        //本周强推----按照更新时间降序，取8条
        QueryWrapper<ComicInfo> wrapper1 = new QueryWrapper<>();
        wrapper1.orderByDesc(DatabaseConsts.ComicTable.COLUMN_LAST_CHAPTER_UPDATE_TIME)
                        .last("LIMIT 8");
        List<ComicInfo> comicInfoList1 = comicInfoMapper.selectList(wrapper1);
        List<HomeComic> homeComicList1 = getHomeComics(comicInfoList1, 2);

        //热门推荐----按照点击量降序，取16条
        QueryWrapper<ComicInfo> wrapper2 = new QueryWrapper<>();
        wrapper2.orderByDesc(DatabaseConsts.ComicTable.COLUMN_VISIT_COUNT)
                .last("LIMIT 16");
        List<ComicInfo> comicInfoList2 = comicInfoMapper.selectList(wrapper2);
        List<HomeComic> homeComicList2 = getHomeComics(comicInfoList2, 3);

        //精品推荐----按照评分降序，取30条
        QueryWrapper<ComicInfo> wrapper3 = new QueryWrapper<>();
        wrapper3.orderByDesc(DatabaseConsts.ComicTable.SCORE)
                .last("LIMIT 30");
        List<ComicInfo> comicInfoList3 = comicInfoMapper.selectList(wrapper3);
        List<HomeComic> homeComicList3 = getHomeComics(comicInfoList3, 4);

        //轮播图------本周强推、热门推荐、精品推荐各抽取2条
        List<ComicInfo> comicInfoList4 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            comicInfoList4.add(comicInfoList1.get(i));
            comicInfoList4.add(comicInfoList2.get(i));
            comicInfoList4.add(comicInfoList3.get(i));
        }
        List<HomeComic> homeComicList4 = getHomeComics(comicInfoList4, 0);

        //数据汇总
        List<HomeComic> homeComicListAll = new ArrayList<>();
        homeComicListAll.addAll(homeComicList1);
        homeComicListAll.addAll(homeComicList2);
        homeComicListAll.addAll(homeComicList3);
        homeComicListAll.addAll(homeComicList4);

        //更新数据表
        doUpdateHomeComic(homeComicListAll);

        //清除缓存
        homeComicCacheManager.evictListHomeComicsCache();

    }

    @Transactional
    public void doUpdateHomeComic(List<HomeComic> homeComicListAll){
        //先清空表数据
        homeComicMapper.delete(new QueryWrapper<HomeComic>());
        //然后插入所有数据
        homeComicListAll.forEach(h -> homeComicMapper.insert(h));
    }


    private List<HomeComic> getHomeComics(List<ComicInfo> comicInfoList,Integer type){
        ArrayList<HomeComic> homeComics = new ArrayList<>();
        if (comicInfoList==null){
            return homeComics;
        }
        for (int i = 0; i < comicInfoList.size(); i++) {
            HomeComic homeComic = new HomeComic();
            ComicInfo comicInfo = comicInfoList.get(i);
            homeComic.setComicId(comicInfo.getId());
            homeComic.setType(type);
            homeComic.setSort(i);
            homeComic.setCreateTime(LocalDateTime.now());
            homeComic.setUpdateTime(LocalDateTime.now());
            homeComics.add(homeComic);
        }
        return homeComics;
    }


}
