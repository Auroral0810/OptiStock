<script setup>
import { ref, computed, onMounted } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { Refresh } from '@element-plus/icons-vue'

// 示例库存数据
const stockData = ref([
  { id: 1, name: '智能手表', sku: 'SW001', stock: 15, threshold: 20 },
  { id: 2, name: '无线耳机', sku: 'WH002', stock: 8, threshold: 15 },
  { id: 3, name: '蓝牙音箱', sku: 'SP003', stock: 5, threshold: 10 },
  { id: 4, name: '智能灯泡', sku: 'LB004', stock: 50, threshold: 30 },
  { id: 5, name: 'VR 眼镜', sku: 'VR005', stock: 12, threshold: 25 }
])

// 搜索表单
const filterForm = ref({
  name: '',
  sku: '',
  status: ''
})

// 计算低库存商品
const lowStockItems = computed(() =>
  stockData.value.filter(item => item.stock < item.threshold)
)

// 过滤商品
const filteredStockData = computed(() => {
  return stockData.value.filter(item => {
    return (
      (!filterForm.value.name || item.name.includes(filterForm.value.name)) &&
      (!filterForm.value.sku || item.sku.includes(filterForm.value.sku))
    )
  })
})

// 临时存储修改的阈值
const tempThresholds = ref({})

// 初始化临时阈值
const initTempThresholds = () => {
  stockData.value.forEach(item => {
    tempThresholds.value[item.id] = item.threshold
  })
}

// 更新临时阈值
const updateTempThreshold = (id, value) => {
  tempThresholds.value[id] = value
}

// 确认更新商品阈值
const confirmUpdateThreshold = (row) => {
  const item = stockData.value.find(item => item.id === row.id)
  if (item) {
    item.threshold = tempThresholds.value[row.id]
    ElMessage.success(`${item.name}的预警阈值已更新为${item.threshold}`)
  }
}

// 渲染库存状态柱状图
const renderBarChart = () => {
  const chartDom = document.getElementById('stockBarChart')
  const myChart = echarts.init(chartDom)
  const option = {
    title: { text: '库存状态分布', left: 'center' },
    tooltip: {},
    xAxis: { type: 'category', data: stockData.value.map(item => item.name) },
    yAxis: { type: 'value' },
    series: [
      {
        name: '库存',
        type: 'bar',
        data: stockData.value.map(item => item.stock),
        color: '#409EFF'
      }
    ]
  }
  myChart.setOption(option)
}

// 渲染库存预警饼图
const renderPieChart = () => {
  const chartDom = document.getElementById('stockPieChart')
  const myChart = echarts.init(chartDom)
  const option = {
    title: { text: '库存预警占比', left: 'center' },
    tooltip: { trigger: 'item' },
    legend: { bottom: 10, left: 'center' },
    series: [
      {
        name: '库存情况',
        type: 'pie',
        radius: '50%',
        data: [
          { value: lowStockItems.value.length, name: '低库存商品', itemStyle: { color: '#f56c6c' } },
          { value: stockData.value.length - lowStockItems.value.length, name: '正常库存', itemStyle: { color: '#67C23A' } }
        ]
      }
    ]
  }
  myChart.setOption(option)
}

// 更新图表
const refreshCharts = () => {
  renderBarChart()
  renderPieChart()
  ElMessage.success('数据已刷新')
}

// 组件挂载时渲染图表和初始化临时阈值
onMounted(() => {
  refreshCharts()
  initTempThresholds()
})
</script>

<template>
  <div class="stock-warning-container">
    <el-card class="box-card">
      <!-- 头部 -->
      <div class="header">
        <div class="title">
          <h2>库存预警</h2>
          <span class="subtitle">监控低库存商品，确保库存稳定</span>
        </div>
        <el-button type="primary" :icon="Refresh" @click="refreshCharts">刷新数据</el-button>
      </div>

      <!-- 搜索筛选 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="商品名称">
            <el-input v-model="filterForm.name" placeholder="请输入商品名称" clearable />
          </el-form-item>
          <el-form-item label="SKU">
            <el-input v-model="filterForm.sku" placeholder="请输入 SKU" clearable />
          </el-form-item>
        </el-form>
      </div>

      <!-- 低库存表格 -->
      <el-table :data="lowStockItems" border>
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="sku" label="SKU" width="100" />
        <el-table-column prop="stock" label="当前库存" width="100" align="center" />
        <el-table-column label="预警阈值" width="250" align="center">
          <template #default="{ row }">
            <div class="threshold-wrapper">
              <el-input-number 
                v-model="tempThresholds[row.id]" 
                :min="5" 
                :max="100" 
                size="small"
                @change="(val) => updateTempThreshold(row.id, val)"
              />
              <el-button 
                type="primary" 
                size="small"
                @click="confirmUpdateThreshold(row)"
              >
                确认
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 图表可视化 -->
      <div class="chart-container">
        <div class="charts-wrapper">
          <div id="stockBarChart" class="chart"></div>
          <div id="stockPieChart" class="chart"></div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.stock-warning-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.title h2 {
  margin: 0;
  font-size: 24px;
}

.subtitle {
  color: #666;
  font-size: 14px;
}

.search-section {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.chart-container {
  margin-top: 20px;
}

.charts-wrapper {
  display: flex;
  gap: 20px;
}

.chart {
  flex: 1;
  height: 400px;
}

.threshold-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}
</style>
