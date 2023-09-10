package io.github.pigeoncat.comicstar.core.listener;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import io.github.pigeoncat.comicstar.core.constant.AmqpConsts;
import io.github.pigeoncat.comicstar.core.constant.EsConsts;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dao.mapper.ComicInfoMapper;
import io.github.pigeoncat.comicstar.dto.es.EsComicDto;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Rabbit 队列监听器
 *
 */
@Component
@ConditionalOnProperty(prefix = "spring", name = {"elasticsearch.enabled",
    "amqp.enabled"}, havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class RabbitQueueListener {

    private final ComicInfoMapper comicInfoMapper;

    private final ElasticsearchClient esClient;

    /**
     * 监听漫画信息改变的 ES 更新队列，更新最新漫画信息到 ES
     */
    @RabbitListener(queues = AmqpConsts.ComicChangeMq.QUEUE_ES_UPDATE)
    @SneakyThrows
    public void updateEsComic(Long comicId) {
        ComicInfo comicInfo = comicInfoMapper.selectById(comicId);
        IndexResponse response = esClient.index(i -> i
            .index(EsConsts.ComicIndex.INDEX_NAME)
            .id(comicInfo.getId().toString())
            .document(EsComicDto.build(comicInfo))
        );
        log.info("Indexed with version " + response.version());
    }

}
