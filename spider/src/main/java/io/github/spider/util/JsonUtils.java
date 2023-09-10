package io.github.spider.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  21:39
 * @TODO description
 */
public class JsonUtils {
    private static ObjectMapper objectMapper=new ObjectMapper();

    private JsonUtils() {
    }

    public static JsonNode stringToJson(String data) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

}
