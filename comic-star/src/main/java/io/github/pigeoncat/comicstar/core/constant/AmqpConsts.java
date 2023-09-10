package io.github.pigeoncat.comicstar.core.constant;

/**
 * AMQP 相关常量
 *
 */
public class AmqpConsts {

    /**
     * 漫画信息改变 MQ
     */
    public static class ComicChangeMq {

        /**
         * 信息改变交换机
         */
        public static final String EXCHANGE_NAME = "EXCHANGE-COMIC-CHANGE";

        /**
         * Elasticsearch comic 索引更新的队列
         */
        public static final String QUEUE_ES_UPDATE = "QUEUE-ES-COMIC-UPDATE";

        /**
         * Redis comic 缓存更新的队列
         */
        public static final String QUEUE_REDIS_UPDATE = "QUEUE-REDIS-COMIC-UPDATE";

    }

}
