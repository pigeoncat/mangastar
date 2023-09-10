package io.github.pigeoncat.comicstar.dto.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;

import java.time.ZoneOffset;

/**
 * Elasticsearch 存储漫画 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsComicDto {

    /**
     * 漫画id
     */
    private Long comicId;

    /**
     * 作品方向;0-男频 1-女频
     */
    private Integer workDirection;

    /**
     * 类别ID
     */
    private Long categoryId;

    /**
     * 类别名
     */
    private String categoryName;

    /**
     * 漫画名
     */
    private String comicName;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 漫画描述
     */
    private String comicDesc;

    /**
     * 评分;总分:10 ，真实评分 = score/10
     */
    private Integer score;

    /**
     * 漫画状态;0-连载中 1-已完结
     */
    private Integer comicStatus;

    /**
     * 点击量
     */
    private Long visitCount;


    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 章节数
     */
    private Integer chapterCount;

    /**
     * 最新章节号
     */
    private Integer lastChapterNum;

    /**
     * 最新章节名
     */
    private String lastChapterName;

    /**
     * 最新章节更新时间
     */
    private Long lastChapterUpdateTime;

    /**
     * 是否收费;1-收费 0-免费
     */
    private Integer isVip;

    public static EsComicDto build(ComicInfo comicInfo){
        return EsComicDto.builder()
                .comicId(comicInfo.getId())
                .categoryId(comicInfo.getCategoryId())
                .categoryName(comicInfo.getCategoryName())
                .comicDesc(comicInfo.getComicDesc())
                .comicName(comicInfo.getComicName())
                .authorId(comicInfo.getAuthorId())
                .authorName(comicInfo.getAuthorName())
                .comicStatus(comicInfo.getComicStatus())
                .commentCount(comicInfo.getCommentCount())
                .isVip(comicInfo.getIsVip())
                .score(comicInfo.getScore())
                .visitCount(comicInfo.getVisitCount())
                .chapterCount(comicInfo.getChapterCount())
                .workDirection(comicInfo.getWorkDirection())
                .lastChapterNum(comicInfo.getLastChapterNum())
                .lastChapterName(comicInfo.getLastChapterName())
                .lastChapterUpdateTime(comicInfo.getLastChapterUpdateTime()
                        .toInstant(ZoneOffset.ofHours(8)).toEpochMilli())
                .build();
    }

}
