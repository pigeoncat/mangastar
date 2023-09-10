<template>
  <div class="topMain">
    <div class="box_center cf">
<!--      logo-->
      <router-link :to="{ name: 'home' }" class="logo fl"><img :src="logo" alt="漫画之星" /></router-link>

<!--      搜索框-->
      <div class="searchBar fl">
        <div class="search cf">
          <input v-model="keyword" type="text" placeholder="漫画名、作者、关键字" class="s_int" style="border-color: black;" v-on:keyup.enter="searchByK" />
          <label class="search_btn" style="background-color:  black;" id="btnSearch" @click="searchByK()"><i class="icon"></i></label>
        </div>
      </div>

      <div class="bookShelf fr" id="headerUserInfo">
        <!--
        <a class="sj_link" href="/user/favorites.html">我的书架</a>-->
        <span v-if="!token" class="user_link"><!--<i class="line mr20">|</i
          >-->
          <router-link :to="{ name: 'login' }" class="mr15">登录</router-link>
          <router-link :to="{ name: 'register' }" class="mr15">注册</router-link>
        </span>
        <span v-if="token" class="user_link"><!--<i class="line mr20">|</i
          >--><router-link :to="{name:'userSetup'}" class="mr15">{{ nickName }}</router-link>
          <a @click="logout" href="javascript:void(0)">退出</a></span>
      </div>
    </div>
  </div>
</template>

<script>
  import logo from "@/assets/images/logo.png";
  import { reactive, toRefs, onMounted } from "vue";
  import { useRouter, useRoute } from "vue-router";
  import { getToken, getNickName, removeToken, removeNickName, removeUid } from "@/utils/auth";
  export default {
    name: "Top",
    setup(props, context) {
      const state = reactive({
        keyword: "",
        nickName: getNickName(),
        token: getToken(),
      });
      state.nickName = getNickName();
      state.token = getToken();
      const route = useRoute();
      const router = useRouter();
      state.keyword = route.query.key;
      const searchByK = () => {
        router.push({ path: "/comicClass", query: { key: state.keyword } });
        context.emit("eventSerch", state.keyword);
      };
      const logout = () => {
        removeToken();
        removeNickName();
        removeUid()
        state.nickName = "";
        state.token = "";
      };
      return {
        ...toRefs(state),
        logo,
        searchByK,
        logout,
      };
    },
  };
</script>

