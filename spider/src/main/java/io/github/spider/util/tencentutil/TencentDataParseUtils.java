package io.github.spider.util.tencentutil;

import cn.hutool.core.text.UnicodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.spider.vo.tencentvo.Picture;
import io.github.spider.util.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  21:05
 * @TODO description
 */
public class TencentDataParseUtils {
    //获取DATA数据
    public static String getVarData(String doc){
        String pattern = "var DATA = '(.*?)',";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(doc);
        if (matcher.find()) {
            String data = matcher.group(1);
            return data;
        } else {
            return null;
        }
    }
    //将DATA正确地解码
    public static String base64Decode(String data){
        if (data==null){
            return null;
        }
        String target = null;
        for (int i = 0; i < data.length(); i++) {
            String substring = data.substring(i);
            try {
                byte[] decode = Base64.getDecoder().decode(substring);
                target = new String(decode, StandardCharsets.UTF_8);
            }catch (IllegalArgumentException e){
                continue;
            }catch (Exception e){
                continue;
            }
            //解析没有出错则判断有无正确的数据，正常数据应该包含chapter
            if (target!=null && !target.contains("chapter")){
                continue;
            }
            break;
        }
        return target;
    }

    //获取章节名
    public static String getChapterName(String data){
        if (data==null){
            return null;
        }
        String cTitle = toJsonNode(data).get("chapter").get("cTitle").asText();
        //将unicode转换成中文返回
        return UnicodeUtil.toString(cTitle);
    }

    //获取章节id
    public static Integer getChapterId(String data){
        return data==null?null: toJsonNode(data).get("chapter").get("cid").asInt();
    }

    //获取章节序号，第几章节
    public static Integer getChapterSeq(String data){
        return data==null?null:toJsonNode(data).get("chapter").get("cSeq").asInt();
    }

    //获取章节图片列表
    public static List<Picture> getChapterPictures(String data){
        if (data==null){
            return null;
        }
        JsonNode pictureArrayNode  = toJsonNode(data).get("picture");
        ArrayList<Picture> pictures = new ArrayList<Picture>();
        for (JsonNode pictureNode : pictureArrayNode) {
            Long pid = pictureNode.get("pid").asLong();
            Integer width = pictureNode.get("width").asInt();
            Integer height = pictureNode.get("height").asInt();
            String url = pictureNode.get("url").asText();

            Picture picture = new Picture(pid, width, height, url);
            pictures.add(picture);
        }
        return pictures;
    }

    public static String toValidJsonString(String data){
        if (data==null){
            return null;
        }
        int index = data.indexOf("chapter");
        return "{"+data.substring(index-1);
    }

    public static JsonNode toJsonNode(String data){
        if (data==null){
            return null;
        }
        String jsonString = toValidJsonString(data);
        JsonNode jsonNode = JsonUtils.stringToJson(jsonString);
        return jsonNode;
    }

}
