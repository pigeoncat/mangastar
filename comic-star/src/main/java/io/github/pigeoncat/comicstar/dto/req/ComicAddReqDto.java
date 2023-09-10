package io.github.pigeoncat.comicstar.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 漫画发布 请求DTO
 *
 */
@Data
public class ComicAddReqDto {

    /**
     * 作品方向;0-男频 1-女频
     */
    @Schema(description = "作品方向;0-男频 1-女频", required = true)
    @NotNull
    private Integer workDirection;

    /**
     * 类别ID
     */
    @Schema(description = "类别ID", required = true)
    @NotNull
    private Long categoryId;

    /**
     * 类别名
     */
    @Schema(description = "类别名", required = true)
    @NotBlank
    private String categoryName;

    /**
     * 漫画封面地址
     */
    @Schema(description = "漫画封面地址", required = true)
    @NotBlank
    private String picUrl;

    /**
     * 漫画名
     */
    @Schema(description = "漫画名", required = true)
    @NotBlank
    private String comicName;

    /**
     * 漫画描述
     */
    @Schema(description = "漫画描述", required = true)
    @NotBlank
    private String comicDesc;

    /**
     * 漫画标签
     */
    @Schema(description = "漫画标签", required = true)
    @NotBlank
    private List<String> tags;

    /**
     * 是否收费;1-收费 0-免费
     */
    @Schema(description = "是否收费;1-收费 0-免费", required = true)
    @NotNull
    private Integer isVip;

    /**
     * 动漫状态;0-连载中 1-已完结
     */
    @Schema(description = "动漫状态;0-连载中 1-已完结", required = true)
    @NotNull
    private Integer comicStatus;

    //tags标签转换成string
    public String getTagsString(){
        StringBuilder tagsBuilder = new StringBuilder();
        int size = this.tags.size();
        for (int i = 0; i < size; i++) {
            if (i==size-1){
                tagsBuilder.append(tags.get(i));
            }else {
                tagsBuilder.append(tags.get(i)+",");
            }
        }
        return tagsBuilder.toString();
    }

}
