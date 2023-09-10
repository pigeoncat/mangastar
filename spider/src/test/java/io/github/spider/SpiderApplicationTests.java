package io.github.spider;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.github.spider.util.idgenerator.SnowflakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootTest
class SpiderApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SnowflakeIdGenerator idWorker;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime=LocalDateTime.now();

    @Test
    void mqTest(){
        // 交换机名称
        String exchangeName = "comic";
        // 消息
        String message = "红色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "mysql", message);
    }

    @Test
    void jasonTest() throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
//        var s = objectMapper.writeValueAsString(createTime);
        var s = objectMapper.writeValueAsString(new Date());
        System.out.println(s);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void idWorkerTest(){
        var set = new HashSet<Long>();
        int count=0;
        for (int i = 0; i < 1000; i++) {
            var isFinish = set.add(idWorker.nextId());
            if (isFinish==false){
                System.out.println(false);
                count++;
            }
        }
        System.out.println(count);
    }


}
