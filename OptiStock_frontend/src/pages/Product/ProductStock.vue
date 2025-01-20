<template>
  <div class="inventory-container">
    <el-card>
      <div class="header">
        <h2>库存管理</h2>
        <span class="subtitle">管理仓库库存及调整记录</span>
      </div>

      <!-- 搜索筛选 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="商品名称">
            <el-input v-model="filterForm.name" placeholder="请输入商品名称" clearable />
          </el-form-item>
          <el-form-item label="SKU">
            <el-input v-model="filterForm.sku" placeholder="请输入SKU" clearable />
          </el-form-item>
          <el-form-item label="仓库">
            <el-select v-model="filterForm.warehouse" placeholder="请选择仓库" clearable>
              <el-option label="全部" value="" />
              <el-option v-for="item in warehouseList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 库存表格 -->
      <el-table :data="filteredInventory" border>
        <el-table-column prop="productName" label="商品名称" width="200" />
        <el-table-column prop="sku" label="SKU" width="150" />
        <el-table-column prop="warehouse" label="仓库" width="180" />
        <el-table-column prop="quantity" label="库存数量" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.quantity > 50" type="success">{{ row.quantity }}</el-tag>
            <el-tag v-else-if="row.quantity > 10" type="warning">{{ row.quantity }}</el-tag>
            <el-tag v-else type="danger">{{ row.quantity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="库存状态" width="120">
          <template #default="{ row }">
            <el-tag v-if="row.status === '正常'" type="success">正常</el-tag>
            <el-tag v-else-if="row.status === '低库存'" type="warning">低库存</el-tag>
            <el-tag v-else type="danger">缺货</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button type="primary" link @click="adjustStock(row)">调整库存</el-button>
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
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 库存调整对话框 -->
    <el-dialog v-model="adjustDialogVisible" title="调整库存">
      <el-form :model="adjustForm">
        <el-form-item label="SKU">
          <el-input v-model="adjustForm.sku" disabled />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-select v-model="adjustForm.type" placeholder="选择调整类型">
            <el-option label="入库" value="in" />
            <el-option label="出库" value="out" />
          </el-select>
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="adjustForm.quantity" :min="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="adjustDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAdjustment">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// 示例仓库列表
const warehouseList = ref([
  { id: 'WH1', name: '主仓库' },
  { id: 'WH2', name: '分仓库' }
])

// 搜索表单
const filterForm = ref({ name: '', sku: '', warehouse: '' })

// 示例库存数据
const inventoryData = ref([
  { id: 1, productName: '智能手表', sku: 'SW001', warehouse: '主仓库', quantity: 50, status: '正常' },
  { id: 2, productName: '无线耳机', sku: 'WH002', warehouse: '主仓库', quantity: 80, status: '正常' },
  { id: 3, productName: '蓝牙音箱', sku: 'SP003', warehouse: '分仓库', quantity: 30, status: '低库存' }
])

// 计算筛选后的库存数据
const filteredInventory = computed(() => {
  return inventoryData.value.filter(item => {
    return (
      (!filterForm.value.name || item.productName.includes(filterForm.value.name)) &&
      (!filterForm.value.sku || item.sku.includes(filterForm.value.sku)) &&
      (!filterForm.value.warehouse || item.warehouse === filterForm.value.warehouse)
    )
  })
})

// 分页
const currentPage = ref(1)
const pageSize = ref(5)
const total = computed(() => filteredInventory.value.length)

// 处理分页
const handleSizeChange = val => { pageSize.value = val }
const handlePageChange = val => { currentPage.value = val }

// 重置搜索筛选
const resetFilter = () => { filterForm.value = { name: '', sku: '', warehouse: '' } }

// 库存调整
const adjustDialogVisible = ref(false)
const adjustForm = ref({ sku: '', type: '', quantity: 1 })

const adjustStock = row => {
  adjustForm.value.sku = row.sku
  adjustDialogVisible.value = true
}

const submitAdjustment = () => {
  ElMessage.success(`库存已调整：${adjustForm.value.sku} ${adjustForm.value.type} ${adjustForm.value.quantity}`)
  adjustDialogVisible.value = false
}
</script>

<style scoped>
.inventory-container {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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
.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
