package io.github.spider.util.idgenerator;

/**
 * @Author pigeoncat
 * 雪花算法全局id生成器
 * @Date 2023/09/02
 * @TODO description
 */
public class SnowflakeIdGenerator {
    //应用程序起始时间
    private static final long START_TIMESTAMP = 1630521600000L; // 2021-09-02 00:00:00 UTC

    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);
    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    //机器id
    private final long workerId;
    //机房id
    private final long dataCenterId;
    //序列号
    private long sequence = 0L;
    //上一次生成id的时间戳，不一定和真实时间同步，只是不断增长
    private long lastTimestamp = START_TIMESTAMP;

    //单例
    private static volatile SnowflakeIdGenerator idGenerator;

    private SnowflakeIdGenerator(long workerId, long dataCenterId) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    public static SnowflakeIdGenerator getSingletonIdGenerator(long workerId, long dataCenterId){
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("Worker ID can't be greater than " + MAX_WORKER_ID + " or less than 0");
        }
        if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException("Data Center ID can't be greater than " + MAX_DATA_CENTER_ID + " or less than 0");
        }
        //双检锁
        if (idGenerator==null){
            synchronized (SnowflakeIdGenerator.class){
                if (idGenerator==null){
                    idGenerator=new SnowflakeIdGenerator(workerId,dataCenterId);
                }
            }
        }
        return idGenerator;
    }

    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();
        //发生时钟回拨
        if (currentTimestamp < lastTimestamp) {
            //变更当前时间戳为上一次毫秒时间戳
            currentTimestamp=lastTimestamp;
        }
        //正常情况(即使发生时钟回拨，只要没拨到上一次生成id时刻的前边，就算是正常，因为不会有id重复，当前节点id也是升序增长的)
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & ((1 << SEQUENCE_BITS) - 1);
            //如果4096个序列号用完
            if (sequence == 0) {
                //再加一毫秒
                currentTimestamp++;
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

}