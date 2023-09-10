package io.github.comicmq.service;

import co.elastic.clients.elasticsearch.core.search.Hit;
import io.github.comicmq.entity.EsManga;

import java.io.IOException;
import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/09/05  14:36
 * @TODO description
 */
public interface EsSearchService {

    /**
     * 新增
     * @param esManga
     */
    void insert(EsManga esManga);

    /**
     * 修改
     * @param esManga
     */
    void update(EsManga esManga);

    /**
     * 通过名称和爬虫来源查找漫画
     * @return
     */
    List<Hit<EsManga>> searchByNameAndOrigin(String comicName, String origin) throws IOException;

    /**
     * 检查是否存在
     * @return
     */
    boolean isExist(EsManga esManga) throws IOException;

}
