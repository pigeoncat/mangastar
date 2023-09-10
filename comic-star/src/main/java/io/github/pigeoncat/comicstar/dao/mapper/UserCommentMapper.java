package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.UserComment;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户评论 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface UserCommentMapper extends BaseMapper<UserComment> {

}
