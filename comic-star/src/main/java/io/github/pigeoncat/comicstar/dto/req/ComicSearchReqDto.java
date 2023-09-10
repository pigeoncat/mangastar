package io.github.pigeoncat.comicstar.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.pigeoncat.comicstar.core.common.req.PageReqDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 漫画搜索 请求DTO
 *
 */
@Data
public class ComicSearchReqDto extends PageReqDto {

    /**
     * 搜索关键字
     */
    @Parameter(description = "搜索关键字")
    private String keyword;

    /**
     * 作品方向
     */
    @Parameter(description = "作品方向")
    private Integer workDirection;

    /**
     * 分类ID
     */
    @Parameter(description = "分类ID")
    private Integer categoryId;

    /**
     * 是否收费，1：收费，0：免费
     */
    @Parameter(description = "是否收费，1：收费，0：免费")
    private Integer isVip;

    /**
     * 更新状态，0：连载中，1：已完结
     */
    @Parameter(description = "漫画更新状态，0：连载中，1：已完结")
    private Integer comicStatus;


    /**
     * 最小更新时间
     * 如果使用Get请求，直接使用对象接收，则可以使用@DateTimeFormat注解进行格式化；
     * 如果使用Post请求，@RequestBody接收请求体参数，默认解析日期格式为yyyy-MM-dd HH:mm:ss ,
     * 如果需要接收其他格式的参数，则可以使用@JsonFormat注解
     * */
    @Parameter(description = "最小更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeMin;

    /**
     * 排序字段
     */
    @Parameter(description = "排序字段")
    private String sort;
}
