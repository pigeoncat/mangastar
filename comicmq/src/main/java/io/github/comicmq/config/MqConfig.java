package io.github.comicmq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        return new DirectExchange("comic");
    }

    /**
     * MySql漫画信息存储队列
     * @return
     */
    @Bean
    public Queue comicMysqlQueue(){
        return new Queue("comic.mysql.queue");
    }


    /**
     * ES漫画信息存储队列
     * @return
     */
    @Bean
    public Queue comicEsQueue(){
        return new Queue("comic.es.queue");
    }

    /**
     * 绑定队列和交换机
     * @return
     */
    @Bean
    public BindingBuilder.DirectExchangeRoutingKeyConfigurer bindingComicPictureQueue(Queue comicMysqlQueue, DirectExchange directExchange){
        return BindingBuilder.bind(comicMysqlQueue).to(directExchange);
    }

    @Bean
    public BindingBuilder.DirectExchangeRoutingKeyConfigurer bindingComicEsQueue(Queue comicEsQueue, DirectExchange directExchange){
        return BindingBuilder.bind(comicEsQueue).to(directExchange);
    }




}
