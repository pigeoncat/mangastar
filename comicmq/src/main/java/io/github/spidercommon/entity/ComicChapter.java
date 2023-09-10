package io.github.spidercommon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 漫画章节
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comic_chapter")
public class ComicChapter implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 漫画ID
     */
    private Long comicId;

    /**
     * 章节号
     */
    private Integer chapterNum;

    /**
     * 章节名
     */
    private String chapterName;

    /**
     * 章节描述
     */
    private String chapterDesc;

    /**
     * 漫画页数
     */
    private Integer pageCount;

    /**
     * 是否收费;1-收费 0-免费
     */
    private Integer isVip;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;


}
