package io.github.spider.common.constant;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  10:04
 * @TODO description
 */
public class TencentComicConstant {

    //腾讯漫画免费漫画导航url
    //例子:https://ac.qq.com/Comic/all/search/hot/vip/1/page/1
    public static final String FREE_COMIC_NAVIGATE = "https://ac.qq.com/Comic/all/search/hot/vip/1/page/";

    //腾讯漫画免费漫画导航url分页开始
    public static final Integer FREE_COMIC_NAVIGATE_PAGE_START = 1;

    //腾讯漫画免费漫画导航url分页结束
//    public static final Integer FREE_COMIC_NAVIGATE_PAGE_END = 176;
    public static final Integer FREE_COMIC_NAVIGATE_PAGE_END = 50;

    //腾讯漫画免费漫画页每条漫画详情页url地址前缀
    public static final String COMIC_DETAIL_PAGE_PREFIX = "https://ac.qq.com";

    //漫画章节阅读页url地址前缀
    public static final String COMIC_CHAPTER_PAGE_PREFIX = "https://ac.qq.com/ComicView/index/id/";

}
