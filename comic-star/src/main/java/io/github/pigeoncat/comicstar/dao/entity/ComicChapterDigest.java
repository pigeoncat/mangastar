package io.github.pigeoncat.comicstar.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 漫画章节列表 响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComicChapterDigest implements Serializable {

    private static final long serialVersionUID = 1L;

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


}
