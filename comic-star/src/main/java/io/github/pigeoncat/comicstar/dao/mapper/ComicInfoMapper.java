package io.github.pigeoncat.comicstar.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.pigeoncat.comicstar.dao.entity.ComicInfo;
import io.github.pigeoncat.comicstar.dto.req.ComicSearchReqDto;
import io.github.pigeoncat.comicstar.dto.resp.ComicInfoRespDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 漫画信息 Mapper 接口
 * </p>
 *
 */
@Mapper
public interface ComicInfoMapper extends BaseMapper<ComicInfo> {

    /**
     * 增加漫画点击量
     *
     * @param ComicId 漫画ID
     */
    void addVisitCount(@Param("ComicId") Long ComicId);

    /**
     * 漫画搜索
     * @param page mybatis-plus 分页对象
     * @param condition 搜索条件
     * @return 返回结果
     * */
    List<ComicInfo> searchComics(IPage<ComicInfoRespDto> page, ComicSearchReqDto condition);

}
