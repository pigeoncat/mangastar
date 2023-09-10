package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.UserPayLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户充值记录 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface UserPayLogMapper extends BaseMapper<UserPayLog> {

}
