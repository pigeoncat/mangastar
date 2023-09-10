package io.github.spider.vo.tencentvo;

import cn.hutool.core.util.ObjectUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  13:22
 * @TODO description
 */
@Data
@Builder
public class ComicItemVo {
    //封面url
    private String coverUrl;
    //更新至几话或全多少话
    private String chapterCount;
    //漫画详情url后缀
    private String comicInfoUrl;
    //漫画名称
    private String comicName;
    //签约(可以没有)
    private String comicSign;
    //独家(可以没有)
    private String comicExclusive;
    //漫画作者--笔名
    private String author;
    //作品标签，热血、人气、妖怪....
    private List<String> comicTags;
    //人气---多少多少亿或者万
    private String popularity;
    //漫画简介
    private String comicDesc;

    //信息不完整的漫画就不爬取  检查需要的数据是否都有
    public boolean isDataComplete(){
        return ObjectUtil.isNotEmpty(coverUrl) &&
                ObjectUtil.isNotEmpty(chapterCount) &&
                ObjectUtil.isNotEmpty(comicInfoUrl) &&
                ObjectUtil.isNotEmpty(comicName) &&
                ObjectUtil.isNotEmpty(author) &&
                ObjectUtil.isNotEmpty(comicTags) &&
                ObjectUtil.isNotEmpty(popularity) &&
                ObjectUtil.isNotEmpty(comicDesc);
    }

    public int getChapterCountAsInt(){
        if (chapterCount==null){
            return 0;
        }
        // 使用正则表达式提取数字
        String digits = chapterCount.replaceAll("\\D+", "");
        return Integer.valueOf(digits);
    }

    public long getPopularityAsLong(){
        if (popularity==null){
            return 0;
        }
        // 使用正则表达式提取浮点数
        String digits = popularity.replaceAll("[^\\d.]+", "");
        double popu = Double.parseDouble(digits);
        if (popularity.contains("亿")){
            popu *= 100000000;
        }else if(popularity.contains("万")){
            popu *= 10000;
        }
        return (long) popu;
    }

    public String getTagsAsString(){
        StringBuilder builder = new StringBuilder();
        for (String tag : comicTags) {
            builder.append(tag+",");
        }
        return builder.toString();
    }


    public void print(){
        System.err.println(".....漫画展示条目....");
        System.out.println("封面: "+this.coverUrl);
        System.out.println("几话: "+this.chapterCount);
        System.out.println("后缀: "+this.comicInfoUrl);
        System.out.println("名称: "+this.comicName);
        System.out.println("签约: "+this.comicSign);
        System.out.println("独家: "+this.comicExclusive);
        System.out.println("作者: "+this.author);
        System.out.println("标签: "+this.comicTags);
        System.out.println("人气: "+this.popularity);
        System.out.println("描述: "+this.comicDesc);
        System.err.println(".......................................");
    }

}

//封面url
//            ./div[@class="ret-works-cover"]/a/img/@src
//更新至几话或全多少话
//            ./div[@class="ret-works-cover"]//span[@class="mod-cover-list-text"]

//漫画详情url后缀
//            ./div[@class="ret-works-info"]/h3/a/@href
//漫画名称
//            ./div[@class="ret-works-info"]/h3/a/text()
//签约
//            ./div[@class="ret-works-info"]/h3/i[@class="ui-icon-sign"]
//独家
//            ./div[@class="ret-works-info"]/h3/i[@class="ui-icon-exclusive"]
//漫画作者--笔名
//            ./div[@class="ret-works-info"]/p[@class="ret-works-author"]
//作品标签，热血、人气、妖怪....
//            ./div[@class="ret-works-info"]/p[@class="ret-works-tags"]/span[@target="_blank"]
//人气---多少多少亿或者万
//            ./div[@class="ret-works-info"]/p[@class="ret-works-tags"]//em
//漫画描述
//            ./div[@class="ret-works-info"]/p[@class="ret-works-decs"]
