<script setup>
import { ref, computed } from 'vue'
import { HomeFilled, Goods, Box, SoldOut, DataAnalysis, User as UserIcon, Setting, SwitchButton, Key, InfoFilled, ArrowDown } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/utils/auth'

const authStore = useAuthStore()
//头像和昵称数据绑定
const router = useRouter()
const avatarUrl = computed(() => authStore.userInfo.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')
const nickname = computed(() => authStore.userInfo.nickname || '用户昵称')
// 添加面包屑导航数据
const breadcrumb = ref(['系统概览'])

// 菜单配置修改，添加路由路径
const menuItems = [
    {
      index: '1',
      icon: HomeFilled,
      title: '系统概览',
      path: '/homepage/default'
    },
    {
      index: '2',
      icon: Goods,
      title: '商品管理',
      children: [
        { index: '2-1', title: '商品列表', path: '/product/list' },
        { index: '2-2', title: '商品分类', path: '/product/category' },
        { index: '2-3', title: '库存管理', path: '/product/stock' },
        { index: '2-4', title: '库存预警', path: '/product/warning' }
      ]
    },
    {
      index: '3',
      icon: Box,
      title: '采购管理',
      children: [
        { index: '3-1', title: '采购订单', path: '/purchase/order' },
        { index: '3-2', title: '供应商管理', path: '/purchase/supplier' },
        // { index: '3-3', title: '采购入库', path: '/purchase/stockin' },
        // { index: '3-4', title: '供应商管理', path: '/purchase/supplier' }
      ]
    },
    {
      index: '4',
      icon: SoldOut,
      title: '订单与退货',
      children: [
        { index: '4-1', title: '订单管理', path: '/order/manage' },
        // { index: '4-2', title: '订单出库', path: '/order/stockout' },
        { index: '4-2', title: '退货管理', path: '/order/return' }
      ]
    },
    {
      index: '5',
      icon: DataAnalysis,
      title: '数据统计',
      children: [
        { index: '5-1', title: '库存统计', path: '/stats/inventory' },
        { index: '5-2', title: '销售分析', path: '/stats/sales' },
        { index: '5-3', title: '采购分析', path: '/stats/purchase' }
      ]
    },
    {
      index: '6',
      icon: Setting,
      title: '系统管理',
      children: [
        { index: '6-1', title: '修改个人信息', path: '/settings/profile' },
        { index: '6-2', title: '修改密码', path: '/settings/password' },
        { index: '6-3', title: '关于我们', path: '/settings/config' },
        { index: '6-4', title: '退出登录', path: '/settings/logout' }
      ]
    }
  ]
  

const handleOpen = (key, keyPath) => {
  console.log('打开', key, keyPath)
}

const handleClose = (key, keyPath) => {
  console.log('关闭', key, keyPath)
}

import { ElMessageBox, ElMessage } from 'element-plus'

//处理退出函数
const handleLogout = () => {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
        // 清除 Pinia 认证存储
      authStore.logout()
      router.push('/login')
    }).catch(() => {
      ElMessage.info('取消退出登录')
    })
}

// 处理菜单点击修改为路由跳转
const handleMenuSelect = (index) => {
  // 处理退出登录
  if(index === '6-4') {
    handleLogout()
    return
  }

  // 更新面包屑和路由跳转
  const paths = index.split('-')
  const mainMenu = menuItems.find(item => item.index === paths[0])
  
  if(paths.length === 1) {
    breadcrumb.value = [mainMenu.title]
    if(mainMenu.path) {
      router.push(mainMenu.path)
    }
  } else {
    const subMenu = mainMenu.children.find(item => item.index === index)
    breadcrumb.value = [mainMenu.title, subMenu.title]
    if(subMenu.path) {
      router.push(subMenu.path)
    }
  }
}

// 处理头像下拉菜单点击
const handleDropdownClick = (path) => {
  if(path === '/settings/profile') {
    breadcrumb.value = ['系统管理', '修改个人信息']
  } else if(path === '/settings/password') {
    breadcrumb.value = ['系统管理', '修改密码'] 
  } else if(path === '/settings/config') {
    breadcrumb.value = ['系统管理', '关于我们']
  }
  router.push(path)
}
</script>

<template>
  <div class="homepage-container">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-left">
        <el-link class="logo" type="primary" :underline="false">
          OptiStock
        </el-link>
      </div>
      
      <div class="header-right">
        <el-dropdown trigger="click" placement="bottom-end">
          <div class="user-info">
            <span class="nickname">{{ nickname }}</span>
            <el-avatar :size="36" :src="avatarUrl" class="avatar" />
            <el-icon class="arrow-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu class="user-dropdown">
              <el-dropdown-item @click="handleDropdownClick('/settings/profile')">
                <el-icon><UserIcon /></el-icon>
                修改个人信息
              </el-dropdown-item>
              <el-dropdown-item @click="handleDropdownClick('/settings/password')">
                <el-icon><Key /></el-icon>
                修改密码
              </el-dropdown-item>
              <el-dropdown-item @click="handleDropdownClick('/settings/config')">
                <el-icon><InfoFilled /></el-icon>
                关于我们
              </el-dropdown-item>
              <el-dropdown-item divided @click="handleLogout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主体内容区 -->
    <el-container class="main-container" >
      <!-- 左侧菜单 -->
      <el-aside width="220px" class="aside">
        <el-menu
          default-active="1"
          class="el-menu-vertical-demo"
          :collapse="false"
          background-color="#001529"
          text-color="rgba(255,255,255,0.65)"
          active-text-color="#fff"
          @select="handleMenuSelect"
          @open="handleOpen"
          @close="handleClose"
          unique-opened>
          <template v-for="item in menuItems" :key="item.index">
            <!-- 没有子菜单的菜单项 -->
            <el-menu-item v-if="!item.children" :index="item.index">
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.title }}</span>
            </el-menu-item>
            
            <!-- 有子菜单的菜单项 -->
            <el-sub-menu v-else :index="item.index">
              <template #title>
                <el-icon><component :is="item.icon" /></el-icon>
                <span>{{ item.title }}</span>
              </template>
              
              <!-- 分组显示子菜单 -->
              <template v-for="(group, groupIndex) in item.children" :key="groupIndex">
                <el-menu-item-group 
                  v-if="group.groupTitle"
                  :title="group.groupTitle">
                  <el-menu-item :index="group.index">
                    {{ group.title }}
                  </el-menu-item>
                </el-menu-item-group>
                
                <el-menu-item 
                  v-else
                  :index="group.index">
                  {{ group.title }}
                </el-menu-item>
              </template>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-aside>

      <!-- 右侧主体内容 -->
      <el-main class="main">
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" class="breadcrumb">
          <el-breadcrumb-item v-for="item in breadcrumb" :key="item">{{ item }}</el-breadcrumb-item>
        </el-breadcrumb>
        <router-view></router-view>
      </el-main>
      
    </el-container>

  </div>
</template>

<style scoped>
/* 主体页面布局 */
.homepage-container {
  height: 100vh;
  background-color: #f5f7fa;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止页面滚动 */
}

/* 顶部导航栏 */
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  height: 60px;
  background: #ffffff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.08);
  position: relative;
  z-index: 10;
  flex-shrink: 0; /* 防止头部压缩 */
}

.logo {
  font-size: 26px;
  font-weight: bold;
  background: linear-gradient(120deg, #409EFF, #36cfc9);
  -webkit-background-clip: text;
  color: transparent;
  letter-spacing: 1px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 16px;
  border-radius: 6px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(64,158,255,0.12);
}

.nickname {
  margin: 0 14px;
  font-size: 15px;
  color: #4a4a4a;
  font-weight: 500;
}

.arrow-icon {
  color: #909399;
  transition: transform 0.3s ease;
}

/* 主体内容区域 */
.main-container {
  flex: 1;
  display: flex;
  height: calc(100vh - 60px);
  overflow: hidden; /* 防止内容区域滚动 */
}

/* 侧边栏 */
.aside {
  width: 200px; /* 减小侧边栏宽度 */
  background: #001529;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0,0,0,0.1);
  flex-shrink: 0; /* 防止侧边栏压缩 */
  overflow-y: auto; /* 允许菜单滚动 */
}

/* 主内容区 */
.main {
  flex: 1;
  padding: 0;
  background: #f5f7fa;
  display: flex;
  flex-direction: column;
  overflow-y: auto; /* 允许主内容区域滚动 */
}

/* 面包屑导航 */
.breadcrumb {
  margin: 0;
  padding: 16px 32px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  flex-shrink: 0; /* 防止面包屑压缩 */
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  padding: 24px 32px;
  position: relative;
}

/* Element Plus 组件样式重写 */
:deep(.el-menu) {
  border-right: none;
  background: #001529;
}

:deep(.el-menu-item) {
  height: 50px;
  line-height: 50px;
  
  &.is-active {
    background-color: #1890ff !important;
    color: #ffffff !important;
  }
  
  &:hover {
    background-color: rgba(24,144,255,0.8) !important;
    color: #ffffff !important;
  }
}

:deep(.el-sub-menu__title) {
  &:hover {
    background-color: rgba(255,255,255,0.05) !important;
  }
}

:deep(.el-menu-item-group__title) {
  padding: 8px 20px;
  color: rgba(255,255,255,0.45);
  font-size: 12px;
}

/* 下拉菜单样式 */
:deep(.el-dropdown-menu) {
  padding: 6px 0;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

:deep(.el-dropdown-menu__item) {
  line-height: 38px;
  padding: 0 24px;
  font-size: 14px;
  
  &:hover {
    background-color: #ecf5ff;
    color: #409EFF;
  }
}

/* 响应式布局调整 */
@media screen and (max-width: 1200px) {
  .aside {
    width: 160px;
  }
}

@media screen and (max-width: 768px) {
  .header {
    padding: 0 16px;
  }
  
  .aside {
    width: 64px;
  }
  
  .main {
    padding: 0;
  }
}
</style>