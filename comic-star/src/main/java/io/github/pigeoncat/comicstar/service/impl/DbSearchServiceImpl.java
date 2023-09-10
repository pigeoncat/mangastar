package io.github.pigeoncat.comicstar.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dto.req.ComicSearchReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import io.github.pigeoncat.comicstar.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据库搜索 服务实现类
 *
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "false")
@Service
@RequiredArgsConstructor
@Slf4j
public class DbSearchServiceImpl implements SearchService {

    private final ComicInfoMapper comicInfoMapper;

    @Override
    public RestResp<PageRespDto<ComicInfoRespDto>> searchComics(ComicSearchReqDto condition) {
        Page<ComicInfoRespDto> page = new Page<>();
        page.setCurrent(condition.getPageNum());
        page.setSize(condition.getPageSize());
        List<ComicInfo> comicInfos = comicInfoMapper.searchComics(page, condition);
        return RestResp.ok(
            PageRespDto.of(condition.getPageNum(), condition.getPageSize(), page.getTotal(),
                comicInfos.stream().map(v -> ComicInfoRespDto.builder()
                    .comicId(v.getId())
                    .comicName(v.getComicName())
                    .categoryId(v.getCategoryId())
                    .categoryName(v.getCategoryName())
                    .authorId(v.getAuthorId())
                    .authorName(v.getAuthorName())
                    .chapterCount(v.getChapterCount())
                    .lastChapterName(v.getLastChapterName())
                    .build()).toList()));
    }

}
