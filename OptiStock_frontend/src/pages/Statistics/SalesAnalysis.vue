<template>
  <div class="sales-analysis">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">销售分析</h1>
        <div class="date-picker">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="handleDateChange"
          />
        </div>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>总销售额</span>
                <el-icon><Money /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="dashboard" :percentage="mockData.totalSalesPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ mockData.totalSales }}</span>
                <span class="unit">元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>订单总数</span>
                <el-icon><Document /></el-icon>
              </div>
            </template>
            <div class="card-content success">
              <el-progress type="circle" :percentage="mockData.orderCountPercentage" :width="120" status="success"/>
              <div class="stat-info">
                <span class="number">{{ mockData.orderCount }}</span>
                <span class="unit">单</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>客单价</span>
                <el-icon><PriceTag /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="dashboard" :percentage="mockData.averageOrderPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ mockData.averageOrder }}</span>
                <span class="unit">元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>销售增长率</span>
                <el-icon><TrendCharts /></el-icon>
              </div>
            </template>
            <div class="card-content warning">
              <el-progress type="circle" :percentage="mockData.growthRatePercentage" :width="120" status="warning"/>
              <div class="stat-info">
                <span class="number">{{ mockData.growthRate }}</span>
                <span class="unit">%</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">销售趋势</div>
            <div id="salesTrendChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">热销商品TOP10</div>
            <div id="topProductsChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">销售分类占比</div>
            <div id="categorySalesChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">销售地区分布</div>
            <div id="regionSalesChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Money, Document, PriceTag, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const dateRange = ref([])

// 模拟数据
const mockData = reactive({
  totalSales: 2345678,
  totalSalesPercentage: 85,
  orderCount: 1234,
  orderCountPercentage: 70,
  averageOrder: 1900,
  averageOrderPercentage: 65,
  growthRate: 23.5,
  growthRatePercentage: 23.5,
  salesTrend: [
    { date: '1月', value: 1200 },
    { date: '2月', value: 1400 },
    { date: '3月', value: 1800 },
    { date: '4月', value: 1600 },
    { date: '5月', value: 2000 },
    { date: '6月', value: 2200 }
  ],
  topProducts: [
    { name: 'iPhone 14', value: 1200 },
    { name: 'MacBook Pro', value: 980 },
    { name: 'iPad Pro', value: 780 },
    { name: 'AirPods', value: 650 },
    { name: 'Apple Watch', value: 450 }
  ],
  categorySales: [
    { value: 40, name: '手机' },
    { value: 25, name: '电脑' },
    { value: 15, name: '平板' },
    { value: 12, name: '配件' },
    { value: 8, name: '其他' }
  ],
  regionSales: [
    { name: '华东', value: 35 },
    { name: '华北', value: 25 },
    { name: '华南', value: 20 },
    { name: '西南', value: 12 },
    { name: '其他', value: 8 }
  ]
})

// 图表实例
let salesTrendChart = null
let topProductsChart = null
let categorySalesChart = null
let regionSalesChart = null

// 日期变化处理
const handleDateChange = (val) => {
  console.log('日期范围变化:', val)
  // 这里添加日期变化后的数据更新逻辑
}

// 初始化销售趋势图表
const initSalesTrendChart = () => {
  const chartDom = document.getElementById('salesTrendChart')
  salesTrendChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: mockData.salesTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value',
      name: '销售额'
    },
    series: [{
      data: mockData.salesTrend.map(item => item.value),
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.3
      }
    }]
  }
  salesTrendChart.setOption(option)
}

// 初始化热销商品图表
const initTopProductsChart = () => {
  const chartDom = document.getElementById('topProductsChart')
  topProductsChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    xAxis: {
      type: 'value',
      name: '销量'
    },
    yAxis: {
      type: 'category',
      data: mockData.topProducts.map(item => item.name)
    },
    series: [{
      type: 'bar',
      data: mockData.topProducts.map(item => item.value)
    }]
  }
  topProductsChart.setOption(option)
}

// 初始化分类销售图表
const initCategorySalesChart = () => {
  const chartDom = document.getElementById('categorySalesChart')
  categorySalesChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      type: 'pie',
      radius: '50%',
      data: mockData.categorySales
    }]
  }
  categorySalesChart.setOption(option)
}

// 初始化地区销售图表
const initRegionSalesChart = () => {
  const chartDom = document.getElementById('regionSalesChart')
  regionSalesChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
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
      data: mockData.regionSales
    }]
  }
  regionSalesChart.setOption(option)
}

onMounted(() => {
  initSalesTrendChart()
  initTopProductsChart()
  initCategorySalesChart()
  initRegionSalesChart()

  window.addEventListener('resize', () => {
    salesTrendChart?.resize()
    topProductsChart?.resize()
    categorySalesChart?.resize()
    regionSalesChart?.resize()
  })
})
</script>

<style scoped>
.sales-analysis {
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
      color: #303133;
    }

    .unit {
      font-size: 14px;
      color: #909399;
      margin-left: 4px;
    }
  }
}

.chart-card {
  .chart-title {
    font-size: 16px;
    color: #303133;
    margin-bottom: 20px;
    padding-left: 10px;
    border-left: 4px solid #409EFF;
  }

  .chart {
    height: 300px;
  }
}
</style>
