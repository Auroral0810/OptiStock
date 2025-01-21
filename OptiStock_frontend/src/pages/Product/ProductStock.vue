<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Minus, Download, Search, Document, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 图表相关
const chartRef = ref(null)
const chartTimeRange = ref('week')
const selectedRows = ref([])
let stockChart = null

// 入库和出库的颜色数组 - 使用对比度更大的颜色
const inColors = ['#00FF00', '#00FFFF', '#00FF7F', '#7FFFD4', '#98FB98']
const outColors = ['#FF0000', '#FF1493', '#FF4500', '#FF69B4', '#FF8C00']

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(20)

// 搜索表单
const filterForm = ref({
  name: '',
  sku: '',
  adjustType: ''
})
// SKU选项列表
import {getSkuListAndCategoryList} from '@/api/product'
const skuOptions = ref([])
const skuOptionsList = async () => {
  const res = await getSkuListAndCategoryList()
  console.log(res)
  skuOptions.value = res.data.skuList
}
skuOptionsList()

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
  nextTick(() => {
    if(selection.length > 0) {
      updateChart()
    } else {
      if(stockChart) {
        stockChart.clear()
        stockChart.dispose()
        stockChart = null
      }
    }
  })
}

// 初始化图表
const initChart = () => {
  if (chartRef.value) {
    if(stockChart) {
      stockChart.dispose()
    }
    stockChart = echarts.init(chartRef.value)
  }
}

// 更新图表数据
const updateChart = () => {
  if(!chartRef.value) return
  
  initChart()
  
  if(!stockChart) return
  
  // 为每个选中的商品生成随机数据
  const dates = ['1-15', '1-16', '1-17', '1-18', '1-19', '1-20', '1-21']
  const series = []
  
  selectedRows.value.forEach((item, index) => {
    // 入库数据
    series.push({
      name: `${item.productName}-入库`, 
      type: 'line',
      data: dates.map(() => Math.floor(Math.random() * 50)),
      itemStyle: {
        color: inColors[index % inColors.length]
      }
    })
    // 出库数据
    series.push({
      name: `${item.productName}-出库`,
      type: 'line', 
      data: dates.map(() => Math.floor(Math.random() * 30)),
      itemStyle: {
        color: outColors[index % outColors.length]
      }
    })
  })

  const option = {
    title: {
      text: '选中商品入库/出库数量趋势'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      right: 10,
      top: 20,
      bottom: 20,
    },
    grid: {
      right: '15%'
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: series
  }
  stockChart.setOption(option)
}

// 监听时间范围变化
watch([chartTimeRange, selectedRows], () => {
  if(selectedRows.value.length > 0) {
    updateChart()
  }
})

// 组件挂载时添加窗口大小变化监听器，以便图表自适应调整大小
onMounted(() => {
  window.addEventListener('resize', () => {
    stockChart?.resize()
  })
})

// 组件卸载时移除监听器并销毁图表实例，避免内存泄漏
onUnmounted(() => {
  window.removeEventListener('resize', () => {
    stockChart?.resize()
  })
  if(stockChart) {
    stockChart.dispose()
    stockChart = null
  }
})

// 库存数据
const inventoryData = ref([])

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    name: '',
    sku: '',
    adjustType: ''
  }
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handlePageChange = (val) => {
  currentPage.value = val
}

// 库存调整相关
const adjustDialogVisible = ref(false)

// 打开调整库存对话框
const openAdjustStockDialog = (row) => {
  adjustDialogVisible.value = true
}

// 提交库存调整
const submitAdjustment = () => {
  ElMessage.success('库存调整成功')
  adjustDialogVisible.value = false
}

//获取商品数据
// const
</script>

<template>
  <div class="inventory-container">
    <!-- 顶部卡片 -->
    <el-card class="main-card" shadow="hover">
      <div class="header">
        <div class="title-section">
          <h2 class="main-title">库存管理</h2>
          <span class="subtitle">管理仓库库存、入库、出库及调整记录</span>
        </div>
        <div class="action-section">
          <el-button type="primary" :icon="Plus" class="action-btn">新增入库</el-button>
          <el-button type="warning" :icon="Minus" class="action-btn">新增出库</el-button>
          <el-button type="success" :icon="Download" class="action-btn">导出库存数据</el-button>
        </div>
      </div>

      <!-- 搜索筛选 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <div class="form-row">
            <el-form-item label="商品名称">
              <el-input 
                v-model="filterForm.name" 
                placeholder="请输入商品名称" 
                clearable
                :prefix-icon="Search"
                class="search-input"
              />
            </el-form-item>
            <el-form-item label="SKU">
              <el-select 
                v-model="filterForm.sku" 
                placeholder="请选择或输入SKU" 
                filterable 
                allow-create
                clearable
                :prefix-icon="Document"
                class="search-input"
              >
                <el-option
                  v-for="item in skuOptions"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :icon="Search" plain class="filter-btn">查询</el-button>
              <el-button :icon="Refresh" @click="resetFilter" class="filter-btn">重置</el-button>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <!-- 库存表格 -->
      <el-table 
        :data="inventoryData" 
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        class="inventory-table"
        :row-key="row => row.id"
      >
        <el-table-column type="selection" width="55" :selectable="() => true" />
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="productName" label="商品名称" min-width="180" />
        <el-table-column prop="sku" label="SKU" width="150" />
        <el-table-column prop="quantity" label="库存数量" width="150" align="center">
          <template #default="{ row }">
            <el-tag 
              effect="light"
              class="stock-tag"
            >
              {{ row.quantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalValue" label="商品总值" width="150" align="right">
          <template #default="{ row }">
            ¥{{ (row.price * row.quantity).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdate" label="最后更新时间" width="180" />
        <el-table-column label="操作" width="250" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="openAdjustStockDialog(row)">调整库存</el-button>
            <el-button type="success" link>查看记录</el-button>
            <el-button type="danger" link>删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[5, 10, 20, 50]"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          class="custom-pagination"
        />
      </div>

      <!-- 库存趋势图 -->
      <div class="chart-section">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="chart-header">
              <span>选中商品库存变动趋势</span>
              <el-radio-group v-model="chartTimeRange" size="small">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div v-if="selectedRows.length === 0" class="no-data-tip">
            请在表格中选择要查看趋势的商品
          </div>
          <div v-else class="chart-container" ref="chartRef"></div>
        </el-card>
      </div>
    </el-card>
  </div>
</template>



<style scoped>
.inventory-container {
  padding: 20px;
  background-color: #f5f7fa;
}

.header {
  display: flex;
  justify-content: space-between;
}

.search-section {
  margin-top: 20px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.inventory-table {
  margin-top: 20px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.chart-section {
  margin: 20px 0;
}

.chart-container {
  height: 400px;
  width: 100%;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.no-data-tip {
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #909399;
  font-size: 16px;
}
</style>
