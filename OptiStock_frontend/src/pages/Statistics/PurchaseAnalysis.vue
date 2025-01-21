<template>
  <div class="purchase-analysis">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">采购分析</h1>
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
                <span>总采购金额</span>
                <el-icon><Money /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="dashboard" :percentage="mockData.totalAmountPercentage" :width="120" />
              <div class="stat-info">
                <span class="number">{{ mockData.totalAmount }}</span>
                <span class="unit">元</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>采购订单数</span>
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
                <span>供应商数量</span>
                <el-icon><User /></el-icon>
              </div>
            </template>
            <div class="card-content">
              <el-progress type="dashboard" :percentage="mockData.supplierPercentage" :width="120"/>
              <div class="stat-info">
                <span class="number">{{ mockData.supplierCount }}</span>
                <span class="unit">家</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover" class="stat-card">
            <template #header>
              <div class="card-header">
                <span>平均交付时间</span>
                <el-icon><Timer /></el-icon>
              </div>
            </template>
            <div class="card-content warning">
              <el-progress type="circle" :percentage="mockData.deliveryTimePercentage" :width="120" status="warning"/>
              <div class="stat-info">
                <span class="number">{{ mockData.averageDeliveryTime }}</span>
                <span class="unit">天</span>
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
            <div class="chart-title">月度采购趋势</div>
            <div id="trendChart" class="chart"></div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">采购品类分布</div>
            <div id="categoryChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="24">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-title">供应商采购金额TOP10</div>
            <div id="supplierChart" class="chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { Money, Document, User, Timer } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

const dateRange = ref([])

// 模拟数据
const mockData = reactive({
  totalAmount: 1234567,
  totalAmountPercentage: 85,
  orderCount: 356,
  orderCountPercentage: 70,
  supplierCount: 45,
  supplierPercentage: 60,
  averageDeliveryTime: 5.2,
  deliveryTimePercentage: 40,
  monthlyTrend: [
    { month: '1月', amount: 100000 },
    { month: '2月', amount: 150000 },
    { month: '3月', amount: 180000 },
    { month: '4月', amount: 130000 },
    { month: '5月', amount: 200000 },
    { month: '6月', amount: 250000 }
  ],
  categoryData: [
    { value: 35, name: '电子产品' },
    { value: 25, name: '办公用品' },
    { value: 20, name: '耗材' },
    { value: 15, name: '配件' },
    { value: 5, name: '其他' }
  ],
  supplierRanking: [
    { name: '供应商A', value: 500000 },
    { name: '供应商B', value: 450000 },
    { name: '供应商C', value: 400000 },
    { name: '供应商D', value: 350000 },
    { name: '供应商E', value: 300000 },
    { name: '供应商F', value: 250000 },
    { name: '供应商G', value: 200000 },
    { name: '供应商H', value: 150000 },
    { name: '供应商I', value: 100000 },
    { name: '供应商J', value: 50000 }
  ]
})

// 图表实例
let trendChart = null
let categoryChart = null
let supplierChart = null

// 初始化趋势图表
const initTrendChart = () => {
  const chartDom = document.getElementById('trendChart')
  trendChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: mockData.monthlyTrend.map(item => item.month)
    },
    yAxis: {
      type: 'value',
      name: '采购金额(元)'
    },
    series: [{
      data: mockData.monthlyTrend.map(item => item.amount),
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.3
      },
      itemStyle: {
        color: '#409EFF'
      }
    }]
  }
  trendChart.setOption(option)
}

// 初始化品类分布图表
const initCategoryChart = () => {
  const chartDom = document.getElementById('categoryChart')
  categoryChart = echarts.init(chartDom)
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [{
      name: '采购品类',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false
      },
      emphasis: {
        label: {
          show: true,
          fontSize: '16',
          fontWeight: 'bold'
        }
      },
      labelLine: {
        show: false
      },
      data: mockData.categoryData
    }]
  }
  categoryChart.setOption(option)
}

// 初始化供应商排名图表
const initSupplierChart = () => {
  const chartDom = document.getElementById('supplierChart')
  supplierChart = echarts.init(chartDom)
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
      name: '采购金额(元)'
    },
    yAxis: {
      type: 'category',
      data: mockData.supplierRanking.map(item => item.name)
    },
    series: [{
      name: '采购金额',
      type: 'bar',
      data: mockData.supplierRanking.map(item => item.value),
      itemStyle: {
        color: '#67C23A'
      }
    }]
  }
  supplierChart.setOption(option)
}

// 日期变化处理
const handleDateChange = (val) => {
  console.log('日期范围变化:', val)
  // 这里可以添加根据日期刷新数据的逻辑
}

// 页面加载时初始化图表
onMounted(() => {
  initTrendChart()
  initCategoryChart()
  initSupplierChart()

  // 监听窗口大小变化，重绘图表
  window.addEventListener('resize', () => {
    trendChart?.resize()
    categoryChart?.resize()
    supplierChart?.resize()
  })
})
</script>

<style scoped>
.purchase-analysis {
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
