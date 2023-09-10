<!-- 排行榜 -->
<template>
  <!-- 顶部搜索栏和导航栏 -->
  <Header />

  <!-- 中间主体部分 -->
  <div class="main box_center cf mb50">
    <div class="channelRankingContent cf">
      <!-- 左侧排行榜具体展示部分 -->
      <div class="wrap_left fl">
        <div class="wrap_bg">
          <!--榜单详情 start-->
          <div class="pad20">
            <div class="comic_tit">
              <div class="fl">
                <h3 class="font26 mt5 mb5" id="rankName">{{ rankName }}</h3>
              </div>
              <a class="fr"></a>
            </div>
          </div>
          <!--榜单详情 end-->
<!--          展示榜单 start-->
          <div class="channelWrap channelPic cf">
            <div class="leftBox">
              <!-- 展示内容.... -->
              <div class="picRecommend cf" id="hotRecComics">
                <!-- 封装展示条目 -->
                <div class="itemsList" v-for="(item, index) in comics" :key="index" >
                  <!-- 封面 -->
                  <a class="items_img comic-box" href="javascript:void(0)" @click="comicDetail(item.comicId)">
                    <img :src="`${item.picUrl}`" onerror="this.src='default.gif';this.onerror=null"
                         :alt="item.comicName" />
                  </a>
                  <!-- 描述信息 -->
                  <div class="items_txt">
                    <h4>
                      <a href="javascript:void(0)" @click="comicDetail(item.comicId)">{{ item.comicName }}</a>
                    </h4>
                    <p class="author">
                      <a href="javascript:void(0)">作者：{{ item.authorName }}</a>
                    </p>
                    <p class="intro">
                      <a href="javascript:void(0)" @click="comicDetail(item.comicId)" v-html="item.comicDesc"></a>
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
<!--          展示榜单 end-->
        </div>
      </div>

      <!-- 右侧选择排行榜部分 -->
      <div class="wrap_right fr">
        <div class="wrap_inner wrap_right_cont mb20">
          <div class="title cf noborder">
            <h3 class="on">排行榜</h3>
          </div>
          <div class="rightList2">
            <ul id="rankType">
              <li>
                <a :class="`${rankType == 1 ? 'on' : ''}`" href="javascript:void(0)" @click="visitRank"
                >点击榜</a
                >
              </li>
              <li>
                <a :class="`${rankType == 2 ? 'on' : ''}`" href="javascript:void(0)" @click="newestRank">新书榜</a>
              </li>
              <li>
                <a :class="`${rankType == 3 ? 'on' : ''}`" href="javascript:void(0)" @click="updateRank">更新榜</a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 底部友情链接部分 -->
  <Footer />
</template>

<script>
import "@/assets/styles/comic.css";
import { reactive, toRefs, onMounted, ref } from "vue";
import { useRouter, useRoute } from "vue-router";
import {listVisitRankComics,listUpdateRankComics,listNewestRankComics} from "@/api/comic";
import Header from "@/components/common/Header";
import Footer from "@/components/common/Footer";
export default {
  name: "comicRank",
  components: {
    Header,
    Footer,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const state = reactive({
      comics: [],
      rankName: "点击榜",
      rankType: 1,
    });
    onMounted(() => {
      visitRank();
    });

    const visitRank = async () => {
      const { data } = await listVisitRankComics();
      state.comics = data;
      state.rankName = "点击榜";
      state.rankType = 1;
    };

    const newestRank = async () => {
      const { data } = await listNewestRankComics();
      state.comics = data;
      state.rankName = "新书榜";
      state.rankType = 2;
    };

    const updateRank = async () => {
      const { data } = await listUpdateRankComics();
      state.comics = data;
      state.rankName = "更新榜";
      state.rankType = 3;
    };

    const comicDetail = (comicId) => {
      router.push({ path: `/comic/${comicId}` });
    };

    return {
      ...toRefs(state),
      comicDetail,
      newestRank,
      visitRank,
      updateRank,
    };
  },
  computed: {
    wordCountFormat(wordCount) {
      return (wordCount) => {
        if (wordCount.length > 5) {
          return parseInt(wordCount / 10000) + "万";
        }
        if (wordCount.length > 4) {
          return parseInt(wordCount / 1000) + "千";
        }
        return wordCount;
      };
    },
  },
};
</script>

<style>

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



.el-pagination {
  justify-content: center;
}
.el-pagination.is-background .el-pager li:not(.is-disabled).is-active {
  background-color: #f80 !important;
}
.el-pagination {
  --el-pagination-hover-color: #f80 !important;
}
</style>
