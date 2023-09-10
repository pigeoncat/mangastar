package io.github.spidercommon.dto;

import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  22:33
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChapterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //章节
    private ComicChapter comicChapter;

    //图片集合
    private List<ComicChapterPicture> pictureList;

}
