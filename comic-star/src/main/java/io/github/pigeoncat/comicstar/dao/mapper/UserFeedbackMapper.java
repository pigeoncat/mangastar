package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.UserFeedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户反馈 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface UserFeedbackMapper extends BaseMapper<UserFeedback> {

}
