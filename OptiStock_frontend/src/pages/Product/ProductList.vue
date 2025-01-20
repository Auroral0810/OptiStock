<script setup>
import { ref, computed } from 'vue'
import { Plus, Edit, Delete, Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)

// 搜索表单
const filterForm = ref({
  name: '',
  categoryName: '',
  status: '',
  sku: ''
})

// 示例商品数据
const allProductData = ref([
  { 
    id: 1, 
    name: '智能手表', 
    categoryName: '智能穿戴',
    sku: 'SW001', 
    price: 299.99,
    costPrice: 199.99,
    stockQuantity: 100,
    unit: '件',
    status: '上架'
  },
  { 
    id: 2, 
    name: '无线耳机', 
    categoryName: '音频设备',
    sku: 'WH002', 
    price: 199.99,
    costPrice: 129.99,
    stockQuantity: 50,
    unit: '个',
    status: '下架'
  },
  { 
    id: 3, 
    name: '蓝牙音箱', 
    categoryName: '音频设备',
    sku: 'SP003', 
    price: 149.99,
    costPrice: 89.99,
    stockQuantity: 30,
    unit: '台',
    status: '上架'
  }
])

// 商品分类选项
const categoryOptions = [
  { label: '智能穿戴', value: '智能穿戴' },
  { label: '音频设备', value: '音频设备' },
  { label: '智能家居', value: '智能家居' }
]

// SKU选项
const skuOptions = computed(() => {
  return allProductData.value.map(item => ({
    label: item.sku,
    value: item.sku
  }))
})

// 获取状态标签类型
const getStatusType = (status) => {
  const types = { '上架': 'success', '下架': 'warning', '停产': 'danger' }
  return types[status] || 'info'
}

// 过滤数据
const filteredProducts = computed(() => {
  return allProductData.value.filter(item => {
    return (
      (!filterForm.value.name || item.name.includes(filterForm.value.name)) &&
      (!filterForm.value.categoryName || item.categoryName === filterForm.value.categoryName) &&
      (!filterForm.value.status || item.status === filterForm.value.status) &&
      (!filterForm.value.sku || item.sku.includes(filterForm.value.sku))
    )
  })
})

// 处理分页数据
const paginatedProducts = computed(() => {
  total.value = filteredProducts.value.length
  const start = (currentPage.value - 1) * pageSize.value
  return filteredProducts.value.slice(start, start + pageSize.value)
})

// 添加商品
const openAddDialog = () => {
  ElMessage.success('打开添加商品窗口')
}

// 编辑商品
const openEditDialog = (row) => {
  ElMessage.info(`编辑商品: ${row.name}`)
}

// 删除商品
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    allProductData.value = allProductData.value.filter(item => item.id !== id)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handlePageChange = (val) => {
  currentPage.value = val
}

// 导出商品
const exportData = () => {
  ElMessage.success('商品数据已导出')
}

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = { name: '', categoryName: '', status: '', sku: '' }
}
</script>

<template>
  <div class="product-container">
    <el-card class="box-card">
      <!-- 顶部标题和操作区域 -->
      <div class="header">
        <div class="title">
          <h2>商品管理</h2>
          <span class="subtitle">管理商品信息</span>
        </div>
        <div class="actions">
          <el-button type="primary" :icon="Download" @click="exportData">导出商品</el-button>
          <el-button type="primary" :icon="Plus" @click="openAddDialog">添加商品</el-button>
        </div>
      </div>
      <!-- 搜索筛选区域 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <div class="form-row">
            <el-form-item label="商品名称:">
              <el-input v-model="filterForm.name" placeholder="请输入商品名称" clearable style="width: 150px" />
            </el-form-item>
            <el-form-item label="商品分类:">
              <el-select v-model="filterForm.categoryName" placeholder="请选择分类" clearable style="width: 150px">
                <el-option
                  v-for="item in categoryOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" plain @click="resetFilter">重置筛选</el-button>
            </el-form-item>
          </div>
          <div class="form-row">
            <el-form-item label="SKU:">
              <el-autocomplete
                v-model="filterForm.sku"
                :fetch-suggestions="(queryString, cb) => {
                  const results = queryString
                    ? skuOptions.value.filter(i => i.value.toLowerCase().includes(queryString.toLowerCase()))
                    : skuOptions.value
                  cb(results)
                }"
                placeholder="请输入或选择SKU"
                clearable
                style="width: 178px"
              />
            </el-form-item>
            <el-form-item label="状态:">
              <el-select v-model="filterForm.status" placeholder="请选择状态" clearable style="width: 180px">
                <el-option label="全部" value="" />
                <el-option label="上架" value="上架" />
                <el-option label="下架" value="下架" />
                <el-option label="停产" value="停产" />
              </el-select>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <!-- 商品表格展示 -->
      <el-table :data="paginatedProducts" border class="responsive-table">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="name" label="商品名称" min-width="120" />
        <el-table-column prop="categoryName" label="商品分类" min-width="120" />
        <el-table-column prop="sku" label="SKU" width="100" />
        <el-table-column prop="price" label="售价" width="100" align="center">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="costPrice" label="成本价" width="100" align="center">
          <template #default="{ row }">¥{{ row.costPrice }}</template>
        </el-table-column>
        <el-table-column prop="stockQuantity" label="库存数量" width="85" align="center" />
        <el-table-column prop="unit" label="单位" width="55" align="center" />
        <el-table-column prop="status" label="状态" width="70" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="83" align="center">
          <template #default="{ row }">
            <el-dropdown>
              <el-button type="primary" link>
                编辑/删除<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="openEditDialog(row)">编辑</el-dropdown-item>
                  <el-dropdown-item @click="handleDelete(row.id)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
          layout="jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.product-container {
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
.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.actions {
  display: flex;
  gap: 10px;
}
</style>
