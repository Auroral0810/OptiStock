<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Search } from '@element-plus/icons-vue'

// 搜索表单
const filterForm = ref({
  name: '',
  sku: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0) // 总条数从后端获取

// 选中的行
const selectedRows = ref([])

// 搜索商品
const searchStockData = () => {
  currentPage.value = 1
  ThresholdDataList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.value = {
    name: '',
    sku: ''
  }
  currentPage.value = 1
  ThresholdDataList()
}

// 处理页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val
  ThresholdDataList()
}

// 处理每页条数变化
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  ThresholdDataList()
}

// 处理表格选择变化
const handleSelectionChange = async (selection) => {
  selectedRows.value = selection
  // 等待DOM更新后再渲染图表
  await nextTick()
  if(selection.length > 0) {
    renderRadarChart()
    renderBarChart() 
  }
}

// 当前页面的阈值数据
const currentPageThresholds = ref({})
// 临时存储修改的阈值
const tempThresholds = ref({})

// 更新临时阈值
const updateTempThreshold = (id, val) => {
  tempThresholds.value[id] = val
  currentPageThresholds.value[id] = val
}


// SKU选项列表
import {getSkuListAndCategoryList} from '@/api/product'
const skuOptions = ref([])
const skuOptionsList = async () => {
  const res = await getSkuListAndCategoryList()
  skuOptions.value = res.data.skuList.map(sku => ({
    value: sku,
    label: sku
  }))
}

import {getThresholdDataList,updateThreshold} from '@/api/stock'
//获取阈值数据
const thresholdData = ref([])
//调用api获取阈值数据,传入的是搜索条件和分页条件
const ThresholdDataList = async () => {
  try {
    const res = await getThresholdDataList(currentPage.value, pageSize.value, filterForm.value)
    if(res.data) {
      thresholdData.value = res.data.rows || []
      total.value = res.data.total || 0
      // 更新当前页面的阈值数据
      thresholdData.value.forEach(item => {
        currentPageThresholds.value[item.id] = item.warningThreshold
      })
      console.log(currentPageThresholds.value)
    }
  } catch(error) {
    ElMessage.error('获取数据失败')
    thresholdData.value = []
    total.value = 0
  }
}
// 渲染库存趋势雷达图
const renderRadarChart = () => {
  const chartDom = document.getElementById('stockRadarChart')
  if(!chartDom) return
  const myChart = echarts.init(chartDom)
  
  const data = selectedRows.value.map(item => ({
    name: item.name,
    value: [
      item.stockQuantity,
      currentPageThresholds.value[item.id] || item.warningThreshold,
      ((item.stockQuantity / (currentPageThresholds.value[item.id] || item.warningThreshold)) * 100).toFixed(0)
    ]
  }))

  const option = {
    title: {
      text: '库存预警分析',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    legend: {
      type: 'scroll',
      bottom: 10,
      data: data.map(item => item.name)
    },
    radar: {
      indicator: [
        { name: '当前库存', max: Math.max(...data.map(item => item.value[0])) || 100 },
        { name: '预警阈值', max: Math.max(...data.map(item => item.value[1])) || 100 },
        { name: '库存水平(%)', max: 100 }
      ],
      shape: 'circle',
      splitNumber: 5,
      axisName: {
        color: '#666'
      }
    },
    series: [{
      type: 'radar',
      data: data,
      symbol: 'none',
      areaStyle: {
        opacity: 0.3
      }
    }]
  }
  
  myChart.setOption(option)
}

// 渲染柱状图
const renderBarChart = () => {
  const chartDom = document.getElementById('stockBarChart')
  if(!chartDom) return
  const myChart = echarts.init(chartDom)
  
  const colors = ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de']
  const data = selectedRows.value.map((item, index) => ({
    name: item.name,
    stock: item.stockQuantity,
    threshold: currentPageThresholds.value[item.id] || item.warningThreshold,
    color: colors[index % colors.length]
  }))

  const option = {
    title: {
      text: '库存与阈值对比',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['当前库存', '预警阈值'],
      bottom: 10
    },
    xAxis: {
      type: 'category',
      data: data.map(item => item.name)
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '当前库存',
        type: 'bar',
        data: data.map((item, index) => ({
          value: item.stock,
          itemStyle: {
            color: item.color
          }
        }))
      },
      {
        name: '预警阈值',
        type: 'line',
        lineStyle: {
          type: 'dashed'
        },
        data: data.map((item, index) => ({
          value: item.threshold,
          itemStyle: {
            color: item.color
          }
        }))
      }
    ]
  }
  
  myChart.setOption(option)
}

// 更新图表
const refreshCharts = async () => {
  if(selectedRows.value.length > 0) {
    await nextTick()
    renderRadarChart()
    renderBarChart()
  }
}

// 组件挂载时初始化临时阈值和获取SKU选项
onMounted(async () => {
  await skuOptionsList()
  await ThresholdDataList()
})
// 确认更新商品阈值
const confirmUpdateThreshold = (row) => {
  // console.log(tempThresholds.value,row)
  const newThreshold = tempThresholds.value[row.id] || row.warningThreshold
  ElMessageBox.confirm(
    `是否将商品 "${row.name}" 的预警阈值更新为 ${newThreshold}？`,
    '更新阈值',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    // currentPageThresholds.value[row.id] = newThreshold
    //调用api更新阈值
    const res = await updateThreshold(row.id,newThreshold)
    ElMessage.success(`${row.name}的预警阈值已更新为${newThreshold}`)
    //设置对应id的阈值为新的值
    currentPageThresholds.value[row.id] = newThreshold
    refreshCharts()
  }).catch(() => {
    tempThresholds.value[row.id] = row.warningThreshold
    currentPageThresholds.value[row.id] = row.warningThreshold
  })
}

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
        <el-button type="primary" :icon="Refresh" @click="refreshCharts;ElMessage.success('数据已刷新')">刷新数据</el-button>
      </div>

      <!-- 搜索筛选 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm">
          <el-form-item label="商品名称">
            <el-input v-model="filterForm.name" placeholder="请输入商品名称" clearable />
          </el-form-item>
          <el-form-item label="SKU">
            <el-select
              v-model="filterForm.sku"
              placeholder="请选择或输入SKU"
              filterable
              allow-create
              clearable
            >
              <el-option
                v-for="item in skuOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="searchStockData">查询</el-button>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 库存阈值表格 -->
      <el-table 
        :data="thresholdData" 
        border
        @selection-change="handleSelectionChange"
        v-loading="!thresholdData.length"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="sku" label="SKU" width="100" />
        <el-table-column prop="stockQuantity" label="当前库存" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.stockQuantity < currentPageThresholds[row.id] ? 'danger' : 'success'">
              {{ row.stockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="预警阈值" width="250" align="center">
          <template #default="{ row }">
            <div class="threshold-wrapper">
              <el-input-number 
                v-model="currentPageThresholds[row.id]" 
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

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="total"
          layout="jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 图表可视化 -->
      <div v-if="selectedRows.length > 0" class="chart-container">
        <div class="charts-wrapper">
          <div id="stockBarChart" class="chart"></div>
          <div id="stockRadarChart" class="chart"></div>
        </div>
      </div>
      <div v-else class="no-data-tip">
        请选择要查看的商品数据
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
  height: 500px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  padding: 20px;
}

.threshold-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.no-data-tip {
  text-align: center;
  color: #909399;
  font-size: 14px;
  margin-top: 20px;
}
</style>
