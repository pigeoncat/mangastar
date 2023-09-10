package io.github.comicmq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import io.github.comicmq.common.constant.MqConstant;
import io.github.comicmq.entity.EsManga;
import io.github.comicmq.service.EsSearchService;
import io.github.spidercommon.constant.ComicSaveModel;
import io.github.spidercommon.dto.EsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  23:33
 * @TODO description
 */
@Component
@Slf4j
public class EsMqListener {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EsSearchService esSearchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstant.ES_QUEUE),
            exchange = @Exchange(name = MqConstant.COMIC_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = {MqConstant.ES_ROUTING_KEY}
    ))
    public void listenComicEsQueue(Message message, Channel channel){
        try {
            EsDto esDto = convertMessageToObject(message);
            EsManga esManga = getEsMangaFromDto(esDto);
            //新增到es
            if (esDto!=null && esDto.getSaveModel()== ComicSaveModel.INSERT){
                esSearchService.insert(esManga);
            }
            //更新es内容
            if (esDto!=null && esDto.getSaveModel()== ComicSaveModel.UPDATE){
                esSearchService.update(esManga);
            }
            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消息处理发生异常：{}", e.getMessage());
            log.error("消息不重回队列......");
            //不要开启,容易出问题
//            try {
//                // 消费失败，将消息重新放回队列
//                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//            } catch (IOException ex) {
//                log.error("消息重新放回队列失败...");
//                log.error(ex.getMessage());
//            }
        }
    }

    private EsDto convertMessageToObject(Message message) throws IOException {
        byte[] body = message.getBody();
        EsDto esDto = objectMapper.readValue(body, EsDto.class);
        return esDto;
    }

    private EsManga getEsMangaFromDto(EsDto esDto){
        return EsManga.builder()
                .comicId(esDto.getComicId())
                .categoryId(esDto.getCategoryId())
                .categoryName(esDto.getCategoryName())
                .comicName(esDto.getComicName())
                .authorName(esDto.getAuthorName())
                .picUrl(esDto.getPicUrl())
                .tags(esDto.getTags())
                .comicDesc(esDto.getComicDesc())
                .chapterCount(esDto.getChapterCount())
                .lastChapterNum(esDto.getLastChapterNum())
                .lastChapterName(esDto.getLastChapterName())
                .origin(esDto.getOrigin())
                .build();
    }


}
