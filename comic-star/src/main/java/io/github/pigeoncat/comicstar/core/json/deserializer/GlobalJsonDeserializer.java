package io.github.pigeoncat.comicstar.core.json.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;


/**
 * JSON 全局反序列化器
 *
 */
@JsonComponent
public class GlobalJsonDeserializer {

    /**
     * 字符串反序列化器：过滤特殊字符，解决 XSS 攻击
     */
    public static class StringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {
            return jsonParser.getValueAsString()
                .replace("<", "&lt;")
                .replace(">", "&gt;");
        }
    }
}
