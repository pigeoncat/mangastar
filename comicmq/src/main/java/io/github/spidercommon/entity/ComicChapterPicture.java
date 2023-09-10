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
 * 漫画章节图片地址
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comic_chapter_picture")
public class ComicChapterPicture implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 漫画ID
     */
    private Long comicId;

    /**
     * 漫画章节号
     */
    private Integer chapterNum;

    /**
     * 漫画章节图片序号
     */
    private Integer sort;

    /**
     * 漫画图片地址
     */
    private String url;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
