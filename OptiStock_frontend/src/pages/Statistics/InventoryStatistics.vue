<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Goods, Money, Warning, TopRight, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getInventoryData } from '@/api/statistics'

// 日期范围
const dateRange = ref([])

// 统计数据
const statsData = ref({
  basicStats: {},
  lossData: [],
  stockFlow: [],
  valueDistribution: [],
  topTenProducts: [],
  bottomTenProducts: [],
  warningDistribution: [],
  categoryDistribution: []
})

// 获取统计数据
const getStats = async () => {
  try {
    const res = await getInventoryData()
    if(res.code === 200) {
      statsData.value = res.data
    }
  } catch(err) {
    console.error('获取统计数据失败:', err)
  }
}

// 刷新所有图表
const refreshCharts = async () => {
  await getStats()
  initTopTenChart()
  initBottomTenChart() 
  initWarningChart()
  initCategoryChart()
  initLossChart()
  initStockFlowChart()
  initValueDistributionChart()
}

// 日期范围变化时刷新
const handleDateRangeChange = () => {
  if (dateRange.value && dateRange.value.length === 2) {
    const params = {
      startDate: dateRange.value[0],
      endDate: dateRange.value[1]
    }
    console.log('Date range changed:', params)
    refreshCharts()
  }
}

// 图表实例
let topTenChart = null
let bottomTenChart = null
let warningChart = null
let categoryChart = null
let lossChart = null
let stockFlowChart = null
let valueDistributionChart = null

// 初始化损耗趋势图
const initLossChart = () => {
  const chartDom = document.getElementById('lossChart')
  lossChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '商品损耗率趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      formatter: '{b} <br/> 损耗率: {c}%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: statsData.value.lossData.map(item => item.date),
      boundaryGap: false
    },
    yAxis: {
      type: 'value',
      name: '损耗率(%)',
      axisLabel: {
        formatter: '{value}%'
      }
    },
    series: [{
      name: '损耗率',
      type: 'line',
      smooth: true,
      data: mockData.lossData.map(item => item.value),
      areaStyle: {
        opacity: 0.3
      },
      itemStyle: {
        color: '#F56C6C'
      }
    }]
  }
  lossChart.setOption(option)
}

// 初始化出入库趋势图
const initStockFlowChart = () => {
  const chartDom = document.getElementById('stockFlowChart')
  stockFlowChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '出入库数量趋势',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['入库', '出库'],
      top: 30
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: mockData.stockFlow.map(item => item.date)
    },
    yAxis: {
      type: 'value',
      name: '数量'
    },
    series: [
      {
        name: '入库',
        type: 'line',
        smooth: true,
        data: mockData.stockFlow.map(item => item.inbound),
        itemStyle: {
          color: '#67C23A'
        }
      },
      {
        name: '出库',
        type: 'line',
        smooth: true,
        data: mockData.stockFlow.map(item => item.outbound),
        itemStyle: {
          color: '#409EFF'
        }
      }
    ]
  }
  stockFlowChart.setOption(option)
}

// 初始化商品价值分布图
const initValueDistributionChart = () => {
  const chartDom = document.getElementById('valueDistributionChart')
  valueDistributionChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '商品价值分布',
      left: 'center'
    },
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
      type: 'category',
      data: mockData.valueDistribution.map(item => item.range),
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: '商品数量'
    },
    series: [{
      name: '商品数量',
      type: 'bar',
      data: mockData.valueDistribution.map(item => ({
        value: item.count,
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      })),
      showBackground: true,
      backgroundStyle: {
        color: 'rgba(180, 180, 180, 0.2)'
      }
    }]
  }
  valueDistributionChart.setOption(option)
}

// 初始化库存最多的十大商品图表
const initTopTenChart = () => {
  const chartDom = document.getElementById('topTenChart')
  topTenChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '库存最多的十大商品',
      left: 'center'
    },
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
      inverse: true
    },
    series: [
      {
        name: '库存数量',
        type: 'bar',
        data: mockData.topTenProducts.map(item => ({
          value: item.value,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        })),
        label: {
          show: true,
          position: 'right'
        }
      }
    ]
  }
  topTenChart.setOption(option)
}

// 初始化库存最少的十大商品图表
const initBottomTenChart = () => {
  const chartDom = document.getElementById('bottomTenChart')
  bottomTenChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '库存最少的十大商品',
      left: 'center'
    },
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
      inverse: true
    },
    series: [
      {
        name: '库存数量',
        type: 'bar',
        data: mockData.bottomTenProducts.map(item => ({
          value: item.value,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
              { offset: 0, color: '#ff9a9e' },
              { offset: 1, color: '#fad0c4' }
            ])
          }
        })),
        label: {
          show: true,
          position: 'right'
        }
      }
    ]
  }
  bottomTenChart.setOption(option)
}

// 初始化库存预警分布图表
const initWarningChart = () => {
  const chartDom = document.getElementById('warningChart')
  warningChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '库存预警分布',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle'
    },
    series: [
      {
        name: '预警状态',
        type: 'pie',
        radius: ['50%', '70%'],
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
        data: mockData.warningDistribution.map(item => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: item.name === '严重不足' ? '#F56C6C' : 
                  item.name === '库存偏低' ? '#E6A23C' : '#909399'
          }
        }))
      }
    ]
  }
  warningChart.setOption(option)
}

// 初始化库存分类占比图表
const initCategoryChart = () => {
  const chartDom = document.getElementById('categoryChart')
  categoryChart = echarts.init(chartDom)
  const option = {
    title: {
      text: '库存分类占比',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left',
      top: 'middle'
    },
    series: [
      {
        name: '商品分类',
        type: 'pie',
        radius: '50%',
        data: mockData.categoryDistribution.map(item => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#65B581' },
              { offset: 1, color: '#4FFBDF' }
            ])
          }
        })),
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

// 页面加载时初始化所有图表
onMounted(() => {
  refreshCharts()
  
  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    topTenChart?.resize()
    bottomTenChart?.resize()
    warningChart?.resize()
    categoryChart?.resize()
    lossChart?.resize()
    stockFlowChart?.resize()
    valueDistributionChart?.resize()
  })
})
</script>

<template>
  <div class="inventory-statistics">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">库存统计分析</h1>
        <div class="header-controls">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :size="'default'"
            @change="handleDateRangeChange"
            style="margin-right: 16px;"
          />
          <el-button type="primary" :icon="Refresh" @click="refreshCharts">
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
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="8">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">损耗率趋势</div>
            <div id="lossChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">出入库趋势</div>
            <div id="stockFlowChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">商品价值分布</div>
            <div id="valueDistributionChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

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

.header-controls {
  display: flex;
  align-items: center;
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

.chart {
  height: 300px;
  width: 100%;
}
</style>
