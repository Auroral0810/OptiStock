<template>
  <div class="sales-analysis">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">销售分析</h1>
        <div class="header-controls">
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
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetDateRange">
            <el-icon><RefreshLeft /></el-icon>
            重置
          </el-button>
          <el-button type="success" @click="refreshCharts">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
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
              <el-progress type="dashboard" :percentage="statsData.basicStats.totalSalesPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ statsData.basicStats.totalSales }}</span>
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
              <el-progress type="circle" :percentage="statsData.basicStats.orderCountPercentage" :width="120" status="success"/>
              <div class="stat-info">
                <span class="number">{{ statsData.basicStats.orderCount }}</span>
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
              <el-progress type="dashboard" :percentage="statsData.basicStats.averageOrderPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ statsData.basicStats.averageOrder }}</span>
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
              <el-progress type="circle" :percentage="statsData.basicStats.growthRatePercentage" :width="120" status="warning"/>
              <div class="stat-info">
                <span class="number">{{ statsData.basicStats.growthRate }}</span>
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
            <div ref="salesTrendChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">热销商品TOP10</div>
            <div ref="topProductsChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">销售分类占比</div>
            <div ref="categorySalesChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">平台订单占比</div>
            <div ref="platformOrdersChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 退款分析区域 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">退款趋势分析</div>
            <div ref="refundTrendChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">退款原因分布</div>
            <div ref="refundReasonChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">退款商品类别分析</div>
            <div ref="refundCategoryChartRef" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">退款TOP10商品</div>
            <div ref="refundTopProductsChartRef" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Money, Document, PriceTag, TrendCharts, Search, RefreshLeft, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { getSalesStatistics } from '@/api/statistics'

const dateRange = ref([])

// 图表DOM引用
const salesTrendChartRef = ref(null)
const topProductsChartRef = ref(null)
const categorySalesChartRef = ref(null)
const platformOrdersChartRef = ref(null)
const refundTrendChartRef = ref(null)
const refundReasonChartRef = ref(null)
const refundCategoryChartRef = ref(null)
const refundTopProductsChartRef = ref(null)

// 统计数据
const statsData = reactive({
  basicStats: {
    totalSales: 0,
    totalSalesPercentage: 0,
    orderCount: 0,
    orderCountPercentage: 0,
    averageOrder: 0,
    averageOrderPercentage: 0,
    growthRate: 0,
    growthRatePercentage: 0
  },
  salesTrend: [],
  topProducts: [],
  categorySales: [],
  platformOrders: [],
  refundStats: {
    refundTrend: [],
    refundReasons: [],
    refundCategories: [],
    refundTopProducts: []
  }
})

// 图表实例
let salesTrendChart = null
let topProductsChart = null
let categorySalesChart = null
let platformOrdersChart = null
let refundTrendChart = null
let refundReasonChart = null
let refundCategoryChart = null
let refundTopProductsChart = null

// 日期变化处理
const handleDateChange = (val) => {
  console.log('日期范围变化:', val)
  fetchData()
}

// 搜索按钮点击事件
const handleSearch = () => {
  fetchData()
}

// 重置日期范围
const resetDateRange = () => {
  dateRange.value = []
  fetchData()
}

// 刷新图表
const refreshCharts = () => {
  fetchData()
}

// 获取后端数据
const fetchData = async () => {
  try {
    const params = {
      startDate: dateRange.value?.[0]?.toISOString().split('T')[0] || '',
      endDate: dateRange.value?.[1]?.toISOString().split('T')[0] || ''
    }
    
    const res = await getSalesStatistics(params)
    if(res.code === 200) {
      // 更新统计数据
      statsData.basicStats = res.data.basicStats
      statsData.salesTrend = res.data.salesTrend
      statsData.topProducts = res.data.topProducts
      statsData.categorySales = res.data.categorySales
      statsData.platformOrders = res.data.platformOrders
      statsData.refundStats = res.data.refundStats
      
      // 重新初始化所有图表
      initSalesTrendChart()
      initTopProductsChart()
      initCategorySalesChart()
      initPlatformOrdersChart()
      initRefundTrendChart()
      initRefundReasonChart()
      initRefundCategoryChart()
      initRefundTopProductsChart()
      
      ElMessage.success('数据加载成功')
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据出错:', error)
    ElMessage.error('获取数据出错')
  }
}

// 初始化销售趋势图表
const initSalesTrendChart = () => {
  if (!salesTrendChartRef.value) return
  salesTrendChart = echarts.init(salesTrendChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: statsData.salesTrend.map(item => item.date)
    },
    yAxis: {
      type: 'value',
      name: '销售额'
    },
    series: [{
      data: statsData.salesTrend.map(item => item.value),
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
  if (!topProductsChartRef.value) return
  topProductsChart = echarts.init(topProductsChartRef.value)
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
      data: statsData.topProducts.map(item => item.name)
    },
    series: [{
      type: 'bar',
      data: statsData.topProducts.map(item => item.value)
    }]
  }
  topProductsChart.setOption(option)
}

// 初始化分类销售图表
const initCategorySalesChart = () => {
  if (!categorySalesChartRef.value) return
  categorySalesChart = echarts.init(categorySalesChartRef.value)
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
      data: statsData.categorySales.map(item => ({
        name: item.name,
        value: item.value
      }))
    }]
  }
  categorySalesChart.setOption(option)
}

// 初始化平台订单占比图表
const initPlatformOrdersChart = () => {
  if (!platformOrdersChartRef.value) return
  platformOrdersChart = echarts.init(platformOrdersChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      name: '平台订单',
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
      data: statsData.platformOrders.map(item => ({
        name: item.name,
        value: item.value
      }))
    }]
  }
  platformOrdersChart.setOption(option)
}

// 初始化退款趋势图表
const initRefundTrendChart = () => {
  if (!refundTrendChartRef.value) return
  refundTrendChart = echarts.init(refundTrendChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['退款金额', '退款率']
    },
    xAxis: {
      type: 'category',
      data: statsData.refundStats.refundTrend.map(item => item.date)
    },
    yAxis: [
      {
        type: 'value',
        name: '退款金额',
        position: 'left'
      },
      {
        type: 'value',
        name: '退款率(%)',
        position: 'right',
        max: 100
      }
    ],
    series: [
      {
        name: '退款金额',
        type: 'bar',
        data: statsData.refundStats.refundTrend.map(item => item.value)
      },
      {
        name: '退款率',
        type: 'line',
        yAxisIndex: 1,
        data: statsData.refundStats.refundTrend.map(item => item.rate)
      }
    ]
  }
  refundTrendChart.setOption(option)
}

// 初始化退款原因图表
const initRefundReasonChart = () => {
  if (!refundReasonChartRef.value) return
  refundReasonChart = echarts.init(refundReasonChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [{
      type: 'pie',
      radius: '50%',
      data: statsData.refundStats.refundReasons.map(item => ({
        name: item.name,
        value: item.value
      })),
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  }
  refundReasonChart.setOption(option)
}

// 初始化退款商品类别分析图表
const initRefundCategoryChart = () => {
  if (!refundCategoryChartRef.value) return
  refundCategoryChart = echarts.init(refundCategoryChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['退款数量', '退款率']
    },
    xAxis: {
      type: 'category',
      data: statsData.refundStats.refundCategories.map(item => item.name)
    },
    yAxis: [
      {
        type: 'value',
        name: '退款数量'
      },
      {
        type: 'value',
        name: '退款率(%)',
        max: 100
      }
    ],
    series: [
      {
        name: '退款数量',
        type: 'bar',
        data: statsData.refundStats.refundCategories.map(item => item.refundCount)
      },
      {
        name: '退款率',
        type: 'line',
        yAxisIndex: 1,
        data: statsData.refundStats.refundCategories.map(item => item.refundRate)
      }
    ]
  }
  refundCategoryChart.setOption(option)
}

// 初始化退款TOP10商品图表
const initRefundTopProductsChart = () => {
  if (!refundTopProductsChartRef.value) return
  refundTopProductsChart = echarts.init(refundTopProductsChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['退款数量', '退款率']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    yAxis: {
      type: 'category',
      data: statsData.refundStats.refundTopProducts.map(item => item.name),
      axisLine: { show: false },
      axisTick: { show: false }
    },
    series: [
      {
        name: '退款数量',
        type: 'bar',
        data: statsData.refundStats.refundTopProducts.map(item => item.refundCount),
        label: {
          show: true,
          position: 'right'
        }
      },
      {
        name: '退款率',
        type: 'line',
        yAxisIndex: 0,
        data: statsData.refundStats.refundTopProducts.map(item => item.refundRate),
        label: {
          show: true,
          position: 'right'
        }
      }
    ]
  }
  refundTopProductsChart.setOption(option)
}

onMounted(() => {
  fetchData()

  window.addEventListener('resize', () => {
    salesTrendChart?.resize()
    topProductsChart?.resize()
    categorySalesChart?.resize()
    platformOrdersChart?.resize()
    refundTrendChart?.resize()
    refundReasonChart?.resize()
    refundCategoryChart?.resize()
    refundTopProductsChart?.resize()
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

.header-controls {
  display: flex;
  align-items: center;
  gap: 10px;
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
