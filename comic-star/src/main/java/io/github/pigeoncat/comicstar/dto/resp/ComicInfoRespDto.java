package io.github.pigeoncat.comicstar.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapterDigest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 漫画信息 响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComicInfoRespDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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
     * 漫画描述
     */
    @Schema(description = "漫画描述")
    private String comicDesc;

    /**
     * 漫画状态;0-连载中 1-已完结
     */
    @Schema(description = "漫画状态;0-连载中 1-已完结")
    private Integer comicStatus;


    /**
     * 作家id
     */
    @Schema(description = "作家id")
    private Long authorId;

    /**
     * 作家名
     */
    @Schema(description = "作家名")
    private String authorName;


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
     * 点击量
     */
    @Schema(description = "点击量")
    private Long visitCount;

    /**
     * 总章节数
     */
    @Schema(description = "总章节数")
    private Integer chapterCount;

    /**
     * 评论数
     */
    @Schema(description = "评论数")
    private Integer commentCount;

    /**
     * 漫画封面地址
     */
    @Schema(description = "漫画封面地址")
    private String picUrl;

    /**
     * 所有章节列表
     */
    @Schema(description = "所有章节列表")
    private List<ComicChapterDigest> allChapterList;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;


}
