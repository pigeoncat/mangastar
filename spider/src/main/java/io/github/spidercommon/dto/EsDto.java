package io.github.spidercommon.dto;

import io.github.spidercommon.constant.ComicSaveModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  22:31
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //模式，新增还是更新
    ComicSaveModel saveModel;

    /**
     * 漫画ID
     */
    private Long comicId;

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
     * 作家名
     */
    private String authorName;

    /**
     * 漫画封面地址
     */
    private String picUrl;



    /**
     * 漫画标签
     */
    private String tags;



    /**
     * 漫画描述
     */
    private String comicDesc;


    /**
     * 总章节数
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
     * 漫画来源
     */
    private String origin;


    /**
     * 最新章节更新时间
     */
    private LocalDateTime lastChapterUpdateTime;


}
