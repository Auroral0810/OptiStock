<template>
  <div class="inventory-statistics">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">库存统计分析</h1>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>总库存商品数</span>
                <el-icon><Goods /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="dashboard" :percentage="mockData.totalInventoryPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ mockData.totalInventory }}</span>
                <span class="unit">件</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>库存总价值</span>
                <el-icon><Money /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="circle" :percentage="mockData.totalValuePercentage" :width="120" status="success"/>
              <div class="stat-info">
                <span class="number">{{ mockData.totalValue }}</span>
                <span class="unit">元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>低库存预警</span>
                <el-icon><Warning /></el-icon>
              </div>
            </template>
            <div class="card-content warning">
              <el-progress type="dashboard" :percentage="mockData.lowStockPercentage" :width="120" status="warning"/>
              <div class="stat-info">
                <span class="number">{{ mockData.lowStockCount }}</span>
                <span class="unit">项</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>库存周转率</span>
                <el-icon><TopRight /></el-icon>
              </div>
            </template>
            <div class="card-content success">
              <el-progress type="circle" :percentage="mockData.turnoverPercentage" :width="120" status="success"/>
              <div class="stat-info">
                <span class="number">{{ mockData.turnoverRate }}</span>
                <span class="unit">次/年</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="charts-section">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">库存最多的十大商品</div>
            <div id="topTenChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">库存最少的十大商品</div>
            <div id="bottomTenChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">库存预警商品分布</div>
            <div id="warningChart" class="chart" style="height: 400px"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">库存分类占比</div>
            <div id="categoryChart" class="chart" style="height: 400px"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Goods, Money, Warning, TopRight } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 模拟数据
const mockData = reactive({
  totalInventory: 1234,
  totalInventoryPercentage: 80,
  totalValue: 456789,
  totalValuePercentage: 65,
  lowStockCount: 12,
  lowStockPercentage: 20,
  turnoverRate: 3.5,
  turnoverPercentage: 75,
  topTenProducts: [
    { name: '手机', value: 1200 },
    { name: '笔记本电脑', value: 1000 },
    { name: '平板电脑', value: 800 },
    { name: '智能手表', value: 700 },
    { name: '耳机', value: 600 },
    { name: '充电器', value: 500 },
    { name: '保护壳', value: 400 },
    { name: '数据线', value: 300 },
    { name: '移动电源', value: 200 },
    { name: '支架', value: 100 }
  ],
  bottomTenProducts: [
    { name: '键盘', value: 5 },
    { name: '鼠标', value: 8 },
    { name: '摄像头', value: 10 },
    { name: '音箱', value: 12 },
    { name: '麦克风', value: 15 },
    { name: '路由器', value: 18 },
    { name: '硬盘', value: 20 },
    { name: '内存条', value: 22 },
    { name: 'CPU', value: 25 },
    { name: '显卡', value: 28 }
  ],
  warningDistribution: [
    { value: 8, name: '严重不足' },
    { value: 12, name: '库存偏低' },
    { value: 15, name: '即将不足' }
  ],
  categoryDistribution: [
    { value: 35, name: '手机数码' },
    { value: 25, name: '电脑配件' },
    { value: 20, name: '智能穿戴' },
    { value: 15, name: '配件耗材' },
    { value: 5, name: '其他' }
  ]
})

// 图表实例
let topTenChart = null
let bottomTenChart = null
let warningChart = null
let categoryChart = null

// 初始化库存最多的十大商品图表
const initTopTenChart = () => {
  const chartDom = document.getElementById('topTenChart')
  topTenChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      name: '库存数量'
    },
    yAxis: {
      type: 'category',
      data: mockData.topTenProducts.map(item => item.name),
      axisLabel: {
        interval: 0
      }
    },
    series: [{
      name: '库存数量',
      type: 'bar',
      data: mockData.topTenProducts.map(item => item.value),
      itemStyle: {
        color: '#409EFF'
      }
    }]
  }
  topTenChart.setOption(option)
}

// 初始化库存最少的十大商品图表
const initBottomTenChart = () => {
  const chartDom = document.getElementById('bottomTenChart')
  bottomTenChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      name: '库存数量'
    },
    yAxis: {
      type: 'category',
      data: mockData.bottomTenProducts.map(item => item.name),
      axisLabel: {
        interval: 0
      }
    },
    series: [{
      name: '库存数量',
      type: 'bar',
      data: mockData.bottomTenProducts.map(item => item.value),
      itemStyle: {
        color: '#E6A23C'
      }
    }]
  }
  bottomTenChart.setOption(option)
}

// 初始化预警商品分布图
const initWarningChart = () => {
  const chartDom = document.getElementById('warningChart')
  warningChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        name: '预警状态',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '20',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: mockData.warningDistribution[0].value, name: '严重不足', itemStyle: { color: '#F56C6C' } },
          { value: mockData.warningDistribution[1].value, name: '库存偏低', itemStyle: { color: '#E6A23C' } },
          { value: mockData.warningDistribution[2].value, name: '即将不足', itemStyle: { color: '#67C23A' } }
        ]
      }
    ]
  }
  warningChart.setOption(option)
}

// 初始化库存分类占比图
const initCategoryChart = () => {
  const chartDom = document.getElementById('categoryChart')
  categoryChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '库存分类',
        type: 'pie',
        radius: '60%',
        data: mockData.categoryDistribution,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  categoryChart.setOption(option)
}

// 页面加载时初始化图表
onMounted(() => {
  initTopTenChart()
  initBottomTenChart()
  initWarningChart()
  initCategoryChart()
  
  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    topTenChart?.resize()
    bottomTenChart?.resize()
    warningChart?.resize()
    categoryChart?.resize()
  })
})
</script>

<style scoped>
.inventory-statistics {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.main-title {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.statistics-cards {
  margin-bottom: 20px;
}

.stat-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 16px;
  }

  .card-content {
    text-align: center;
    padding: 10px 0;
    display: flex;
    flex-direction: column;
    align-items: center;

    .stat-info {
      margin-top: 10px;
    }

    .number {
      font-size: 24px;
      font-weight: bold;
      color: #409EFF;
    }

    .unit {
      margin-left: 5px;
      font-size: 14px;
      color: #909399;
    }

    &.warning .number {
      color: #E6A23C;
    }

    &.success .number {
      color: #67C23A;
    }
  }
}

.charts-section {
  margin-bottom: 20px;

  .chart-card {
    .chart-title {
      font-size: 16px;
      margin-bottom: 20px;
      padding: 10px;
      border-bottom: 1px solid #EBEEF5;
    }

    .chart {
      height: 300px;
    }
  }
}
</style>
