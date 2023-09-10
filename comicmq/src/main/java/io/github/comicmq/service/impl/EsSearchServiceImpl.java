package io.github.comicmq.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.comicmq.common.constant.EsConstant;
import io.github.comicmq.entity.EsManga;
import io.github.comicmq.service.EsSearchService;
import io.github.comicmq.util.idgenerator.SnowflakeIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  12:48
 * @TODO description
 */
@ConditionalOnProperty(prefix = "spring.elasticsearch", name = "enabled", havingValue = "true")
@Service
@RequiredArgsConstructor
@Slf4j
public  class EsSearchServiceImpl implements EsSearchService {

    private  final ElasticsearchClient esClient;

    private final SnowflakeIdGenerator idWorker;


    @Override
    public void insert(EsManga esManga) {
        try {
            if (!isExist(esManga)){
                esClient.index(req -> req
                        .index(EsConstant.MANGA_INDEX)
                        .id(String.valueOf(idWorker.nextId()))
                        .document(esManga)
                );
            }
        } catch (IOException e) {
            log.error("新增漫画出现异常：{}"+e.getMessage());
        }
    }

    @Override
    public void update(EsManga esManga) {
        try {
            //查询单个
            List<Hit<EsManga>> hits = searchByNameAndOrigin(esManga.getComicName(), esManga.getOrigin());
            //删除
            String id=null;
            for (Hit<EsManga> h : hits) {
                id = h.id();
                try {
                    esClient.delete(d -> d.index(EsConstant.MANGA_INDEX).id(h.id()));
                } catch (IOException e) {
                    log.error("es删除漫画失败，{}",e.getMessage());
                }
            }
           //更新
            if (id!=null){
                try {
                    String finalId = id;
                    esClient.index(i -> i
                            .index(EsConstant.MANGA_INDEX)
                            .id(finalId)
                            .document(esManga)
                    );
                }catch (IOException e) {
                    log.error("es更新漫画失败，{}",e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("es根据漫画名和爬虫源更新数据失败，{}",e.getMessage());
        }
    }

    @Override
    public List<Hit<EsManga>> searchByNameAndOrigin(String comicName, String origin) throws IOException {
        //查询单个
        SearchResponse<EsManga> response = esClient.search(s -> s
                        .index(EsConstant.MANGA_INDEX)
                        .query(q -> q
                                .bool(b -> b
                                        .must(m -> m
                                                .term(t -> t.field(EsConstant.COMIC_NAME).value(FieldValue.of(comicName))))
                                        .must(m -> m
                                                .term(t -> t.field(EsConstant.ORIGIN).value(FieldValue.of(origin))))
                                )
                        ),
                EsManga.class
        );
        return response.hits().hits();
    }

    @Override
    public boolean isExist(EsManga esManga) throws IOException {
        if (ObjectUtils.isEmpty(esManga)){
            throw new NullPointerException("参数不能为空");
        }
        List<Hit<EsManga>> esMangas = searchByNameAndOrigin(esManga.getComicName(), esManga.getOrigin());
        if (esMangas.size()>0){
            return true;
        }
        return false;
    }


}
























