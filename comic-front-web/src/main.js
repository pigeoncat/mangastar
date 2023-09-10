import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from '@/router'
import '@/assets/styles/base.css'
import '@/assets/styles/main.css'
import store from "@/store";

const app = createApp(App)

app.use(ElementPlus)

app.use(router)

app.use(store)

app.mount('#app')