<script setup>
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Minus, Download, Search, Document, Refresh } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

// 图表相关
const chartRef = ref(null)
const chartTimeRange = ref('week')
const selectedRows = ref([])
let stockChart = null

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(20)

// 搜索表单
const filterForm = ref({
  name: '',
  sku: '',
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
const updateChart = async () => {
  if(!chartRef.value || selectedRows.value.length === 0) return
  
  initChart()
  
  if(!stockChart) return

  // 获取选中行的库存记录数据
  const recordData = []
  for(const row of selectedRows.value) {
    const res = await getStockAdjustRecord(row.id, chartTimeRange.value)
    recordData.push({
      name: row.name,
      data: res.data
    })
  }

  // 处理数据，按时间排序并提取数据点
  const series = recordData.map(item => {
    const sortedData = item.data.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt))
    return {
      name: item.name,
      type: 'line',
      data: sortedData.map(record => [
        record.createdAt,
        record.quantity
      ])
    }
  })

  const option = {
    title: {
      text: '选中商品库存调整记录'
    },
    tooltip: {
      trigger: 'axis',
      formatter: function(params) {
        const time = params[0].data[0]
        let result = `${time}<br/>`
        params.forEach(param => {
          result += `${param.seriesName}: ${Math.abs(param.data[1])}件<br/>`
        })
        return result
      }
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
      type: 'time',
      axisLabel: {
        formatter: '{MM}-{dd}'
      }
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: function(value) {
          return Math.abs(value)
        }
      }
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
  }
  StockList()
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  StockList()
}

const handlePageChange = (val) => {
  currentPage.value = val
  StockList()
}

import {getStockList,adjustStock,getStockAdjustRecord} from '@/api/stock'
import {useStockStore} from '@/stores/stock'
const stockStore = useStockStore()
//获取库存数据
const StockList = async () => {
  const res = await getStockList(currentPage.value, pageSize.value, filterForm.value)
  console.log(res)
  inventoryData.value = res.data.rows
  console.log(inventoryData.value)
  total.value = res.data.total
  //存储到store中
  stockStore.setStockList(inventoryData.value)
}
StockList()
//导出数据,导出成csv文件，数据存储在inventoryData中
//要求先设置表头
const exportStockData = () => {
  const headers = ['商品名称', 'SKU', '库存数量', '商品总值', '最后更新时间']
  const csvContent = headers.join(',') + '\n' + inventoryData.value.map(item => `${item.name},${item.sku},${item.stockQuantity},${item.totalValue},${item.updatedAt}`).join('\n')
  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = 'stock.csv'
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
}

// 调整库存相关数据
const drawerVisible = ref(false)
const adjustForm = ref({
  productId: '',
  name: '',
  sku: '',
  currentStock: 0,
  quantity: 0,
  adjustmentType: '',
  remarks: ''
})

// 表单校验规则
const rules = ref({
  adjustmentType: [
    { required: true, message: '请选择调整类型', trigger: 'blur' }
  ],
  quantity: [
    { required: true, message: '请输入调整数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '调整数量必须大于0', trigger: 'blur' },
    { validator: (rule, value, callback) => {
      if (value > adjustForm.value.currentStock) {
        callback(new Error('调整数量不能大于当前库存'))
      } else {
        callback()
      }
    }, trigger: 'blur' }
  ],
  remarks: [
    { required: true, message: '请输入调整备注', trigger: 'blur' },
    { min: 2, max: 100, message: '备注长度在2-100个字符之间', trigger: 'blur' }
  ]
})

// 调整类型选项
const adjustTypeOptions = [
  { label: '过期', value: '过期' },
  { label: '损坏', value: '损坏' },
  { label: '其他', value: '其他' }
]

// 打开调整库存抽屉
const openAdjustDrawer = (row) => {
  if(row.stockQuantity === 0) {
    ElMessage.warning('库存为0，无法调整')
    return
  }
  adjustForm.value = {
    productId: row.id,
    name: row.name,
    sku: row.sku,
    currentStock: row.stockQuantity,
    quantity: 0,
    adjustmentType: '',
    remarks: ''
  }
  drawerVisible.value = true
}

const adjustFormRef = ref(null)


// 提交库存调整
const submitStockAdjust = async () => {
  await adjustFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 弹出确认框
        await ElMessageBox.confirm(
          `确认要调整 ${adjustForm.value.name} 的库存吗？调整数量: ${adjustForm.value.quantity}`,
          '确认调整',
          {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )

        await adjustStock(adjustForm.value)
        ElMessage.success('库存调整成功!')
        drawerVisible.value = false
        StockList() // 刷新列表
      } catch(error) {
        if (error === 'cancel') {
          return
        }
        ElMessage.error('库存调整失败')
      }
    }
  })
}

// 库存记录相关数据
const recordVisible = ref(false) // 记录表格是否显示
const recordTableData = ref([]) // 记录表格数据
const recordTimeRange = ref('week') // 时间范围选择
const recordDialogTitle = ref('') // 弹窗标题
const currentProductId = ref(null) // 当前查看记录的商品ID

// 打开记录弹窗
const openRecordDialog = async (row) => {
  recordDialogTitle.value = `${row.name} - 库存调整记录`
  recordVisible.value = true
  currentProductId.value = row.id
  await fetchRecordData()
}

// 获取记录数据
const fetchRecordData = async () => {
  if (!currentProductId.value) return
  const res = await getStockAdjustRecord(currentProductId.value, recordTimeRange.value)
  recordTableData.value = res.data
}

// 监听时间范围变化
watch(recordTimeRange, async () => {
  await fetchRecordData()
})
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
          <el-button type="success" :icon="Download" class="action-btn" @click="exportStockData">导出当前页</el-button>
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
              <el-button type="primary" :icon="Search" plain class="filter-btn" @click="StockList()">查询</el-button>
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
        <el-table-column prop="name" label="商品名称" min-width="180" />
        <el-table-column prop="sku" label="SKU" width="150" />
        <el-table-column prop="stockQuantity" label="库存数量" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              effect="light"
              class="stock-tag"
            >
              {{ row.stockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="totalValue" label="商品总值" width="150" align="right">
          <template #default="{ row }">
            <el-tag 
              type="success"
              effect="dark"
              class="value-tag"
            >
              ¥{{ row.totalValue }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="最后更新时间" width="180" />
        <el-table-column label="操作" width="250" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="openAdjustDrawer(row)">调整库存</el-button>
            <el-button type="success" link @click="openRecordDialog(row)">查看记录</el-button>
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
          layout="jumper,total, sizes, prev, pager, next"
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
              <span>选中商品库存调整记录</span>
              <el-radio-group v-model="chartTimeRange" size="small">
                <el-radio-button label="week">近7天</el-radio-button>
                <el-radio-button label="month">近30天</el-radio-button>
                <el-radio-button label="year">近1年</el-radio-button>
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

    <!-- 调整库存抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      title="调整库存"
      size="30%"
      :destroy-on-close="true"
    >
      <el-form 
        ref="adjustFormRef"
        :model="adjustForm" 
        :rules="rules"
        label-width="100px"
        @validate="$event"
      >
        <el-form-item label="商品名称">
          <el-input v-model="adjustForm.name" disabled />
        </el-form-item>
        <el-form-item label="SKU">
          <el-input v-model="adjustForm.sku" disabled />
        </el-form-item>
        <el-form-item label="当前库存">
          <el-input v-model="adjustForm.currentStock" disabled>
            <template #append>件</template>
          </el-input>
        </el-form-item>
        <el-form-item label="调整类型" prop="adjustmentType">
          <el-select 
            v-model="adjustForm.adjustmentType" 
            placeholder="请选择调整类型"
            @blur="$refs.adjustFormRef.validateField('adjustmentType')"
          >
            <el-option
              v-for="item in adjustTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="调整数量" prop="quantity">
          <el-input-number 
            v-model="adjustForm.quantity"
            :min="1"
            :max="adjustForm.currentStock"
            placeholder="输入调整数量"
            @blur="$refs.adjustFormRef.validateField('quantity')"
          >
          </el-input-number>
          <div class="adjust-tip">不可用库存数量</div>
        </el-form-item>
        <el-form-item label="调整备注" prop="remarks">
          <el-input 
            v-model="adjustForm.remarks"
            type="textarea"
            rows="3"
            placeholder="请输入调整原因"
            @blur="$refs.adjustFormRef.validateField('remarks')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="submitStockAdjust">确认调整</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 库存记录弹窗 -->
    <el-dialog
      v-model="recordVisible"
      :title="recordDialogTitle"
      width="60%"
      :destroy-on-close="true"
    >
      <div class="record-header">
        <el-radio-group v-model="recordTimeRange" size="small">
          <el-radio-button label="week">近7天</el-radio-button>
          <el-radio-button label="month">近30天</el-radio-button>
          <el-radio-button label="year">近1年</el-radio-button>
        </el-radio-group>
      </div>

      <el-table :data="recordTableData" border stripe class="record-table">
        <el-table-column prop="id" label="记录ID" width="100" />
        <el-table-column prop="adjustmentType" label="调整类型" width="120" />
        <el-table-column prop="quantity" label="调整数量" width="120">
          <template #default="{ row }">
            <el-tag type="danger">-{{ row.quantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="调整备注" min-width="200" />
        <el-table-column prop="createdAt" label="调整时间" width="180" />
      </el-table>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="recordVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
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

.record-header {
  margin-bottom: 20px;
}

.record-table {
  margin: 20px 0;
}
</style>
