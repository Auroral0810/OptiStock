<script setup>
import { ref, shallowRef } from 'vue'
import { HomeFilled, Goods, Box, SoldOut, DataAnalysis, User as UserIcon, Setting, SwitchButton } from '@element-plus/icons-vue'
import DefaultDashboard from '@/pages/default.vue'
import { useRouter } from 'vue-router'

// 商品管理
import GoodsList from '@/pages/Goods/GoodsList.vue'
import GoodsCategory from '@/pages/Goods/GoodsCategory.vue'
import InventoryWarning from '@/pages/Goods/InventoryWarning.vue'

// 库存管理
import StockIn from '@/pages/Stock/StockIn.vue'
import StockInRecords from '@/pages/Stock/StockInRecords.vue'
import StockOut from '@/pages/Stock/StockOut.vue'
import StockOutRecords from '@/pages/Stock/StockOutRecords.vue'
import SupplierManage from '@/pages/Stock/SupplierManage.vue'

// 数据统计
import InventoryStatistics from '@/pages/Statistics/InventoryStatistics.vue'
import SalesAnalysis from '@/pages/Statistics/SalesAnalysis.vue'
import PurchaseAnalysis from '@/pages/Statistics/PurchaseAnalysis.vue'

// 系统设置
import Profile from '@/pages/Settings/Profile.vue'
import ChangePassword from '@/pages/Settings/ChangePassword.vue'
import SystemConfig from '@/pages/Settings/SystemConfig.vue'

import { useAuthStore } from '@/utils/auth'

const authStore = useAuthStore()
//头像和昵称数据绑定
const router = useRouter()
const nickname = ref(authStore.userInfo.nickname?authStore.userInfo.nickname:'用户昵称')
const avatarUrl = ref(authStore.userInfo.avatar?authStore.userInfo.avatar:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png')

// 添加面包屑导航数据
const breadcrumb = ref(['系统概览'])

// 当前显示的组件（shallowRef 避免 Vue 过度响应式处理）
const currentComponent = shallowRef(DefaultDashboard)

// 组件映射表
const componentMap = {
  DefaultDashboard,
  GoodsList,
  GoodsCategory,
  InventoryWarning,
  StockIn,
  StockInRecords,
  StockOut,
  StockOutRecords,
  SupplierManage,
  InventoryStatistics,
  SalesAnalysis,
  PurchaseAnalysis,
  Profile,
  ChangePassword,
  SystemConfig
}

// 菜单配置
const menuItems = [
    {
      index: '1',
      icon: HomeFilled,
      title: '首页',
      component: 'DefaultDashboard'
    },
    {
      index: '2',
      icon: Goods,
      title: '商品管理',
      children: [
        { index: '2-1', title: '商品列表', component: 'GoodsList' },
        { index: '2-2', title: '商品分类', component: 'GoodsCategory' },
        { index: '2-3', title: '库存查询', component: 'StockOverview' }, // ✅ 新增库存查询，合并库存管理
        { index: '2-4', title: '库存预警', component: 'InventoryWarning' }
      ]
    },
    {
      index: '3',
      icon: Box,
      title: '采购管理',
      children: [
        { index: '3-1', title: '采购计划', component: 'PurchasePlan' },
        { index: '3-2', title: '采购订单', component: 'PurchaseOrder' },
        { index: '3-3', title: '采购入库', component: 'StockIn' },
        { index: '3-4', title: '供应商管理', component: 'SupplierManage' } // ✅ 供应商管理移动到采购模块
      ]
    },
    {
      index: '4',
      icon: SoldOut,
      title: '订单与出库',
      children: [
        { index: '4-1', title: '订单管理', component: 'OrderManagement' }, // ✅ 订单管理从出库中分离
        { index: '4-2', title: '订单出库', component: 'OrderStockOut' },  // ✅ 订单相关出库
        { index: '4-3', title: '退货与损耗', component: 'ReturnAndLoss' } // ✅ 新增退货损耗管理
      ]
    },
    {
      index: '5',
      icon: DataAnalysis,
      title: '数据统计',
      children: [
        { index: '5-1', title: '库存统计', component: 'InventoryStatistics' },
        { index: '5-2', title: '销售分析', component: 'SalesAnalysis' },
        { index: '5-3', title: '采购分析', component: 'PurchaseAnalysis' }
      ]
    },
    {
      index: '6',
      icon: Setting,
      title: '系统管理',
      children: [
        { index: '6-1', title: '修改个人信息', component: 'Profile' },
        { index: '6-2', title: '修改密码', component: 'ChangePassword' },
        { index: '6-3', title: '系统配置', component: 'SystemConfig' },
        { index: '6-4', title: '退出登录', component: 'Logout' }
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
// 处理菜单点击
const handleMenuSelect = (index) => {
  // 处理退出登录
  if(index === '6-4') {
    handleLogout()
    return
  }

  // 更新面包屑和组件
  const paths = index.split('-')
  const mainMenu = menuItems.find(item => item.index === paths[0])
  
  if(paths.length === 1) {
    breadcrumb.value = [mainMenu.title]
    if(mainMenu.component) {
      currentComponent.value = componentMap[mainMenu.component]
    }
  } else {
    const subMenu = mainMenu.children.find(item => item.index === index)
    breadcrumb.value = [mainMenu.title, subMenu.title]
    if(subMenu.component) {
      currentComponent.value = componentMap[subMenu.component]
    }
  }
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
              <el-dropdown-item>
                <el-icon><UserIcon /></el-icon>
                个人中心
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
    <el-container class="main-container">
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
        <!-- 动态组件 -->
        <component :is="currentComponent" />
      </el-main>
      
    </el-container>

    <!-- 版权信息 -->
    <div class="copyright">
      <p>© 2024 OptiStock 仓储管理系统 版权所有</p>
    </div>
  </div>
</template>

<style scoped>
.homepage-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  position: relative;
  z-index: 10;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  background: linear-gradient(45deg, #409EFF, #36cfc9);
  -webkit-background-clip: text;
  color: transparent;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 4px;
  transition: all 0.3s;
}

.user-info:hover {
  background: rgba(64,158,255,0.1);
}

.nickname {
  margin: 0 12px;
  font-size: 14px;
  color: #606266;
}

.arrow-icon {
  color: #909399;
  transition: transform 0.3s;
}

.main-container {
  flex: 1;
  height: calc(100vh - 60px);
}

.aside {
  background: #001529;
  transition: all 0.3s;
  box-shadow: 2px 0 8px rgba(0,0,0,0.15);
}

.main {
  padding: 0;
  background: #f5f7fa;
  overflow-y: auto;
}

.breadcrumb {
  margin-bottom: 24px;
  padding: 24px 24px 0;
}

.overview-section {
  margin-bottom: 24px;
}

.overview-card {
  margin-bottom: 20px;
  height: 180px;
}

.overview-item {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}

.overview-title {
  font-size: 16px;
  color: #909399;
}

.overview-number {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin: 16px 0;
}

.overview-footer {
  display: flex;
  align-items: center;
}

.charts-section {
  .chart-row {
    margin-bottom: 20px;
  }
}

.chart-card {
  height: 400px;
  margin-bottom: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-content {
  height: calc(100% - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
}

:deep(.el-card) {
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  }
}

:deep(.el-card__body) {
  height: calc(100% - 60px);
  padding: 0;
}

:deep(.el-empty) {
  padding: 40px 0;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu-item.is-active) {
  background-color: #1890ff !important;
  color: #ffffff !important;
}

:deep(.el-menu-item):hover {
  background-color: #1890ff !important;
  color: #ffffff !important;
}

:deep(.el-sub-menu__title):hover {
  background-color: rgba(24,144,255,0.1) !important;
}

:deep(.el-dropdown-menu) {
  padding: 4px 0;
}

:deep(.el-dropdown-menu__item) {
  line-height: 36px;
  padding: 0 20px;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: #ecf5ff;
  color: #409EFF;
}

:deep(.el-tag) {
  margin: 4px;
}

.copyright {
  text-align: center;
  padding: 20px 0;
  background: #fff;
  color: #606266;
  font-size: 14px;
  border-top: 1px solid #e4e7ed;
}
</style>