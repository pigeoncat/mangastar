package io.github.pigeoncat.comicstar.core.config;

import io.github.pigeoncat.comicstar.core.constant.AmqpConsts;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AMQP 配置类
 *
 */
@Configuration
public class AmqpConfig {

    /**
     * 漫画信息改变交换机
     */
    @Bean
    public FanoutExchange comicChangeExchange() {
        return new FanoutExchange(AmqpConsts.ComicChangeMq.EXCHANGE_NAME);
    }

    /**
     * Elasticsearch comic 索引更新队列
     */
    @Bean
    public Queue esComicUpdateQueue() {
        return new Queue(AmqpConsts.ComicChangeMq.QUEUE_ES_UPDATE);
    }

    /**
     * Elasticsearch comic 索引更新队列绑定到漫画信息改变交换机
     */
    @Bean
    public Binding esComicUpdateQueueBinding() {
        return BindingBuilder.bind(esComicUpdateQueue()).to(comicChangeExchange());
    }

}
