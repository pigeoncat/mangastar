package io.github.pigeoncat.comicstar.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户评论响应 Dto
 */
@Data
@Builder
public class UserCommentRespDto {

    @Schema(description = "评论内容")
    private String commentContent;

    @Schema(description = "评论漫画封面")
    private String commentComicPic;

    @Schema(description = "评论漫画")
    private String commentComic;

    @Schema(description = "评论时间")
    private LocalDateTime commentTime;

}
