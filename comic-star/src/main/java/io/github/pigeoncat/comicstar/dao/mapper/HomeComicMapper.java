package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.HomeComic;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 漫画推荐 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface HomeComicMapper extends BaseMapper<HomeComic> {

}
