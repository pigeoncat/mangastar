import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  // createWebHistory 路由模式路径不带#号(生产环境下不能直接访问项目，需要 nginx 转发)
  // createWebHashHistory 路由模式路径带#号
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('@/views/Home')
    },
     // 注册
     {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register')
    },
    // 登录
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login')
    },
    // 用户
    {
      path: '/user/setup',
      name: 'userSetup',
      component: () => import('@/views/UserSetup')
    },
    // 全部作品
    {
      path: '/comicClass',
      name: 'comicClass',
      component: () => import('@/views/ComicClass')
    },
    // 排行榜
    {
      path: '/comicRank',
      name: 'comicRank',
      component: () => import('@/views/ComicRank')
    },
    // 详情页
    {
      path: '/comic/:id',
      name: 'comic',
      component: () => import('@/views/Comic')
    },
      //漫画阅读页
    {
      path: '/comic/:id/:chapterId',
      name: 'comicContent',
      component: () => import('@/views/ComicContent')

    },
  ]
})

// 解决 vue 中路由跳转时，总是从新页面中间开始显示
router.afterEach((to, from, next) => {
  window.scrollTo(0, 0)
})

export default router