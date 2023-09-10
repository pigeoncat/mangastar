package io.github.spider.vo.tencentvo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  15:21
 * @TODO description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComicInfoVo {

    //评分
    private Double score;
    //是否已完结
    private boolean isFinish;
    //漫画描述
    private String desc;
    //起始章节号,有可能是预告啥的,点击预告好像不会加载所有章节信息
    private String chapterStartNum;
    //漫画详情页章节展示目录展示的最后一条章节的章节id
    private String lastNumInChapterList;

    public int getScoreAsInt(){
        return (int) score.doubleValue();
    }


    public void print(){
        System.err.println(".....漫画详情....");
        System.out.println("评分: "+this.score);
        System.out.println("完结: "+this.isFinish);
        System.out.println("简介: "+this.desc);
        System.out.println("章节展示目录第一页第一条: "+this.chapterStartNum);
        System.out.println("章节展示目录第一页最后一条: "+this.lastNumInChapterList);
        System.err.println(".......................................");
    }

}
