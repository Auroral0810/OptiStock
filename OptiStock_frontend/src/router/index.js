import { createRouter, createWebHistory } from 'vue-router'

// 登录 & 主页
import Login from '@/pages/Login.vue'
import HomePage from '@/pages/HomePage.vue'
import { useAuthStore } from '@/utils/auth'
import defaultPage from '@/pages/default.vue'
const routes = [
  { path: '/', redirect: '/login' },  // 默认跳转到登录页
  { path: '/login', name: 'Login', component: Login },

  {
    path: '/homepage',
    name: 'HomePage', 
    component: HomePage,
    meta: { requiresAuth: true },  // 需要登录
    children: [
      { path: '/homepage/default', name: 'defaultPage', component:defaultPage},
      // 商品管理
      { path: '/product/list', name: 'ProductList', component: () => import('@/pages/Product/ProductList.vue') },
      { path: '/product/category', name: 'ProductCategory', component: () => import('@/pages/Product/ProductCategory.vue') },
      { path: '/product/stock', name: 'ProductStock', component: () => import('@/pages/Product/ProductStock.vue') },
      { path: '/product/warning', name: 'ProductWarning', component: () => import('@/pages/Product/ProductWarning.vue') },

      // 采购管理
      // { path: '/purchase/plan', name: 'PurchasePlan', component: () => import('@/pages/Purchase/PurchasePlan.vue') },
      { path: '/purchase/order', name: 'PurchaseOrder', component: () => import('@/pages/Purchase/PurchaseOrder.vue') },
      // { path: '/purchase/stockin', name: 'PurchaseStockin', component: () => import('@/pages/Purchase/PurchaseStockin.vue') },
      { path: '/purchase/supplier', name: 'PurchaseSupplier', component: () => import('@/pages/Purchase/PurchaseSupplier.vue') },

      // 订单与出库
      { path: '/order/manage', name: 'OrderManage', component: () => import('@/pages/Order/OrderManage.vue') },
      { path: '/order/stockout', name: 'OrderStockout', component: () => import('@/pages/Order/OrderStockout.vue') },
      { path: '/order/return', name: 'OrderReturn', component: () => import('@/pages/Order/OrderReturn.vue') },

      // 数据统计
      { path: '/stats/inventory', name: 'StatsInventory', component: () => import('@/pages/Statistics/InventoryStatistics.vue') },
      { path: '/stats/sales', name: 'StatsSales', component: () => import('@/pages/Statistics/SalesAnalysis.vue') },
      { path: '/stats/purchase', name: 'StatsPurchase', component: () => import('@/pages/Statistics/PurchaseAnalysis.vue') },

      // 系统管理
      { path: '/settings/profile', name: 'SettingsProfile', component: () => import('@/pages/Settings/Profile.vue') },
      { path: '/settings/password', name: 'SettingsPassword', component: () => import('@/pages/Settings/ChangePassword.vue') },
      { path: '/settings/config', name: 'SettingsConfig', component: () => import('@/pages/Settings/SystemConfig.vue') }
    ]
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
