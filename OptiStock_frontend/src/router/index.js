import { createRouter, createWebHistory } from 'vue-router'

// 登录 & 主页
import Login from '@/pages/Login.vue'
import HomePage from '@/pages/HomePage.vue'
import { useAuthStore } from '@/utils/auth'

const routes = [
  { path: '/', redirect: '/login' },  // 默认跳转到登录页
  { path: '/login', name: 'Login', component: Login },

  {
    path: '/homepage',
    name: 'HomePage',
    component: HomePage,
    meta: { requiresAuth: true },  // 需要登录
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})


// **添加全局前置守卫**

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  console.log('🔍 当前路由:', to.path)
  console.log('🛡️ 用户 Token:', authStore.userToken)

  if (to.meta.requiresAuth && !authStore.userToken) {
    console.log('🚨 未登录，跳转至 /login')
    next('/login')
  } else {
    next()
  }
})
export default router
