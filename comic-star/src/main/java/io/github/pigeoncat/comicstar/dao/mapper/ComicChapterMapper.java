package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.ComicChapter;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 漫画章节 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface ComicChapterMapper extends BaseMapper<ComicChapter> {

    /**
     * 把大于指定章节号的所有章节减一
     * @param chapterNum
     */
    void allChapterNumGtNumSubtractOne(Integer chapterNum);

    /**
     * 把大于指定章节号的所有章节的图片信息章节号减一
     * @param chapterNum
     */
    void allPictureChapterNumGtNumSubtractOne(Integer chapterNum);

}
