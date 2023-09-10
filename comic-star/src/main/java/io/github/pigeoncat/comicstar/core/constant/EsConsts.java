package io.github.pigeoncat.comicstar.core.constant;

/**
 * elasticsearch 相关常量
 *
 */
public class EsConsts {

    private EsConsts() {
        throw new IllegalStateException(SystemConfigConsts.CONST_INSTANCE_EXCEPTION_MSG);
    }

    /**
     * 漫画索引
     */
    public static class ComicIndex {

        private ComicIndex() {
            throw new IllegalStateException(SystemConfigConsts.CONST_INSTANCE_EXCEPTION_MSG);
        }

        /**
         * 索引名
         */
        public static final String INDEX_NAME = "manga";

        /**
         * 漫画id
         */
        public static final String COMIC_ID = "comicId";

        /**
         * 作品方向;0-男频 1-女频
         */
        public static final String FIELD_WORK_DIRECTION = "workDirection";

        /**
         * 类别ID
         */
        public static final String FIELD_CATEGORY_ID = "categoryId";

        /**
         * 类别名
         */
        public static final String FIELD_CATEGORY_NAME = "categoryName";

        /**
         * 漫画名
         */
        public static final String FIELD_COMIC_NAME = "comicName";

        /**
         * 作家id
         */
        public static final String FIELD_AUTHOR_ID = "authorId";

        /**
         * 作家名
         */
        public static final String FIELD_AUTHOR_NAME = "authorName";

        /**
         * 漫画描述
         */
        public static final String FIELD_COMIC_DESC = "comicDesc";

        /**
         * 评分;总分:10 ，真实评分 = score/10
         */
        public static final String FIELD_SCORE = "score";

        /**
         * 漫画状态;0-连载中 1-已完结
         */
        public static final String FIELD_COMIC_STATUS = "comicStatus";

        /**
         * 点击量
         */
        public static final String FIELD_VISIT_COUNT = "visitCount";

        /**
         * 总章节数
         */
        public static final String FIELD_CHAPTER_COUNT = "chapterCount";

        /**
         * 评论数
         */
        public static final String FIELD_COMMENT_COUNT = "commentCount";

        /**
         * 最新章节ID
         */
        public static final String FIELD_LAST_CHAPTER_ID = "lastChapterId";

        /**
         * 最新章节名
         */
        public static final String FIELD_LAST_CHAPTER_NAME = "lastChapterName";

        /**
         * 最新章节更新时间
         */
        public static final String FIELD_LAST_CHAPTER_UPDATE_TIME = "lastChapterUpdateTime";

        /**
         * 是否收费;1-收费 0-免费
         */
        public static final String FIELD_IS_VIP = "isVip";

        /**
         * 字段汇总
         */
        public static final String FIELD_ALL = "all";

    }

}
