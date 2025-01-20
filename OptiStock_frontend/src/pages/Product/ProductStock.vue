<template>
  <div class="inventory-container">
    <!-- 顶部卡片 -->
    <el-card class="main-card" shadow="hover">
      <div class="header">
        <div class="title-section">
          <h2 class="main-title">库存管理</h2>
          <span class="subtitle">管理商品库存、入库、出库及调整记录</span>
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
              <el-input 
                v-model="filterForm.sku" 
                placeholder="请输入SKU" 
                clearable
                :prefix-icon="Document"
                class="search-input"
              />
            </el-form-item>
            <el-form-item label="调整类型">
              <el-select v-model="filterForm.adjustType" placeholder="选择调整类型" clearable>
                <el-option label="入库" value="in" />
                <el-option label="出库" value="out" />
                <el-option label="损坏" value="damage" />
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
        :data="filteredInventory" 
        border
        stripe
        highlight-current-row
        class="inventory-table"
      >
        <el-table-column prop="productName" label="商品名称" min-width="180" />
        <el-table-column prop="sku" label="SKU" width="150" />
        <el-table-column prop="quantity" label="库存数量" width="150" align="center">
          <template #default="{ row }">
            <el-tag 
              :type="getStockTag(row.quantity)"
              effect="light"
              class="stock-tag"
            >
              {{ row.quantity }}
            </el-tag>
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
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Minus, Download, Search, Document, Refresh } from '@element-plus/icons-vue'

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 搜索表单
const filterForm = ref({
  name: '',
  sku: '',
  adjustType: ''
})

// 库存数据
const inventoryData = ref([
  {
    id: 1,
    productName: '智能手表',
    sku: 'SW001',
    quantity: 100,
    lastUpdate: '2025-01-15'
  },
  {
    id: 2,
    productName: '无线耳机',
    sku: 'WH002',
    quantity: 50,
    lastUpdate: '2025-01-12'
  }
])

// 过滤后的库存数据
const filteredInventory = computed(() => {
  return inventoryData.value.filter(item => {
    const nameMatch = !filterForm.value.name || item.productName.includes(filterForm.value.name)
    const skuMatch = !filterForm.value.sku || item.sku.includes(filterForm.value.sku)
    return nameMatch && skuMatch
  })
})

// 获取库存标签类型
const getStockTag = (quantity) => {
  if (quantity <= 10) return 'danger'
  if (quantity <= 50) return 'warning'
  return 'success'
}

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
</script>

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
</style>
