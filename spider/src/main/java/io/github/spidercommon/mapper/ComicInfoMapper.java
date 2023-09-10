package io.github.spidercommon.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.spidercommon.entity.ComicInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 漫画信息 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface ComicInfoMapper extends BaseMapper<ComicInfo> {
}
