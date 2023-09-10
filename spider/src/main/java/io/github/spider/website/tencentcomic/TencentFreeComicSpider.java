package io.github.spider.website.tencentcomic;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.spider.common.constant.ComicConstant;
import io.github.spider.common.constant.MqConstant;
import io.github.spidercommon.constant.ComicSaveModel;
import io.github.spidercommon.dto.ChapterDto;
import io.github.spidercommon.dto.EsDto;
import io.github.spidercommon.dto.MysqlDto;
import io.github.spidercommon.entity.ComicChapter;
import io.github.spidercommon.entity.ComicChapterPicture;
import io.github.spidercommon.entity.ComicInfo;
import io.github.spider.service.ComicService;
import io.github.spider.util.HttpClientUtils;
import io.github.spider.util.HttpUtils;
import io.github.spider.util.RandomUtils;
import io.github.spider.util.ThreadPoolUtils;
import io.github.spider.util.idgenerator.SnowflakeIdGenerator;
import io.github.spider.util.tencentutil.PagePool;
import io.github.spider.util.tencentutil.TencentDataParseUtils;
import io.github.spider.vo.tencentvo.*;
import io.github.spider.website.ISpider;
import io.github.spider.common.constant.TencentComicConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author pigeoncat
 * @Date 2023/08/28  09:42
 * 免费漫画页面的每个展示条目 ComicItemVo   https://ac.qq.com/Comic/all/search/hot/vip/1/page/1   漫画基本信息
 * 点击封面 --> 跳到漫画详情和章节列表页面   https://ac.qq.com/Comic/comicInfo/id/545320           漫画基本信息
 * 点击封面 --> 跳到漫画阅读页面            https://ac.qq.com/ComicView/index/id/545320/cid/4      漫画图片
 */
//免费漫画导航url,page后边的页数由1增长到最后一页,腾讯这里是1--176
@Component  //交给spring管理
@Slf4j
public class TencentFreeComicSpider implements ISpider {

    @Autowired
    private ComicService comicService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private  SnowflakeIdGenerator idWorker;


    private HttpClient httpClient = HttpClientUtils.getHttpClient();


    public static void main(String[] args) {
        new TencentFreeComicSpider().doSpider();
    }


    @Override
    public void doSpider() {
        //用线程池提交爬虫任务
        ExecutorService threadPool = ThreadPoolUtils.getThreadPool();
        //页码
        int pageStart = TencentComicConstant.FREE_COMIC_NAVIGATE_PAGE_START;
        int pageEnd = TencentComicConstant.FREE_COMIC_NAVIGATE_PAGE_END;
        //页面池
        PagePool pagePool = new PagePool(pageStart, pageEnd);
        //爬虫逻辑
        while (true){
            if (!pagePool.isEmpty()){
                //获得页面编号
                int pageNum = pagePool.getPageNum();
                //提交爬虫任务爬取指定编号的页面
                try {
                    List<ComicItemVo> comicItems=null;
                    //获取失败继续尝试,3次之后不再获取
                    try {
                        for (int i = 0; i < 3; i++) {
                            comicItems = getComicItemsFromPageUrl(TencentComicConstant.FREE_COMIC_NAVIGATE + pageNum);
                            if (comicItems!=null&&comicItems.size()>0){
                                break;
                            }
                        }
                    }catch (IOException e){
                        log.error(e.getMessage());
                    }
                    //提交爬虫任务
                    if (comicItems!=null){
                        for (int i = 0; i < comicItems.size(); i++) {
                            ComicItemVo comicItem = comicItems.get(i);
                            threadPool.submit(()->{
                                startCrawling(comicItem);
                            });
                        }
                    }
                    //任务提交成功后随机睡眠几秒再提交下一页爬虫任务
                    int delaySeconds = RandomUtils.getRandomNumberIn(2, 10);
                    TimeUnit.SECONDS.sleep(delaySeconds);
                }catch (Exception e){
                    //任务提交失败
                    System.out.println(e.getMessage());
                    //没有提交成功的任务放回页面池
                    pagePool.addToPool(pageNum);
                    System.out.println(Thread.currentThread().getName()+"线程先睡眠等待10秒再重新提交任务.....");
                    try {
                        TimeUnit.SECONDS.sleep(10);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }else {
                //表示已经爬完一轮，接下来等一段时间后再进行下一轮爬取更新漫画章节
                //时间设为 10 到 12 小时不等
                //释放资源
                int delayMinutes = RandomUtils.getRandomNumberIn(10 * 60, 12 * 60);
                System.err.println("该轮爬取结束后, "+delayMinutes+" 分钟后进行下一轮爬取...");
                try {
                    TimeUnit.MINUTES.sleep(delayMinutes);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //睡醒后重新填充页面池,进行下一轮爬取
                pagePool.refillPool();
            }
        }
    }

    //开始爬取
    private  void  startCrawling(ComicItemVo comicItem) {
        //获得漫画详情url的后边的id号，rest风格，id=654317
        //例如: https://ac.qq.com/Comic/comicInfo/id/654317
        String comicInfoUrl = comicItem.getComicInfoUrl();
        String[] split = comicInfoUrl.split("/");
        String comicId = split[split.length - 1];
        //System.out.println(comicInfoUrl);
        //得到漫画详情信息
        ComicInfoVo comicInfo = getComicInfoFromUrl(comicInfoUrl);
        //漫画阅读页信息,包含所有章节的url,id,章节名和章节序号
        //漫画图片地址在script-->var DATA = '......',
        String readPageUrl = TencentComicConstant.COMIC_CHAPTER_PAGE_PREFIX + comicId + "/cid/" + comicInfo.getLastNumInChapterList();
        List<Chapter> allChapters = getAllChapter(readPageUrl);
        //所有章节信息入库
        saveAllNeededInfo(comicItem, comicInfo, allChapters);
    }


    //得到映射 https://ac.qq.com/Comic/all/search/hot/vip/1/page/1 漫画条目信息的vo类
    private List<ComicItemVo> getComicItemsFromPageUrl(String pageUrl) throws IOException {
        ArrayList<ComicItemVo> comicItemVos = new ArrayList<ComicItemVo>();
        //爬取和解析页面
        Document doc  = getHtmlDocument(pageUrl);
        if (ObjectUtil.isEmpty(doc)) {
            return comicItemVos;
        }
        //获得li标签集合(漫画采用ul>li标签展示)
        Elements lis = doc.selectXpath("//div[@class=\"ret-search-result\"]/ul/li");
        lis.forEach(li -> {
            //解析li标签封装到免费漫画页漫画展示条目对应的数据描述 vo 对象
            ComicItemVo comicItem = getVoFromCurrentItem(li);
            //数据完整的条目，才加入集合
            if (comicItem.isDataComplete()) {
                comicItemVos.add(comicItem);
            }
        });
        return comicItemVos;
    }

    private ComicItemVo getVoFromCurrentItem(Element li) {
        //个别xpath没解析出来，换成css选择器
        //封面url
        String coverUrl = li.select("div.ret-works-cover > a > img").attr("data-original");
        //漫画详情url后缀
        String comicDetailSuffix = li.select("div.ret-works-cover > a").attr("href");
        //漫画名称
        String comicName = li.select("div.ret-works-cover > a").attr("title");
        //更新至几话或全多少话
        String chapterCount = li.selectXpath("./div[@class=\"ret-works-cover\"]//span[@class=\"mod-cover-list-text\"]").text();
        //签约
        String comicSign = li.selectXpath("./div[@class=\"ret-works-info\"]/h3/i[@class=\"ui-icon-sign\"]").text();
        //独家
        String comicExclusive = li.selectXpath("./div[@class=\"ret-works-info\"]/h3/i[@class=\"ui-icon-exclusive\"]").text();
        //漫画作者--笔名
        String author = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-author\"]").text();
        //作品标签，热血、人气、妖怪....
        Elements comicTagsEl = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-tags\"]/span[@target=\"_blank\"]");
        ArrayList<String> comicTags = new ArrayList<>();
        comicTagsEl.forEach(tag -> comicTags.add(tag.text()));
        //人气---多少多少亿或者万
        String popularity = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-tags\"]//em").text();
        //漫画描述
        String comicDesc = li.selectXpath("./div[@class=\"ret-works-info\"]/p[@class=\"ret-works-decs\"]").text();

        return ComicItemVo.builder()
                .coverUrl(coverUrl)
                .comicInfoUrl(TencentComicConstant.COMIC_DETAIL_PAGE_PREFIX + comicDetailSuffix)
                .comicName(comicName)
                .chapterCount(chapterCount)
                .comicSign(comicSign)
                .comicExclusive(comicExclusive)
                .author(author)
                .comicTags(comicTags)
                .popularity(popularity)
                .comicDesc(comicDesc)
                .build();
    }


    //得到映射 https://ac.qq.com/Comic/comicInfo/id/545320  信息的vo类
    private ComicInfoVo getComicInfoFromUrl(String comicInfoUrl) {
        Document doc = getHtmlDocument(comicInfoUrl);
        //评分
        String scoreStr = doc.selectXpath("//div[@class=\"works-score clearfix\"]/p/strong").text();
        Double score=Double.valueOf(scoreStr);
        //是否已经完结
        String label = doc.selectXpath("//div[@class=\"works-cover ui-left\"]/label").text();
        boolean isFinish=label.contains("完")?true:false;
        //漫画简介
        String desc = doc.selectXpath("//div[@class=\"works-intro-detail ui-left\"]//p[@class=\"works-intro-short ui-text-gray9\"]").text();
        //起始章节
        String chapterStartUrl = doc.select("div.works-chapter-list-wr.ui-left > ol.works-chapter-list > li > p > span.works-chapter-item > a").attr("href");
        //起始章节号
        String chapterStartNum = getCId(chapterStartUrl);
        //漫画详情页章节展示目录展示的最后一条
        String lastInChapterListUrl = doc.select("div.works-chapter-list-wr.ui-left > ol.works-chapter-list > li > p:last-child > span:last-child > a").attr("href");
        String lastNumInChapterList = getCId(lastInChapterListUrl);
        ComicInfoVo comicInfo = new ComicInfoVo(score,isFinish,desc, chapterStartNum, lastNumInChapterList);
        return comicInfo;
    }


    // https://ac.qq.com/ComicView/index/id/545320/cid/4
    //得到当前章节图片地址
    private List<Picture> getChapterPicture(Chapter chapter) {
        String chapterUrl = chapter.getUrl();
        String doc = getHtmlString(chapterUrl);
        String varData = TencentDataParseUtils.getVarData(doc);
        String data = TencentDataParseUtils.base64Decode(varData);
        List<Picture> pictures = TencentDataParseUtils.getChapterPictures(data);
        return pictures;
    }

    //得到所有章节信息
    private List<Chapter> getAllChapter(String readPageUrl) {
        Document doc = getHtmlDocument(readPageUrl);
        Elements lis = doc.selectXpath("//ul[@id=\"catalogueList\"]/li");
        List<Chapter> chapters = new ArrayList<Chapter>();
        lis.forEach(li -> {
            String suffix = li.selectFirst("a").attr("href");
            String cUrl = TencentComicConstant.COMIC_DETAIL_PAGE_PREFIX + suffix;
            String cSeq = li.selectFirst("span.tool_chapters_list_number").text().replace("[", "").replace("]", "");
            String cTitle = li.selectFirst("span.tool_chapters_list_title").text();

            Chapter chapter = new Chapter(cUrl, getCId(cUrl), cSeq, cTitle);
            chapters.add(chapter);
        });

        return chapters;
    }


    private Document getHtmlDocument(String url) {
        String html = getHtmlString(url);
        if (html != null) {
            return Jsoup.parse(html);
        }
        return null;
    }

    private String getHtmlString(String url) {
        HttpGet httpGet = HttpUtils.createHttpGet();
        httpGet.setURI(URI.create(url));
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == 200 && ObjectUtil.isNotEmpty(entity)) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCId(String url) {
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    //保存爬虫数据到数据库
    private void saveAllNeededInfo(ComicItemVo comicItem, ComicInfoVo comicInfo, List<Chapter> allChapters) {
        //检查爬取的漫画数据是否完整
        if (!comicItem.isDataComplete()){
            return;
        }
        //查询漫画
        ComicInfo oldComicInfo = comicService.getComicInfoByNameAndOrigin(comicItem.getComicName(),"tencent");
        //若漫画不存在则入库
        if (ObjectUtils.isEmpty(oldComicInfo)){
            MysqlDto mysqlDto = new MysqlDto();
            ArrayList<ChapterDto> chapterDtoList = new ArrayList<>();
            //封装漫画信息
            ComicInfo newComicInfo = getComicInfoFrom(ComicSaveModel.INSERT, comicItem, comicInfo, allChapters);
            //入库
            //遍历每个章节并入库
            for (int i = 0; i < allChapters.size(); i++) {
                Chapter chapter = allChapters.get(i);
                List<Picture> pictures = getChapterPicture(chapter);
                //漫画章节信息
                ComicChapter comicChapter = getComicChapterFrom(newComicInfo, chapter, pictures);
                //章节图片集合
                List<ComicChapterPicture> pictureList = getComicChapterPictureList(comicChapter, pictures);
                chapterDtoList.add(new ChapterDto(comicChapter, pictureList));
            }
            mysqlDto.setSaveModel(ComicSaveModel.INSERT);
            mysqlDto.setComicInfo(newComicInfo);
            mysqlDto.setChapterDtoList(chapterDtoList);
            EsDto esDto = getEsDtoFromComicInfo(newComicInfo);
            esDto.setSaveModel(ComicSaveModel.INSERT);
            try {
                System.out.println("发送消息...");
                System.out.println(mysqlDto.getComicInfo().getComicName()+" : "+Thread.currentThread().getName());
                //发布入库消息
                rabbitTemplate.convertAndSend(MqConstant.COMIC_EXCHANGE,MqConstant.MYSQL_ROUTING_KEY,mysqlDto,new CorrelationData());
                rabbitTemplate.convertAndSend(MqConstant.COMIC_EXCHANGE,MqConstant.ES_ROUTING_KEY,esDto,new CorrelationData());
            } catch (AmqpException e) {
                e.printStackTrace();
            }
        }

        //存在，只更新对应章节，不用遍历所有章节
        int oldChapterCount = oldComicInfo.getChapterCount();
        int newChapterCount = allChapters.size();
        int addCount=newChapterCount-oldChapterCount;
        //需要更新章节
        if (addCount>0){
            MysqlDto mysqlDto = new MysqlDto();
            ArrayList<ChapterDto> chapterDtoList = new ArrayList<>();
            //只遍历新的章节入库
            for (int i = oldChapterCount; i < newChapterCount; i++) {
                Chapter chapter = allChapters.get(i);
                List<Picture> pictures = getChapterPicture(chapter);
                //漫画章节信息
                ComicChapter comicChapter = getComicChapterFrom(oldComicInfo, chapter, pictures);
                //章节图片集合
                List<ComicChapterPicture> pictureList = getComicChapterPictureList(comicChapter, pictures);
                chapterDtoList.add(new ChapterDto(comicChapter,pictureList));
            }
            ComicInfo comicInfoUpdate = new ComicInfo();
            comicInfoUpdate.setId(oldComicInfo.getId());
            comicInfoUpdate.setChapterCount(newChapterCount);
            comicInfoUpdate.setLastChapterNum(getLastChapterNum(allChapters));
            comicInfoUpdate.setLastChapterName(getLastChapterName(allChapters));
            comicInfoUpdate.setLastChapterUpdateTime(LocalDateTime.now());
            comicInfoUpdate.setUpdateTime(LocalDateTime.now());
            mysqlDto.setSaveModel(ComicSaveModel.UPDATE);
            mysqlDto.setComicInfo(comicInfoUpdate);
            mysqlDto.setChapterDtoList(chapterDtoList);
            EsDto esDto = getEsDtoFromComicInfo(comicInfoUpdate);
            esDto.setSaveModel(ComicSaveModel.UPDATE);
            try {
                //发布更新消息
                rabbitTemplate.convertAndSend(MqConstant.COMIC_EXCHANGE,MqConstant.MYSQL_ROUTING_KEY,mysqlDto,new CorrelationData());
                rabbitTemplate.convertAndSend(MqConstant.COMIC_EXCHANGE,MqConstant.ES_ROUTING_KEY,esDto,new CorrelationData());
            } catch (AmqpException e) {
                e.printStackTrace();
            }
        }
    }

    private EsDto getEsDtoFromComicInfo(ComicInfo comicInfo){
        return EsDto.builder()
                .comicId(comicInfo.getId())
                .categoryId(comicInfo.getCategoryId())
                .categoryName(comicInfo.getCategoryName())
                .comicName(comicInfo.getComicName())
                .authorName(comicInfo.getAuthorName())
                .picUrl(comicInfo.getPicUrl())
                .tags(comicInfo.getTags())
                .comicDesc(comicInfo.getComicDesc())
                .chapterCount(comicInfo.getChapterCount())
                .lastChapterNum(comicInfo.getLastChapterNum())
                .lastChapterName(comicInfo.getLastChapterName())
                .origin(comicInfo.getOrigin())
                .lastChapterUpdateTime(comicInfo.getLastChapterUpdateTime())
                .build();
    }

    private List<ComicChapterPicture> getComicChapterPictureList(ComicChapter comicChapter, List<Picture> pictures){
        ArrayList<ComicChapterPicture> pictureList = new ArrayList<>(pictures.size());
        for (int i = 0; i < pictures.size(); i++) {
            ComicChapterPicture picture = new ComicChapterPicture();
            picture.setId(idWorker.nextId());
            picture.setComicId(comicChapter.getComicId());
            picture.setChapterNum(comicChapter.getChapterNum());
            picture.setSort(i);
            picture.setUrl(pictures.get(i).getUrl());
            picture.setCreateTime(LocalDateTime.now());
            picture.setUpdateTime(LocalDateTime.now());
            pictureList.add(picture);
        }
        return pictureList;
    }

    private ComicChapter getComicChapterFrom(ComicInfo comicInfo,Chapter chapter,List<Picture> pictures){
        ComicChapter comicChapter = new ComicChapter();
        comicChapter.setId(idWorker.nextId());
        comicChapter.setComicId(comicInfo.getId());
        comicChapter.setChapterNum(chapter.getCSeqAsInt());
        comicChapter.setChapterName(chapter.getCTitle());
        comicChapter.setPageCount(pictures.size());
        comicChapter.setIsVip(0);
        comicChapter.setCreateTime(LocalDateTime.now());
        comicChapter.setUpdateTime(LocalDateTime.now());
        return comicChapter;
    }

    private ComicInfo getComicInfoFrom(ComicSaveModel saveModel, ComicItemVo comicItem, ComicInfoVo comicInfo, List<Chapter> allChapters){
        ComicInfo newComicInfo = new ComicInfo();
        newComicInfo.setId(idWorker.nextId());
        newComicInfo.setWorkDirection(getWorkDirection(comicItem));
        newComicInfo.setCategoryId(getCategoryId(comicItem));
        newComicInfo.setCategoryName(getCategoryName(comicItem));
        newComicInfo.setPicUrl(comicItem.getCoverUrl());
        newComicInfo.setComicName(comicItem.getComicName());
        newComicInfo.setAuthorName(comicItem.getAuthor());
        newComicInfo.setComicDesc(comicInfo.getDesc());
        newComicInfo.setTags(comicItem.getTagsAsString());
        newComicInfo.setScore(comicInfo.getScoreAsInt());
        newComicInfo.setComicStatus(getComicStatus(comicInfo));
        newComicInfo.setVisitCount(comicItem.getPopularityAsLong());
        newComicInfo.setChapterCount(allChapters.size());
        newComicInfo.setLastChapterNum(getLastChapterNum(allChapters));
        newComicInfo.setLastChapterName(getLastChapterName(allChapters));
        newComicInfo.setLastChapterUpdateTime(LocalDateTime.now());
        newComicInfo.setIsVip(0);
        newComicInfo.setOrigin("tencent");
        newComicInfo.setUpdateTime(LocalDateTime.now());
        if (saveModel==ComicSaveModel.INSERT){
            newComicInfo.setCreateTime(LocalDateTime.now());
        }
        return newComicInfo;
    }

    private Integer getWorkDirection(ComicItemVo comicItem){
        for (String tag : comicItem.getComicTags()) {
            //女频
            if (ComicConstant.ComicInfoConstant.FEMALE_FREQUENCY.contains(tag)){
                return ComicConstant.ComicInfoConstant.FEMALE_WORK_DIRECTION;
            }
        }
        //男频
        return ComicConstant.ComicInfoConstant.MALE_WORK_DIRECTION;
    }

    private Long getCategoryId(ComicItemVo comicItem){
        String tags = comicItem.getTagsAsString();
        if (tags.contains("玄")||tags.contains("奇")||tags.contains("幻")){
            return 1L;
        }else if (tags.contains("武")||tags.contains("侠")||tags.contains("仙")){
            return 2L;
        }else if(tags.contains("言")||tags.contains("情")||tags.contains("都市")){
            return 3L;
        }else if(tags.contains("史")||tags.contains("军")){
            return 4L;
        }else if (tags.contains("科")||tags.contains("灵")||tags.contains("异")){
            return 5L;
        }else if (tags.contains("恋")||tags.contains("爱")||tags.contains("浪漫")){
            return 6L;
        }else {
            return 7L;
        }
    }

    private String getCategoryName(ComicItemVo comicItem){
        String tags = comicItem.getTagsAsString();
        if (tags.contains("玄")||tags.contains("奇")||tags.contains("幻")){
            return "玄幻奇幻";
        }else if (tags.contains("武")||tags.contains("侠")||tags.contains("仙")){
            return "武侠仙侠";
        }else if(tags.contains("言")||tags.contains("情")||tags.contains("都市")){
            return "都市言情";
        }else if(tags.contains("史")||tags.contains("军")){
            return "历史军事";
        }else if (tags.contains("科")||tags.contains("灵")||tags.contains("异")){
            return "科幻灵异";
        }else if (tags.contains("恋")||tags.contains("爱")||tags.contains("浪漫")){
            return "女生频道";
        }else {
            return "其他";
        }
    }

    private Integer getComicStatus(ComicInfoVo comicInfo){
        return comicInfo.isFinish() ? 1 : 0 ;
    }

    private String getLastChapterName(List<Chapter> allChapters){
        if (ObjectUtils.isEmpty(allChapters)){
            return null;
        }
        return allChapters.get(allChapters.size()-1).getCTitle();
    }

    private Integer getLastChapterNum(List<Chapter> allChapters){
        if (ObjectUtils.isEmpty(allChapters)){
            return null;
        }
        return allChapters.get(allChapters.size() - 1).getCSeqAsInt();
    }


}
