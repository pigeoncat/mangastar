
<template>
  <div class="mainNav" id="mainNav" style="background-color: black;">
    <div class="box_center cf">
      <ul class="nav" id="navModule">
        <li><router-link :to="{ name: 'home' }" style="border-color: cornflowerblue;">首页</router-link></li>
        <li>
          <router-link :to="{ name: 'comicClass' }" style="border-color: cornflowerblue;"> 全部作品 </router-link>
        </li>
        <li><router-link :to="{ name: 'comicRank' }" style="border-color: cornflowerblue;">排行榜</router-link></li>
        <!--<li class=""><a href="/pay/index.html">充值</a></li>-->
        <li><a @click="goAuthor" href="javascript:void(0)" style="border-color: cornflowerblue;">作家专区</a></li>
      </ul>
    </div>
  </div>
</template>


<script>
  import { reactive, toRefs, onMounted } from "vue";
  import { useRouter, useRoute } from "vue-router";
  import { getToken } from "@/utils/auth";
  import { getAuthorStatus } from "@/api/author"
  export default {
    name: "Navbar",
    setup() {
      const route = useRoute();
      const router = useRouter();
      const goAuthor = async () => {
        if (!getToken()) {
          router.push({
            name: "login",
          });
          return;
        }

        const { data } = await getAuthorStatus();
        if (data === null) {
          router.push({
            name: "authorRegister",
          });
          return;
        }

        let routeUrl = router.resolve({
          name: "authorComicList",
        });
        window.open(routeUrl.href, "_blank");
      };
      return {
        goAuthor,
      };
    },
  };
</script>
