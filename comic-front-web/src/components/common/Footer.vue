 <template>
  <div class="footer">
    <div class="box_center cf">
      <div class="copyright">
        <ul>
          
          <li>
            Copyright (C) pigeoncat All rights
            reserved&nbsp;&nbsp;漫画之星版权所有
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from "vue-router";
import { getToken } from "@/utils/auth";
export default {
  name: "Footer",
  setup() {
    const router = useRouter();
    const goFeedBack = () => {
      if(!getToken()){
        router.push({name: 'login'});
      }else{
      router.push({name: 'feadback'});
      }
    };
    return {
      goFeedBack
    }
  },
  
};
</script>