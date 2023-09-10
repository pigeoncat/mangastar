<!-- 首页 -->
<template>

    <!-- 头部搜索栏和导航栏 -->
  <Header />

  <div id="mainCenter">
    <!-- 中间主体显示部分 -->
    <div class="main box_center cf">
      <!-- 本周强推start... -->
      <div class="channelWrap channelBanner cf">

        <!-- 本周强推左侧部分 -->
        <div class="leftBox" >
          <div class="sliderContent">
            <!-- 大图轮播部分 -->
            <dl class="scBigImg" id="carouseBig">
              <dd v-for="(item, index) in sliderContent" :key="index" :class="{ on: index == 0 }">
                <a href="javascript:void(0)" @click="comicDetail(item.comicId)">
                  <img :src="`${item.picUrl}`" :alt="item.comicName"
                       onerror="this.src='default.gif';this.onerror=null" />
                </a>
              </dd>
            </dl>
            <!-- 小图轮播部分 -->
            <div class="scSmallImg" id="carouseSmall">
              <ul>
                <li v-for="(item, index) in sliderContent" :key="index" :class="{ on: index == 0 }">
                  <img :src="`${item.picUrl}`" :alt="item.comicName"
                       onerror="this.src='default.gif';this.onerror=null" />
                </li>
              </ul>
            </div>
            <!-- 小图轮播部分 -->
          </div>
        </div>

        <!-- 本周强推左侧部分结束 -->
        <!-- 本周强推右侧部分 -->
        <div class="rightBox">
          <div class="title cf" id="weekcommend">
            <h3>本周强推</h3>
          </div>
          <div class="rightList">
            <ul id="currentWeek">
              <li v-for="(item, index) in weekcommend" :key="index"
                  :class="['num' + (Number(`${index}`) + 1), { on: index == 0 }]">
                <br>
                <!-- 名称 -->
                <div class="comic_name">
                  <i>{{ index + 1 }}</i><a class="name" href="javascript:void(0)" @click="comicDetail(item.comicId)">{{
                    item.comicName
                  }}</a>
                </div>
                <!-- 封面 -->
                <div class="comic_intro">
                  <div class="cover">
                    <a href="javascript:void(0)" @click="comicDetail(item.comicId)">
                      <img
                          :src="`${item.picUrl}`" :alt="item.comicName"
                          onerror="this.src='default.gif';this.onerror=null" />
                    </a>
                  </div>
                  <div>
                    <a class="txt" href="javascript:void(0)" @click="comicDetail(item.comicId)">
                      {{ item.comicDesc.length > 30 ? item.comicDesc.slice(0, 30) + '...' : item.comicDesc }}
                    </a>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <!-- 本周强推右侧部分结束 -->
      </div>
      <!-- 本周强推end... -->

      <!-- 热门推荐start.... -->
      <div class="channelWrap channelPic cf">
        <div class="leftBox">
          <div class="title">
            <h2 class="on">热门推荐</h2>
          </div>
          <!-- 热门推荐展示内容.... -->
          <div class="picRecommend cf" id="hotRecComics">
            <!-- 封装展示条目 -->
            <div class="itemsList" v-for="(item, index) in hotRecommend" :key="index">
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
      <!-- 热门推荐end.... -->

      <!-- 精品推荐start.... -->
      <div class="channelWrap channelPic cf">
        <div class="leftBox">
          <div class="title">
            <h2>精品推荐</h2>
          </div>
          <!-- 精品推荐展示内容.... -->
          <div class="picRecommend cf" id="classicComics">
            <!-- 封装展示条目 -->
            <div class="itemsList" v-for="(item, index) in goodRecommend" :key="index">
              <!-- 封面 -->
              <a class="items_img comic-box" href="javascript:void(0)" @click="comicDetail(item.comicId)">
                <img :src="`${item.picUrl}`" onerror="this.src='default.gif';this.onerror=null" />
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
      <!-- 精品推荐end.... -->
    </div>
  </div>

  <!-- 底部友情链接部分 -->
  <FriendLink />
  <Footer />
</template>



<script>
import {reactive, toRefs, onMounted, ref} from "vue";
    import { useRouter, useRoute } from "vue-router";
    import { listHomeComics } from "@/api/home";
    import { ElMessage } from "element-plus";
    import Header from "@/components/common/Header";
    import Footer from "@/components/common/Footer";
    import FriendLink from "@/components/home/FriendLink";

    export default {
    name: "home",
    components: {
      Header,
      FriendLink,
      Footer,
    },
    setup() {

      const route = useRoute();
      const router = useRouter();

      const state = reactive({
        // 轮播图
        sliderContent: [],
        //本周强推
        weekcommend: [],
        // 热门推荐
        hotRecommend: [],
        // 精品推荐
        goodRecommend: [],
        imgBaseUrl: process.env.VUE_APP_BASE_IMG_URL,
      });

      onMounted(async () => {

        const { data } = await listHomeComics();

        await data.forEach((comic) => {
          if (comic.type == 0) {
            // 轮播图
            state.sliderContent[state.sliderContent.length] = comic;
          }
          if (comic.type == 2) {
            //本周强推
            state.weekcommend[state.weekcommend.length] = comic;
          }
          if (comic.type == 3) {
            //热门推荐
            state.hotRecommend[state.hotRecommend.length] = comic;
          }
          if (comic.type == 4) {
            //精品推荐
            state.goodRecommend[state.goodRecommend.length] = comic;
          }
        });


        var $div = $(".scBigImg dl"); //放置大图容器
        var $nav = $(".scSmallImg li"); //放置缩略图容器
        var num = -1;
        var open;

        function changeKv() {
          if (num >= $nav.length - 1) {
            num = 0;
          } else {
            num++;
          }
          $nav.eq(num).trigger("mouseenter");
          open = setTimeout(changeKv, 3000);
        }
        changeKv();

        $nav.each(function (index) {
          $(this)
            .off("mouseenter")
            .on("mouseenter", function () {
              clearTimeout(open);

              $(this).addClass("on").siblings().removeClass("on");
              $(".scBigImg dd")
                .eq(index)
                .addClass("on")
                .siblings()
                .removeClass("on");
              $(".scSmallImg")
                .off("mouseleave")
                .on("mouseleave", function () {
                  num = index;
                  setTimeout(function () {
                    changeKv();
                  }, 3000);
                });
            });
        });
        $div.each(function () {
          $(this)
            .off("mouseenter")
            .on("mouseenter", function () {
              clearTimeout(open);
            });
        });

      });

      const comicDetail = (comicId) => {
        router.push({ path: `/comic/${comicId}` });
      };



      return {
        ...toRefs(state),
        comicDetail,
      };


    }
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

#mainCenter{
  /*background-color: black;*/
  background-image: url("../assets/images/bg04.jpg");
}

#carouseBig img{
  /*width: 400px;*/
  /*height: 500px;*/
  width: 60%;
  height: 70%;
}

#carouseSmall{
  width: 20%;
  height: 20%;

}

.sliderContent{
  width: 700px;
  height: 700px;
  /*background-color: black;*/
}
</style>

