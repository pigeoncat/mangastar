package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.AuthorIncome;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 稿费收入统计 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface AuthorIncomeMapper extends BaseMapper<AuthorIncome> {

}
