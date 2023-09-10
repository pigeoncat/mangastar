package io.github.spider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.spider.common.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  18:28
 * @TODO description
 */
@Configuration
@Slf4j
public class MqConfig {

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 配置消息转换器
     * 需要把JacksonConfig中配置的解决了时间序列化问题的序列化器配置给mq
     * 不然mq消息会发送失败，因为转换异常，是由LocalDateTime引起的
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    /**
     * 声明交换机
     * @return Direct类型交换机
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MqConstant.COMIC_EXCHANGE);
    }

    /**
     * MySql漫画信息存储队列
     * @return
     */
    @Bean
    public Queue comicMysqlQueue(){
        return new Queue(MqConstant.MYSQL_QUEUE);
    }


    /**
     * ES漫画信息存储队列
     * @return
     */
    @Bean
    public Queue comicEsQueue(){
        return new Queue(MqConstant.ES_QUEUE);
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public Binding bindingComicPictureQueue(Queue comicMysqlQueue, DirectExchange directExchange){
        return BindingBuilder.bind(comicMysqlQueue).to(directExchange).with(MqConstant.MYSQL_ROUTING_KEY);
    }

    @Bean
    public Binding bindingComicEsQueue(Queue comicEsQueue, DirectExchange directExchange){
        return BindingBuilder.bind(comicEsQueue).to(directExchange).with(MqConstant.ES_ROUTING_KEY);
    }

    /**
     * 配置消息手动确认
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        //设置消息转换器,一定要配,不然还是用默认的转换器
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        // 确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                assert correlationData != null;
                log.info("消息确认送到交换机(Exchange)，消息的唯一标识符：{}", correlationData.getId());
            } else {
                log.info("投递失败，错误原因 ：{}", cause);
                log.info("尝试重发...");
                String exchange = correlationData.getReturned().getExchange();
                String routingKey = correlationData.getReturned().getRoutingKey();
                Message message = correlationData.getReturned().getMessage();
                try {
                    rabbitTemplate.convertAndSend(exchange,routingKey,message,correlationData);
                    log.info("消息重新发送成功");
                }catch (Exception e){
                    log.error("消息重新发送失败: {}", e.getMessage());
                }
            }
        });
        return rabbitTemplate;
    }



}
