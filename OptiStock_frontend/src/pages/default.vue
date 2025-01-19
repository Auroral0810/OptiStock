<template>
    <div class="dashboard-container">
      <!-- 数据概览卡片 -->
      <el-row :gutter="16" class="overview-section">
        <el-col :xs="24" :sm="12" :md="6" v-for="(item, index) in overviewItems" :key="index">
          <el-card class="overview-card" :class="`card-${item.tagType}`" shadow="hover">
            <div class="overview-item">
              <div class="overview-header">
                <div class="overview-title">
                  <el-icon class="icon" :size="20"><component :is="item.icon" /></el-icon>
                  {{ item.title }}
                </div>
                <el-tag :type="item.tagType" size="small" effect="dark">{{ item.tag }}</el-tag>
              </div>
              <div class="overview-number">{{ item.value }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
  
      <!-- 图表区域 -->
      <el-row :gutter="16" class="charts-section">
        <!-- 左侧大图表 -->
        <el-col :span="16" class="left-charts">
          <!-- 销售趋势图 -->
          <el-card class="trend-chart" shadow="hover">
            <template #header>
              <div class="card-header">
                <div class="header-left">
                  <span class="title">销售趋势分析</span>
                  <el-tag type="info" effect="plain" size="small">同比增长 12%</el-tag>
                </div>
                <el-radio-group v-model="timeRange" size="small">
                  <el-radio-button label="week">本周</el-radio-button>
                  <el-radio-button label="month">本月</el-radio-button>
                  <el-radio-button label="year">全年</el-radio-button>
                </el-radio-group>
              </div>
            </template>
            <div class="chart-content">
              <el-empty description="图表加载中..." />
            </div>
          </el-card>
          
          <!-- 下方两个小图表 -->
          <el-row :gutter="16" class="bottom-charts">
            <el-col :span="12">
              <el-card class="small-chart warning-card" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <div class="header-left">
                      <span class="title">库存预警</span>
                      <el-tag type="danger" effect="dark" size="small">5个商品库存不足</el-tag>
                    </div>
                  </div>
                </template>
                <div class="chart-content">
                  <el-empty description="图表加载中..." />
                </div>
              </el-card>
            </el-col>
            <el-col :span="12">
              <el-card class="small-chart" shadow="hover">
                <template #header>
                  <div class="card-header">
                    <div class="header-left">
                      <span class="title">出入库统计</span>
                      <el-tag type="warning" effect="dark" size="small">本月入库: 56</el-tag>
                    </div>
                  </div>
                </template>
                <div class="chart-content">
                  <el-empty description="图表加载中..." />
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
  
        <!-- 右侧图表和列表 -->
        <el-col :span="8" class="right-section">
          <!-- 饼图 -->
          <el-card class="pie-chart" shadow="hover">
            <template #header>
              <div class="card-header">
                <span class="title">商品分类占比</span>
              </div>
            </template>
            <div class="chart-content">
              <el-empty description="图表加载中..." />
            </div>
          </el-card>
  
          <!-- 排行榜 -->
          <el-card class="rank-list" shadow="hover">
            <template #header>
              <div class="card-header">
                <div class="header-left">
                  <span class="title">热销商品排行</span>
                  <el-tag type="success" effect="dark" size="small">TOP 5</el-tag>
                </div>
              </div>
            </template>
            <div class="list-content">
              <div v-for="(item, index) in rankList" :key="index" class="rank-item">
                <span class="rank-index" :class="`rank-${index + 1}`">{{ index + 1 }}</span>
                <span class="rank-name">{{ item.name }}</span>
                <span class="rank-value">{{ item.value }}</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { TrendCharts, Money, Goods, List, Box } from '@element-plus/icons-vue'
  
  const timeRange = ref('month')
  const rankList = ref([
    { name: '商品A', value: '¥12,580' },
    { name: '商品B', value: '¥10,234' },
    { name: '商品C', value: '¥8,546' },
    { name: '商品D', value: '¥6,842' },
    { name: '商品E', value: '¥5,421' }
  ])
  
  const overviewItems = [
    {
      title: '总销售额',
      value: '¥12,580',
      tag: '较昨日 +12%',
      tagType: 'success',
      icon: Money
    },
    {
      title: '商品总数',
      value: '856',
      tag: '种类数量',
      tagType: 'primary',
      icon: Goods
    },
    {
      title: '订单数量',
      value: '328',
      tag: '本月订单总数',
      tagType: 'warning',
      icon: List
    },
    {
      title: '库存总值',
      value: '¥985,600',
      tag: '当前库存价值',
      tagType: 'info',
      icon: Box
    }
  ]
  </script>
  
  <style scoped>
  .dashboard-container {
    height: calc(100vh - 120px);
    padding: 16px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    background-color: #f6f8fb;
  }
  
  /* 概览卡片样式 */
  .overview-section {
    margin-bottom: 16px;
  }
  
  .overview-card {
    height: 100px;
    border: none;
    transition: all 0.3s;
    position: relative;
    overflow: hidden;
  }
  
  .overview-card::after {
    content: '';
    position: absolute;
    top: 0;
    right: 0;
    width: 100px;
    height: 100px;
    background: linear-gradient(45deg, transparent, rgba(255,255,255,0.1));
    border-radius: 50%;
    transform: translate(50%, -50%);
  }
  
  .card-success {
    background: linear-gradient(135deg, #67C23A, #95D475);
  }
  
  .card-primary {
    background: linear-gradient(135deg, #409EFF, #79BBFF);
  }
  
  .card-warning {
    background: linear-gradient(135deg, #E6A23C, #F3D19E);
  }
  
  .card-info {
    background: linear-gradient(135deg, #909399, #C8C9CC);
  }
  
  .overview-item {
    height: 100%;
    padding: 16px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    color: white;
  }
  
  .overview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .overview-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
  }
  
  .icon {
    background: rgba(255,255,255,0.2);
    padding: 8px;
    border-radius: 8px;
  }
  
  .overview-number {
    font-size: 28px;
    font-weight: bold;
  }
  
  /* 图表卡片样式 */
  .charts-section {
    flex: 1;
    min-height: 0;
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 20px;
  }
  
  .header-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }
  
  .title {
    font-size: 16px;
    font-weight: 500;
    color: #1f2f3d;
  }
  
  .trend-chart, .small-chart, .pie-chart, .rank-list {
    background: white;
    border: none;
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  }
  
  /* 排行榜样式 */
  .rank-item {
    display: flex;
    align-items: center;
    padding: 12px;
    border-bottom: 1px solid #f0f0f0;
    transition: all 0.3s;
  }
  
  .rank-item:hover {
    background: #f6f8fb;
  }
  
  .rank-index {
    width: 28px;
    height: 28px;
    line-height: 28px;
    text-align: center;
    border-radius: 6px;
    margin-right: 16px;
    font-weight: bold;
  }
  
  .rank-1 { background: #ff4d4f; color: white; }
  .rank-2 { background: #ffa940; color: white; }
  .rank-3 { background: #40a9ff; color: white; }
  .rank-4, .rank-5 { background: #f0f0f0; color: #666; }
  
  .rank-name {
    flex: 1;
    font-size: 14px;
    color: #1f2f3d;
  }
  
  .rank-value {
    font-weight: 500;
    color: #1f2f3d;
  }
  
  /* 布局调整 */
  .left-charts {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
  }
  
  .trend-chart {
    height: 60%;
  }
  
  .bottom-charts {
    height: 40%;
  }
  
  .right-section {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
  }
  
  .pie-chart, .rank-list {
    height: calc(50% - 8px);
  }
  
  /* Element Plus 组件样式优化 */
  :deep(.el-card) {
    overflow: visible;
  }
  
  :deep(.el-tag) {
    border: none;
  }
  
  :deep(.el-radio-button__inner) {
    border-radius: 6px;
  }
  
  :deep(.el-empty) {
    padding: 24px 0;
  }
  </style>