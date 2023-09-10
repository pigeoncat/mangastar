package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.UserCommentReply;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户评论回复 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface UserCommentReplyMapper extends BaseMapper<UserCommentReply> {

}
