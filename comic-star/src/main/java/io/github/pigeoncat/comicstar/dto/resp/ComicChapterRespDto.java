package io.github.pigeoncat.comicstar.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapterPicture;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 漫画章节 响应DTO
 */
@Data
@Builder
public class ComicChapterRespDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 漫画ID
     */
    @Schema(description = "漫画ID")
    private Long comicId;

    /**
     * 章节ID
     */
    @Schema(description = "章节ID")
    private Long chapterId;


    /**
     * 章节号
     */
    @Schema(description = "章节号")
    private Integer chapterNum;

    /**
     * 章节名
     */
    @Schema(description = "章节名")
    private String chapterName;

    /**
     * 章节内容描述
     */
    @Schema(description = "章节内容描述")
    private String chapterContent;

    /**
     * 章节图片数
     */
    @Schema(description = "章节图片数")
    private Integer chapterPictureCount;

    /**
     * 漫画章节图片地址
     */
    @Schema(description = "漫画章节图片地址")
    private List<ComicChapterPicture> chapterPictures;

    /**
     * 是否收费;1-收费 0-免费
     */
    @Schema(description = "是否收费;1-收费 0-免费")
    private Integer isVip;


    /**
     * 章节更新时间
     */
    @Schema(description = "章节更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime chapterUpdateTime;



}
