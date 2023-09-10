<!--漫画详情页-->
<template>
  <!--  头部-->
  <Header/>
  <div id="main-all">
    <!--  中间详情展示部分-->
    <div class="main box_center cf mb50">
      <!--    漫画信息 盒子 -->
      <div class="channelWrap channelBookInfo cf">
        <!--      封面盒子-->
        <div class="comic_cover left-float comic-box" id="comicCoverBox">
          <a>
            <img
                id="comicCover"
                class="cover"
                :src="`${comic.picUrl}`"
                :alt="comic.comicName"
                @click="comicContent(firstChapter.comicId, firstChapter.chapterId)"
            /></a>
        </div>
        <!--      漫画详细信息介绍盒子-->
        <div class="comic_info left-float" id="comicInfoBox">
          <div class="left-float" id="comicBaseInfoBox">
            <h1>{{ comic.comicName }} &nbsp;&nbsp;&nbsp;&nbsp; {{ comic.comicStatus == 0 ? "连载中" : "已完结" }}</h1><br>
            <a class="author">作者: {{ comic.authorName }}</a><br><br>
            <span>点击量: &nbsp;
          {{
                comic.visitCount >= 100000000
                    ? (comic.visitCount / 100000000 + ' 亿')
                    : comic.visitCount >= 10000
                        ? (comic.visitCount / 10000 + ' 万')
                        : comic.visitCount
              }}
        </span><br>
          </div>
          <div class="intro_txt comic-box" id="comicDescBox">
            <a >简介: &nbsp;{{comic.comicDesc}}</a>
          </div>
          <div class="btns" id="optBtn">
            <a
                style="background-color: #4a90e2;border-color: #60BBFF;margin-top: 40px"
                href="javascript:void(0)"
                @click="comicContent(firstChapter.comicId, firstChapter.chapterId)"
                class="btn_ora"
            >点击阅读</a>
          </div>
        </div>

      </div>
      <!--    章节列表展示盒子-->
      <div class="channelWrap channelBookInfo cf" id="chapterListBox">
        <div id="chapterNavBar">
          <div class="left-float">
            <a><h1 style="font-size: large">章节目录:</h1></a>
          </div>
          <div class="left-float" style="margin-left: 400px;">
            <button @click="showPrevious" class="button button-previous">上一页</button>
            <button @click="showNext" class="button button-next">下一页</button>
          </div>
        </div>
        <div id="chapterListCenter">
          <ul class="list cf">
            <li v-for="(item,index) in displayedChapters" :key="index"
                class="chapterLi"
                @click="comicContent(item.comicId,item.chapterId)"
            >
              <span >{{item.chapterName}}</span>
            </li>
          </ul>
        </div>

      </div>

    </div>
  </div>

  <!--  尾部-->
  <Footer/>
</template>


<script>
import "@/assets/styles/comic.css";
import man from "@/assets/images/man.png";
import {reactive, toRefs, onMounted, onUpdated, onBeforeMount,computed,ref} from "vue";
import {ElMessage} from "element-plus";
import {useRouter, useRoute} from "vue-router";
import { useStore } from 'vuex';
import {
  getComicById,
  addVisitCount,
  getLastChapterAbout,
  listRecComics,
  listNewestComments, listChapters,getFirstChapter
} from "@/api/comic";
import {comment, deleteComment, updateComment} from "@/api/user";
import {getUid} from "@/utils/auth";
import Header from "@/components/common/Header";
import Footer from "@/components/common/Footer";
import author_head from "@/assets/images/author_head.png";
import no_comment from "@/assets/images/no_comment.png";
import {goToAnchor} from "@/utils";


export default {
  name: "comic",
  components: {
    Header,
    Footer,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const currentIndex = ref(0)
    const pageSize = ref(50)

    const state = reactive({
      uid: getUid(),
      comic: {},
      firstChapter: {},
      allChapter:[],
      comics: [],
      chapterAbout: {},
      commentContent: "",
      newestComments: {},
      imgBaseUrl: process.env.VUE_APP_BASE_IMG_URL,
      dialogUpdateCommentFormVisible: false,
      commentId: "",
      updateComment: "",
    });

    onMounted(() => {
      const comicId = route.params.id;
      loadComic(comicId);
      loadFirstChapter(comicId);
      loadAllChapter(comicId);
      // loadRecComics(comicId);
      // loadLastChapterAbout(comicId);
      // loadNewestComments(comicId);
    });

    const displayedChapters = computed(()=>{
      const start = currentIndex.value * pageSize.value;
      const end = start + pageSize.value;
      return state.allChapter.slice(start, end) ;
    });


    const showPrevious = ()=>{
      if (currentIndex.value > 0) {
        currentIndex.value--
      }
    };

    const showNext = ()=>{
      if (currentIndex.value < Math.ceil(state.allChapter.length / pageSize.value) - 1) {
        currentIndex.value++
      }
    };

    onUpdated(() => {
      console.log("onUpdated==========================");
      for (let i = 0; i < state.comics.length; i++) {
        document
            .getElementById("comicCover" + i)
            .setAttribute("onerror", "this.src='default.gif';this.onerror=null");
      }
    });

    const loadComic = async (comicId) => {
      const {data} = await getComicById(comicId);
      state.comic = data;
      document
          .getElementById("comicCover")
          .setAttribute("onerror", "this.src='default.gif';this.onerror=null");
      // addComicVisit(comicId);
    };

    const loadFirstChapter = async (comicId) => {
      const {data} = await getFirstChapter({comicId: comicId});
      state.firstChapter = data;
    };

    const loadAllChapter = async (comicId) => {
      const {data} = await listChapters({comicId: comicId});

      store.dispatch('fetchAllChapters', data); // 将数组保存到 store 中

      state.allChapter = data;
    };

    const loadRecComics = async (comicId) => {
      const {data} = await listRecComics({comicId: comicId});
      state.comics = data;
    };

    const loadLastChapterAbout = async (comicId) => {
      const {data} = await getLastChapterAbout({comicId: comicId});
      state.chapterAbout = data;
    };

    const comicContent = (comicId, chapterId) => {
      router.push({path: `/comic/${comicId}/${chapterId}`});
    };

    const comicDetail = (comicId) => {
      router.push({path: `/comic/${comicId}`});
      loadComic(comicId);
      loadRecComics(comicId);
      loadLastChapterAbout(comicId);
      loadNewestComments(comicId);
    };

    const chapterList = (comicId) => {
      router.push({path: `/chapter_list/${comicId}`});
    };

    const addComicVisit = async (comicId) => {
      addVisitCount({comicId: comicId});
    };

    const loadNewestComments = async (comicId) => {
      const {data} = await listNewestComments({comicId: comicId});
      state.newestComments = data;
    };

    const userComment = async () => {
      if (!state.commentContent) {
        ElMessage.error("用户评论不能为空！");
        return;
      }
      if (state.commentContent.length < 10) {
        ElMessage.error("评论不能少于 10 个字符！");
        return;
      }
      if (state.commentContent.length > 512) {
        ElMessage.error("评论不能多于 512 个字符！");
        return;
      }
      await comment({
        comicId: state.comic.id,
        commentContent: state.commentContent,
      });
      state.commentContent = "";
      loadNewestComments(state.comic.id);
    };

    const updateUserComment = async (id, comment) => {
      state.dialogUpdateCommentFormVisible = true;
      state.updateComment = comment;
      state.commentId = id;
    };

    const deleteUserComment = async (id) => {
      await deleteComment(id);
      loadNewestComments(state.comic.id);
    };

    const goUpdateComment = async (id) => {
      state.dialogUpdateCommentFormVisible = false;
      await updateComment(state.commentId, {content: state.updateComment});
      loadNewestComments(state.comic.id);
    };

    return {
      ...toRefs(state),
      author_head,
      no_comment,
      comicContent,
      comicDetail,
      chapterList,
      goToAnchor,
      userComment,
      deleteUserComment,
      man,
      updateUserComment,
      goUpdateComment,
      displayedChapters,
      showPrevious,
      showNext
    };
  },
  mounted() {
    $(".icon_show").click(function () {
      $(this).hide();
      $(".icon_hide").show();
      $(".intro_txt").innerHeight("auto");
    });
    $(".icon_hide").click(function () {
      $(this).hide();
      $(".icon_show").show();
      $(".intro_txt").innerHeight("");
    });

    $("#AuthorOtherNovel li").unbind("mouseover");

    $("#txtComment").on("input propertychange", function () {
      var count = $(this).val().length;
      $("#emCommentNum").html(count + "/1000");
      if (count > 1000) {
        $("#txtComment").val($("#txtComment").val().substring(0, 1000));
      }
    });
  },
};
</script>

<style>

#main-all{
  background-image: url("../assets/images/bg07.jpeg");
}

#chapterListBox{
  /*height: 500px;*/
}


#comicBaseInfoBox{
  width: 600px;
  height: 100px;
}
#comicDescBox{
  width: 600px;
  height: 300px;
}
#comicInfoBox{
  width: 600px;
  height: 400px;
}
#comicCoverBox {
  width: 300px;
  height: 400px;
  margin-right: 40px;
}


.comic-box {
  /* 设置初始样式 */
  border: 1px solid transparent;
  transition: all 0.3s ease;
  box-shadow: 0 0 0 rgba(0, 0, 0, 0); /* 初始状态没有阴影 */
}

.comic-box:hover {
  /* 鼠标经过时的样式 */
  border-color: deepskyblue; /* 设置边框的颜色或其他样式 */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.3); /* 添加阴影效果 */
  transform: translate(-2px, -2px); /* 平移元素位置 */
}

.cover {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 调整图像适应大小 */
}

.left-float {
  float: left;
}

.chapterLi{
  color: #0e6188;
  font-size: medium;
  /*border: 1px solid #000000;*/
  display: inline-block; /* 设置为内联块级元素，使标签从左到右排列 */
  margin-right: 30px; /* 设置标签之间的间隔为 20px */
  margin-top: 20px;
}
.chapterLi:hover{
  color: black;
  background-color: pink;
  border-radius: 10px;
  cursor: pointer;
}

.button {
  display: inline-block;
  padding: 6px 12px;
  font-size: 10px;
  font-weight: bold;
  text-align: center;
  color: #ffffff;
  background-color: #4a90e2;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.button:hover {
  background-color: #357bd8;
}

.button-previous {
  margin-right: 8px;
}

.button-next {
  margin-left: 8px;
}




.el-button:not(.is-text) {
  border: #f80;
  border-color: #f80;
}

.el-button--primary {
  --el-button-hover-bg-color: #f80;
}

.el-button--primary {
  --el-button-bg-color: #f70;
}

.el-button {
  --el-button-hover-text-color: #fafafa;
}

.el-button {
  --el-button-hover-bg-color: #ff880061;
}
</style>
