package io.github.comicmq.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import io.github.comicmq.common.constant.MqConstant;
import io.github.comicmq.service.ComicService;
import io.github.spidercommon.constant.ComicSaveModel;
import io.github.spidercommon.dto.MysqlDto;
import io.github.spidercommon.entity.ComicInfo;
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
import java.util.concurrent.TimeUnit;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  23:33
 * @TODO description
 */
@Component
@Slf4j
public class MysqlMqListener {

    @Autowired
    private  ObjectMapper objectMapper;
    @Autowired
    private ComicService comicService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MqConstant.MYSQL_QUEUE),
            exchange = @Exchange(name = MqConstant.COMIC_EXCHANGE, type = ExchangeTypes.DIRECT),
            key = {MqConstant.MYSQL_ROUTING_KEY}
    ))
    public void listenComicMysqlQueue(Message message, Channel channel){
        try {
            MysqlDto mysqlDto = convertMessageToObject(message);
            //漫画全量添加
            try {
                if (mysqlDto!=null && mysqlDto.getSaveModel()== ComicSaveModel.INSERT){
                    comicService.insert(mysqlDto);
                }
            }catch (Exception e){
                log.error("漫画全量添加异常：{}"+e.getMessage());
            }
            //漫画章节更新
           try {
               if (mysqlDto!=null && mysqlDto.getSaveModel()== ComicSaveModel.UPDATE){
                   comicService.update(mysqlDto);
               }
           }catch (Exception e){
               log.error("漫画章节更新异常：{}"+e.getMessage());
           }
            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("消息处理发生异常：{}", e.getMessage());
            log.error("消息不重回队列.....");
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

    private MysqlDto convertMessageToObject(Message message) throws IOException {
        byte[] body = message.getBody();
        MysqlDto mysqlDto = objectMapper.readValue(body, MysqlDto.class);
        return mysqlDto;
    }

}
