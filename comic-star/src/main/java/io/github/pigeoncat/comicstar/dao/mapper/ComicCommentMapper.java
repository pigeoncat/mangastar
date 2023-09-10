package io.github.pigeoncat.comicstar.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.ComicComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 漫画评论 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface ComicCommentMapper extends BaseMapper<ComicComment> {

}
