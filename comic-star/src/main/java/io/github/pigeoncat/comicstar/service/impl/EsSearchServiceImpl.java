package io.github.pigeoncat.comicstar.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.github.pigeoncat.comicstar.core.common.resp.PageRespDto;
import io.github.pigeoncat.comicstar.core.common.resp.RestResp;
import io.github.pigeoncat.comicstar.core.constant.EsConsts;
import io.github.pigeoncat.comicstar.dto.es.EsComicDto;
import io.github.pigeoncat.comicstar.dto.es.EsManga;
import io.github.pigeoncat.comicstar.dto.req.ComicSearchReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import io.github.pigeoncat.comicstar.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Elasticsearch 搜索 服务实现类
 *
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
@Slf4j
public class EsSearchServiceImpl implements SearchService {

    private final ElasticsearchClient esClient;

    @SneakyThrows
    @Override
    public RestResp<PageRespDto<ComicInfoRespDto>> searchComics(ComicSearchReqDto condition) {

        SearchResponse<EsManga> response = esClient.search(s -> {
                SearchRequest.Builder searchBuilder = s.index(EsConsts.ComicIndex.INDEX_NAME);
                // 构建检索条件
                buildSearchCondition(condition, searchBuilder);
                // 排序
                if (!StringUtils.isBlank(condition.getSort())) {
                    searchBuilder.sort(o -> o.field(f -> f
                        .field(StringUtils.underlineToCamel(condition.getSort().split(" ")[0]))
                        .order(SortOrder.Desc))
                    );
                }
                // 分页
                searchBuilder.from((condition.getPageNum() - 1) * condition.getPageSize())
                    .size(condition.getPageSize());
                // 设置高亮显示
                searchBuilder.highlight(h -> h.fields(EsConsts.ComicIndex.FIELD_COMIC_NAME,
                        t -> t.preTags("<em style='color:red'>").postTags("</em>"))
                    .fields(EsConsts.ComicIndex.FIELD_AUTHOR_NAME,
                        t -> t.preTags("<em style='color:red'>").postTags("</em>")));

                return searchBuilder;
            },
                EsManga.class
        );

        TotalHits total = response.hits().total();

        List<ComicInfoRespDto> list = new ArrayList<>();
        List<Hit<EsManga>> hits = response.hits().hits();
        for (var hit : hits) {
            EsManga comic = hit.source();
            assert comic != null;
            if (!CollectionUtils.isEmpty(hit.highlight().get(EsConsts.ComicIndex.FIELD_COMIC_NAME))) {
                comic.setComicName(hit.highlight().get(EsConsts.ComicIndex.FIELD_COMIC_NAME).get(0));
            }
            if (!CollectionUtils.isEmpty(
                hit.highlight().get(EsConsts.ComicIndex.FIELD_AUTHOR_NAME))) {
                comic.setAuthorName(
                    hit.highlight().get(EsConsts.ComicIndex.FIELD_AUTHOR_NAME).get(0));
            }
            list.add(ComicInfoRespDto.builder()
                .comicId(comic.getComicId())
                .comicName(comic.getComicName())
                .categoryId(comic.getCategoryId())
                .categoryName(comic.getCategoryName())
                .authorName(comic.getAuthorName())
                .picUrl(comic.getPicUrl())
                .comicDesc(comic.getComicDesc())
                .chapterCount(comic.getChapterCount())
                .lastChapterName(comic.getLastChapterName())
                .build());
        }
        assert total != null;
        return RestResp.ok(
            PageRespDto.of(condition.getPageNum(), condition.getPageSize(), total.value(), list));

    }



    /**
     * 构建检索条件
     */
    private void buildSearchCondition(ComicSearchReqDto condition, SearchRequest.Builder searchBuilder) {
        BoolQuery boolQuery = BoolQuery.of(b -> {
            // 关键词匹配
            if (!StringUtils.isBlank(condition.getKeyword())) {
                b.must((q -> q.multiMatch(t -> t
                    .fields(EsConsts.ComicIndex.FIELD_COMIC_NAME + "^2",
                        EsConsts.ComicIndex.FIELD_AUTHOR_NAME + "^1.8",
                        EsConsts.ComicIndex.FIELD_ALL + "^0.1")
                    .query(condition.getKeyword())
                )
                ));
            }

            // 精确查询
//            if (Objects.nonNull(condition.getWorkDirection())) {
//                b.must(TermQuery.of(m -> m
//                    .field(EsConsts.ComicIndex.FIELD_WORK_DIRECTION)
//                    .value(condition.getWorkDirection())
//                )._toQuery());
//            }
            // 精确查询
            if (Objects.nonNull(condition.getCategoryId())) {
                b.must(TermQuery.of(m -> m
                    .field(EsConsts.ComicIndex.FIELD_CATEGORY_ID)
                    .value(condition.getCategoryId())
                )._toQuery());
            }

            // 范围查询
//            if (Objects.nonNull(condition.getWordCountMin())) {
//                b.must(RangeQuery.of(m -> m
//                    .field(EsConsts.ComicIndex.FIELD_WORD_COUNT)
//                    .gte(JsonData.of(condition.getWordCountMin()))
//                )._toQuery());
//            }

//            if (Objects.nonNull(condition.getWordCountMax())) {
//                b.must(RangeQuery.of(m -> m
//                    .field(EsConsts.ComicIndex.FIELD_WORD_COUNT)
//                    .lt(JsonData.of(condition.getWordCountMax()))
//                )._toQuery());
//            }

//            if (Objects.nonNull(condition.getUpdateTimeMin())) {
//                b.must(RangeQuery.of(m -> m
//                    .field(EsConsts.ComicIndex.FIELD_LAST_CHAPTER_UPDATE_TIME)
//                    .gte(JsonData.of(condition.getUpdateTimeMin().getTime()))
//                )._toQuery());
//            }

            return b;

        });

        searchBuilder.query(q -> q.bool(boolQuery));

    }
}
