
<!--漫画阅读页面-->
<template>
<!--  底片-->
  <div class="background-box">
<!--    上边框盒子-->
    <div class="top-box">

    </div>
<!--    中间展示内容的盒子-->
    <div class="center-main">
      <!--    左边栏-->
      <div class="main-left-box">
        <div class="chapter-name-show" style="color: #d1d1d1">
          <p>第 {{data.chapterNum}} 章节 &nbsp;{{data.chapterName}}</p>
        </div>
        <button class="pre-chapter-button" @click="preChapter">上一章</button>
        <div class="go-back-box" @click="comicDetail(data.comicId)">
          返回
        </div>
      </div>

      <!--    中间展示图片的盒子-->
      <div class="main-center-box" ref="centerBoxRef">
        <el-scrollbar>
          <div v-for="(item,index) in data.chapterPictures" :key="index" >
            <img :src="`${item.url}`" alt="">
          </div>
        </el-scrollbar>
      </div>

      <!--    右边栏-->
      <div class="main-right-box">
        <button class="chapter-list-button"
                @mouseover="showDiv = true"
                @mouseleave="hideDiv">
          章节目录
        </button>
        <div class="chapter-list-box" :class="{ show: showDiv }"
             @mouseover="showDiv = true"
             @mouseleave="hideDiv">
          <el-scrollbar>
            <p v-for="(item,index) in allChapters"
               :key="index"
               class="scrollbar-demo-item chapter-list-item"
               @click="goToComicContent(item.comicId,item.chapterId)"
            >
              {{ item.chapterNum }} &nbsp; {{item.chapterName}}
            </p>
          </el-scrollbar>
        </div>


        <button class="next-chapter-button" @click="nextChapter">下一章</button>
      </div>
    </div>
<!--    底部栏盒子-->
    <div class="bottom-box">

    </div>
  </div>



</template>

<script>
import "@/assets/styles/comic.css";
import "@/assets/styles/read.css";
import { reactive, toRefs, onMounted, ref,nextTick,onBeforeUnmount, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { useStore } from 'vuex';
import { getComicContent, getPreChapterId, getNextChapterId,listChapters } from "@/api/comic";
import { ElMessage} from "element-plus";
import $ from 'jquery';


export default {
  name: "ComicContent",

  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const showDiv = ref(false);
    const centerBoxRef = ref(null);

    const state = reactive({
      data: {},
      allChapters:[],
    });

    onMounted(() => {
      state.allChapters = store.getters.getAllChapters;
      init(route.params.chapterId);
      // $(centerBoxRef.value).scrollTop(0);

    });

    const init = async (chapterId) => {
      const { data } = await getComicContent(chapterId);
      state.data = data;
    };


    const hideDiv = () => {
      showDiv.value = false;
    };


    const comicDetail = (comicId) => {
      router.push({ path: `/comic/${comicId}` });
    };

    const chapterList = (comicId) => {
      router.push({ path: `/chapter_list/${comicId}` });
    };

    const goToComicContent = async (comicId,chapterId) => {
      router.push({ path: `/comic/${comicId}/${chapterId}` });
      init(chapterId);
      $(centerBoxRef.value).find('.el-scrollbar__wrap').scrollTop(0);
    };

    const preChapter = async (comicId) => {
      const { data } = await getPreChapterId(route.params.chapterId);
      if (data) {
        router.push({ path: `/comic/${comicId}/${data}` });
        init(data);
        $(centerBoxRef.value).find('.el-scrollbar__wrap').scrollTop(0);
      } else {
        ElMessage.warning("已经是第一章了！");
      }
    };

    const nextChapter = async (comicId) => {
      const { data } = await getNextChapterId(route.params.chapterId);
      if (data) {
        router.push({ path: `/comic/${comicId}/${data}` });
        init(data);
        $(centerBoxRef.value).find('.el-scrollbar__wrap').scrollTop(0);
      } else {
        ElMessage.warning("已经是最后一章了！");
      }
    };


    return {
      ...toRefs(state),
      comicDetail,
      chapterList,
      preChapter,
      nextChapter,
      goToComicContent,
      showDiv,
      centerBoxRef,
      hideDiv
    };
  },

}
</script>

<style scoped>

.go-back-box{
  position: fixed;
  top: 100px;
  width: 50px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  color: #fff;
  background-color: #7f7f7f;
  opacity: 0.5;
}
.go-back-box:hover{
  opacity: 1;
  cursor: default;
}

/* 自定义滚动条样式 */
.el-scrollbar__wrap {
  background-color: pink; /* 更改滚动条背景颜色 */
  border: 1px solid pink; /* 更改滚动条边框颜色 */
}

.el-scrollbar__track {
  background-color: pink; /* 更改滚动条轨道背景颜色 */
}

.el-scrollbar__thumb {
  background-color: pink; /* 更改滚动条滑块背景颜色 */
  border: 1px solid pink; /* 更改滚动条滑块边框颜色 */
}

.scrollbar-demo-item {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 40px;
  margin: 5px;
  text-align: center;
  border-radius: 4px;
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.chapter-list-item{
  cursor: pointer;
}
.chapter-list-item:hover{
  color: black;
}

.chapter-list-box{
  margin-top: 0;
  margin-left: 20px;
  width: 260px;
  height: 590px;
  /*background-color: pink;*/
  display: none;
  position: fixed;
}

.show {
  display: block;
}

.pre-chapter-button{
  margin-left: 150px;
  margin-top: 627px;
  padding: 10px 20px;
  background-color: #7f7f7f;
  color: #fff;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  opacity: .5;
}
.pre-chapter-button:hover {
  background-color: #a6a6a6;
  color: #fff;
  opacity: .9;
}

.next-chapter-button{
  margin-left: 50px;
  margin-top: 600px;
  padding: 10px 20px;
  background-color: #7f7f7f;
  color: #fff;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  opacity: .5;
}
.next-chapter-button:hover {
  background-color: #a6a6a6;
  color: #fff;
  opacity: .9;
}


.chapter-list-button{
  margin-left: 150px;
  margin-top: 5px;
  padding: 10px 20px;
  background-color: #7f7f7f;
  color: #fff;
  font-size: 16px;
  border: none;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  transition: background-color 0.3s, color 0.3s, transform 0.3s;
}
.chapter-list-button:hover {
  background-color: #a6a6a6;
  color: #fff;
  transform: translateY(2px);
}

.main-center-box{
  height: 100%;
  width: 60%;
  background-color: #666666;
  float: left;
}
.main-left-box{
  height: 100%;
  width: 20%;
  /*background-color: #60BBFF;*/
  background-color: #0e0f0f;
  opacity: .9;
  float: left;
}
.main-right-box{
  height: 100%;
  width: 20%;
  /*background-color: #60BBFF;*/
  background-color: #0e0f0f;
  opacity: .9;
  float: left;
}

.center-main{
  position: absolute;
  width: 100%;
  /*height: 95%;*/
  height: 98%;
  background-color: #7f7f7f;
}
.top-box{
  width: 100%;
  /*height: 5%;*/
  height: 2%;
  /*background-color: pink;*/
}
.bottom-box{
  width: 100%;
  height: 0;
  /*background-color: pink;*/
}

.background-box{
  position: absolute;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: #404040;
  /*background-color: black;*/
}

</style>