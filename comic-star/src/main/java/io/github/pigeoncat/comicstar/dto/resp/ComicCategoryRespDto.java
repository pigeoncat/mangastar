package io.github.pigeoncat.comicstar.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 漫画分类 响应DTO
 *
 */
@Data
@Builder
public class ComicCategoryRespDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类别ID
     */
    @Schema(description = "类别ID")
    private Long id;

    /**
     * 类别名
     */
    @Schema(description = "类别名")
    private String name;

}
