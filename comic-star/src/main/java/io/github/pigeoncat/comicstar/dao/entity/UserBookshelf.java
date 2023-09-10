package io.github.pigeoncat.comicstar.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户书架
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_bookshelf")
public class UserBookshelf implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 漫画ID
     */
    private Long comicId;

    /**
     * 上一次阅读的章节ID
     */
    private Long preChapterId;

    /**
     * 创建时间;
     */
    private LocalDateTime createTime;

    /**
     * 更新时间;
     */
    private LocalDateTime updateTime;

}
