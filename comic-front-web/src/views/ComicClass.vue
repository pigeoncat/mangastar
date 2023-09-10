<!-- 全部作品 -->
<template>
  <!-- 顶部搜索栏和导航栏 -->
  <div class="header">
    <Top @eventSerch="searchByK" />
    <Navbar />
  </div>

  <div id="main-box">
    <!-- 中间主体部分 -->
    <div class="main box_center cf" >
      <!-- 顶部搜索标签选择模块 -->
      <div class="channelWrap classTable cf">
        <div class="so_tag">
          <ul class="list">
            <!--          作品频道-->
            <!--          <li class="so_pd" id="workDirection">-->
            <!--            <span class="tit">作品频道：</span>-->
            <!--            <a-->
            <!--                filter-value="0"-->
            <!--                href="javascript:void(0)"-->
            <!--                @click="loadCategoryList(0)"-->
            <!--                :class="`${workDirectionOn == 0 ? 'on' : ''}`"-->
            <!--            >男频</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="1"-->
            <!--                href="javascript:void(0)"-->
            <!--                @click="loadCategoryList(1)"-->
            <!--                :class="`${workDirectionOn == 1 ? 'on' : ''}`"-->
            <!--            >女频</a-->
            <!--            >-->
            <!--          </li>-->
            <!--          作品分类-->
            <li id="idGirl" class="so_class">
              <span class="tit">作品分类：</span>

              <span class="so_boy" id="boyCategoryList">
              <a
                  href="javascript:void(0)"
                  :class="`${categoryOn == 0 ? 'on' : ''}`"
                  @click="changeCategory(0)"
              >不限</a
              >
              <a
                  v-for="(item, index) in comicCategorys"
                  :key="index"
                  href="javascript:void(0)"
                  :class="`${categoryOn == item.id ? 'on' : ''}`"
                  @click="changeCategory(item.id)"
              >{{ item.name }}</a
              >
            </span>
            </li>
            <!--          是否完结-->
            <!--          <li class="so_progress">-->
            <!--            <span class="tit">是否完结：</span>-->
            <!--            <a-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${comicStatusOn == null ? 'on' : ''}`"-->
            <!--                @click="changeComicStatus(null)"-->
            <!--            >不限</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="0"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${comicStatusOn == 0 ? 'on' : ''}`"-->
            <!--                @click="changeComicStatus(0)"-->
            <!--            >连载中</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="1"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${comicStatusOn == 1 ? 'on' : ''}`"-->
            <!--                @click="changeComicStatus(1)"-->
            <!--            >已完结</a-->
            <!--            >-->
            <!--          </li>-->
            <!--          更新时间-->
            <!--          <li class="so_update">-->
            <!--            <span class="tit">更新时间：</span>-->
            <!--            <a-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${updateTimeOn == null ? 'on' : ''}`"-->
            <!--                @click="changeUpdateTime(null)"-->
            <!--            >不限</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="3"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${updateTimeOn == 3 ? 'on' : ''}`"-->
            <!--                @click="changeUpdateTime(3)"-->
            <!--            >三日内</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="7"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${updateTimeOn == 7 ? 'on' : ''}`"-->
            <!--                @click="changeUpdateTime(7)"-->
            <!--            >七日内</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="15"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${updateTimeOn == 15 ? 'on' : ''}`"-->
            <!--                @click="changeUpdateTime(15)"-->
            <!--            >半月内</a-->
            <!--            >-->
            <!--            <a-->
            <!--                filter-value="30"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${updateTimeOn == 30 ? 'on' : ''}`"-->
            <!--                @click="changeUpdateTime(30)"-->
            <!--            >一月内</a-->
            <!--            >-->
            <!--          </li>-->
            <!--          排序方式-->
            <!--          <li class="so_sort">-->
            <!--            <span class="tit">排序方式：</span>-->
            <!--            <a href="javascript:void(0)"-->
            <!--               :class="`${sortOn == null ? 'on' : ''}`"-->
            <!--               @click="changeSort(null)">不限</a>-->
            <!--            <a-->
            <!--                filter-value="last_index_update_time"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${sortOn == 'last_chapter_update_time desc' ? 'on' : ''}`"-->
            <!--                @click="changeSort('last_chapter_update_time desc')"-->
            <!--            >更新时间</a>-->
            <!--            <a-->
            <!--                filter-value="visit_count"-->
            <!--                href="javascript:void(0)"-->
            <!--                :class="`${sortOn == 'visit_count desc' ? 'on' : ''}`"-->
            <!--                @click="changeSort('visit_count desc')"-->
            <!--            >点击量</a-->
            <!--            >-->
            <!--          </li>-->

          </ul>
        </div>
      </div>

      <!-- 具体条目展示部分 -->
      <div class="channelWrap channelClassContent cf">
        <div class="updateTable rankTable">
          <!-- 条目展示部分 -->
          <div class="itemsList" v-for="(item, index) in comics" :key="index" >
            <!-- 封面 -->
            <a class="items_img comic-box" href="javascript:void(0)" @click="comicDetail(item.comicId)">
              <img :src="`${item.picUrl}`" onerror="this.src='default.gif';this.onerror=null"
                   :alt="item.comicName" />
            </a>
            <!-- 描述信息 -->
            <div class="items_txt">
              <h4>
                <a href="javascript:void(0)"
                   @click="comicDetail(item.comicId)"
                   v-html="item.comicName"
                ></a>
              </h4>
              <p class="author">
                <a href="javascript:void(0)">
                  作者：
                  <span v-html="item.authorName"></span>
                </a>
              </p>
              <p class="intro">
                <a href="javascript:void(0)" @click="comicDetail(item.comicId)" v-html="item.comicDesc"></a>
              </p>
            </div>
          </div>

          <!-- 底部的分页插件 -->
          <el-pagination
              small
              layout="prev, pager, next"
              :background="backgroud"
              :page-size="pageSize"
              :total="total"
              class="mt-4"
              @current-change="handleCurrentChange"
          />
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
import { searchComics, listCategorys } from "@/api/comic";
import { addDay, dateFormat } from "@/utils";
import Top from "@/components/common/Top";
import Navbar from "@/components/common/Navbar";
import Footer from "@/components/common/Footer";

export default {
  name: "comicClass",
  components: {
    Top,
    Navbar,
    Footer,
  },
  setup() {
    const route = useRoute();
    const router = useRouter();

    const state = reactive({
      comicCategorys: [],
      comics: [],
      searchCondition: {},
      backgroud: true,
      total: 0,
      pageSize: 10,
      imgBaseUrl: process.env.VUE_APP_BASE_IMG_URL,
      workDirectionOn: 0,
      categoryOn: 0,
      comicStatusOn: null,
      wordCountOn: null,
      updateTimeOn: null,
      sortOn:null
    });
    onMounted(() => {
      const key = route.query.key;
      state.searchCondition.keyword = key;
      state.searchCondition.pageSize = 10;
      loadCategoryList(0);
    });

    const search = async () => {
      const { data } = await searchComics(state.searchCondition);
      state.comics = data.list;
      console.log(state.comics)
      state.searchCondition.pageNum = data.pageNum;
      state.searchCondition.pageSize = data.pageSize;
      state.total = Number(data.total);
    };

    const searchByK = (key) => {
      state.searchCondition = { keyword: key };
      search();
    };
    const comicDetail = (comicId) => {
      router.push({ path: `/comic/${comicId}` });
    };

    const handleCurrentChange = (pageNum) => {
      state.searchCondition.pageNum = pageNum;
      search();
    };

    const loadCategoryList = async (workDirection) => {
      const { data } = await listCategorys({ workDirection: workDirection });
      state.comicCategorys = data;
      state.workDirectionOn = workDirection;
      state.searchCondition.workDirection = workDirection;
      state.categoryOn = 0;
      state.searchCondition.categoryId = null;
      search();
    };

    const changeCategory = async (categoryId) => {
      state.categoryOn = categoryId;
      if (categoryId > 0) {
        state.searchCondition.categoryId = categoryId;
      } else {
        state.searchCondition.categoryId = null;
      }
      search();
    };

    const changeComicStatus = async (comicStatus) => {
      state.comicStatusOn = comicStatus;
      state.searchCondition.comicStatus = comicStatus;
      search();
    };

    const changeWordCount = async (wordCountMin, wordCountMax) => {
      state.wordCountOn = wordCountMin;
      state.searchCondition.wordCountMin = wordCountMin;
      state.searchCondition.wordCountMax = wordCountMax;

      search();
    };

    const changeUpdateTime = async (days) => {
      state.updateTimeOn = days;
      if (days) {
        state.searchCondition.updateTimeMin = dateFormat(
            "YYYY-mm-dd",
            addDay(-days)
        );
      } else {
        state.searchCondition.updateTimeMin = null;
      }
      search();
    };

    const changeSort = async (sort) => {
      state.sortOn = sort;
      state.searchCondition.sort = sort;
      search();
    };

    return {
      ...toRefs(state),
      comicDetail,
      searchByK,
      search,
      handleCurrentChange,
      loadCategoryList,
      changeCategory,
      changeComicStatus,
      changeWordCount,
      changeUpdateTime,
      changeSort
    };
  },
  computed: {
    wordCountFormat(wordCount) {
      return (wordCount) => {
        if (wordCount.length >= 5) {
          return parseInt(wordCount / 10000) + "万";
        }
        if (wordCount.length >= 4) {
          return parseInt(wordCount / 1000) + "千";
        }
        return wordCount;
      };
    },
  },
};
</script>

<style>

#main-box{
  background-image: url("../assets/images/bg03.jpg");
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