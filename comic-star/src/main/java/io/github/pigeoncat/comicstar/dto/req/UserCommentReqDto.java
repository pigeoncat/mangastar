package io.github.pigeoncat.comicstar.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户发表评论 请求DTO
 */
@Data
public class UserCommentReqDto {

    private Long userId;

    @Schema(description = "漫画ID", required = true)
    @NotNull(message="漫画ID不能为空！")
    private Long comicId;

    @Schema(description = "评论内容", required = true)
    @NotBlank(message="评论不能为空！")
    @Length(min = 10,max = 512)
    private String commentContent;

}
