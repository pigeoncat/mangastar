package io.github.pigeoncat.comicstar.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 漫画信息
 * </p>
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comic_info")
public class ComicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作品方向;0-男频 1-女频
     */
    private Integer workDirection;

    /**
     * 类别ID
     */
    private Long categoryId;

    /**
     * 类别名
     */
    private String categoryName;

    /**
     * 漫画封面地址
     */
    private String picUrl;

    /**
     * 漫画名
     */
    private String comicName;

    /**
     * 作家id
     */
    private Long authorId;

    /**
     * 作家名
     */
    private String authorName;

    /**
     * 漫画描述
     */
    private String comicDesc;

    /**
     * 漫画标签
     */
    private String tags;

    /**
     * 评分;总分:10 ，真实评分 = score/10
     */
    private Integer score;

    /**
     * 漫画状态;0-连载中 1-已完结
     */
    private Integer comicStatus;

    /**
     * 点击量
     */
    private Long visitCount;

    /**
     * 总章节数
     */
    private Integer chapterCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 最新章节号
     */
    private Integer lastChapterNum;


    /**
     * 最新章节名
     */
    private String lastChapterName;

    /**
     * 最新章节更新时间
     */
    private LocalDateTime lastChapterUpdateTime;

    /**
     * 是否收费;1-收费 0-免费
     */
    private Integer isVip;

    /**
     * 漫画来源,爬虫从哪个站点爬取的
     */
    private String origin;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    //覆盖lombok的getter
    public List<String> getTags(){
        if (ObjectUtils.isEmpty(this.tags)){
            return null;
        }
        ArrayList<String> tags = new ArrayList<String>();
        String[] split = this.tags.split(",");
        for (String tag : split) {
            tags.add(tag);
        }
        return tags;
    }

}
