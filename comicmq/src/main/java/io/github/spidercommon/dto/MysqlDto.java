package io.github.spidercommon.dto;


import io.github.spidercommon.constant.ComicSaveModel;
import io.github.spidercommon.entity.ComicInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author pigeoncat
 * @Date 2023/09/04  22:31
 * @TODO description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MysqlDto implements Serializable {

    private static final long serialVersionUID = 1L;

    //模式，新增还是更新
    ComicSaveModel saveModel;

    //漫画详情
    private ComicInfo comicInfo;

    //章节集合
    private List<ChapterDto> chapterDtoList;


}
