package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.UserReadHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户阅读历史 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface UserReadHistoryMapper extends BaseMapper<UserReadHistory> {

}
