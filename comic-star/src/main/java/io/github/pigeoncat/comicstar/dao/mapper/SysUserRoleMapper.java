package io.github.pigeoncat.comicstar.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.pigeoncat.comicstar.dao.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户与角色对应关系 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
