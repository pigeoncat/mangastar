package io.github.pigeoncat.comicstar.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 首页漫画推荐 响应DTO
 */
@Data
public class HomeComicRespDto {

    /**
     * 类型;0-轮播图 1-顶部栏 2-本周强推 3-热门推荐 4-精品推荐
     */
    @Schema(description = "类型;0-轮播图 1-顶部栏 2-本周强推 3-热门推荐 4-精品推荐")
    private Integer type;


    /**
     * 漫画ID
     */
    @Schema(description = "漫画ID")
    private Long comicId;

    /**
     * 漫画名
     */
    @Schema(description = "漫画名")
    private String comicName;

    /**
     * 类别ID
     */
    @Schema(description = "类别ID")
    private Long categoryId;


    /**
     * 类别名
     */
    @Schema(description = "类别名")
    private String categoryName;

    /**
     * 作家名
     */
    @Schema(description = "作家名")
    private String authorName;

    /**
     * 漫画封面地址
     */
    @Schema(description = "漫画封面地址")
    private String picUrl;


    /**
     * 漫画标签
     */
    @Schema(description = "漫画标签")
    private List<String> tags;

    /**
     * 漫画描述
     */
    @Schema(description = "漫画描述")
    private String comicDesc;

    /**
     * 总章节数
     */
    @Schema(description = "总章节数")
    private Integer chapterCount;

    /**
     * 最新章节号
     */
    @Schema(description = "最新章节号")
    private Integer lastChapterNum;


    /**
     * 最新章节名
     */
    @Schema(description = "最新章节名")
    private String lastChapterName;


    /**
     * 最新章节更新时间
     */
    @Schema(description = "最新章节更新时间")
    @JsonFormat(pattern = "MM/dd HH:mm")
    private LocalDateTime lastChapterUpdateTime;

}
