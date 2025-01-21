<template>
  <div class="purchase-stockin-container">
    <!-- 顶部标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">采购入库管理</h1>
        <div class="header-actions">
          <el-button type="primary" :icon="Plus" class="action-button" @click="createStockin">
            新建入库单
          </el-button>
          <el-button type="success" :icon="Download" class="action-button" @click="exportStockin">
            导出记录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 过滤查询区域 -->
    <el-card class="filter-section" shadow="hover">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="入库状态">
          <el-select v-model="filterForm.status" placeholder="选择状态" class="filter-select">
            <el-option label="待入库" value="pending"></el-option>
            <el-option label="已入库" value="completed"></el-option>
            <el-option label="部分入库" value="partial"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="入库日期">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="date-picker"
          ></el-date-picker>
        </el-form-item>

        <el-form-item label="关联订单">
          <el-input v-model="filterForm.orderNumber" placeholder="输入订单编号" />
        </el-form-item>

        <el-form-item class="filter-buttons">
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 入库单列表 -->
    <el-card class="stockin-list-section" shadow="hover">
      <el-table
        :data="stockinList"
        border
        stripe
        class="custom-table"
        :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="stockinNumber" label="入库单号" min-width="180" />
        <el-table-column prop="orderNumber" label="关联订单" min-width="180" />
        <el-table-column prop="totalItems" label="商品数量" width="120" align="center" />
        <el-table-column prop="status" label="入库状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="180" />
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              link 
              @click="handleStockin(row)"
              :disabled="row.status === '已入库'"
            >
              入库
            </el-button>
            <el-button type="primary" link @click="viewDetail(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 入库操作抽屉 -->
    <el-drawer
      v-model="stockinDrawer"
      title="商品入库"
      size="50%"
      :destroy-on-close="true"
    >
      <el-form :model="stockinForm" label-width="100px">
        <el-form-item label="入库单号">
          <el-input v-model="stockinForm.stockinNumber" disabled />
        </el-form-item>
        <el-form-item label="关联订单">
          <el-input v-model="stockinForm.orderNumber" disabled />
        </el-form-item>
        
        <div class="products-list">
          <h3>入库商品列表</h3>
          <el-table :data="stockinForm.products" border>
            <el-table-column prop="name" label="商品名称" min-width="150" />
            <el-table-column prop="sku" label="SKU" width="120" />
            <el-table-column prop="orderQuantity" label="订单数量" width="120" />
            <el-table-column label="入库数量" width="200">
              <template #default="{ row }">
                <el-input-number 
                  v-model="row.stockinQuantity" 
                  :min="0" 
                  :max="row.orderQuantity"
                  @change="validateStockinQuantity"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>

        <el-form-item label="备注">
          <el-input 
            v-model="stockinForm.remark" 
            type="textarea" 
            rows="3"
            placeholder="请输入入库备注信息"
          />
        </el-form-item>

        <div class="drawer-footer">
          <el-button @click="stockinDrawer = false">取消</el-button>
          <el-button type="primary" @click="submitStockin">确认入库</el-button>
        </div>
      </el-form>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, Refresh } from '@element-plus/icons-vue'

// 过滤表单
const filterForm = ref({
  status: '',
  dateRange: [],
  orderNumber: ''
})

// 入库单列表数据
const stockinList = ref([
  {
    stockinNumber: 'SI202401001',
    orderNumber: 'PO202401001',
    totalItems: 5,
    status: '待入库',
    createdAt: '2024-01-15 10:00:00'
  }
])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)

// 入库抽屉相关
const stockinDrawer = ref(false)
const stockinForm = reactive({
  stockinNumber: '',
  orderNumber: '',
  products: [],
  remark: ''
})

// 获取状态标签类型
const getStatusType = (status) => {
  const statusMap = {
    '待入库': 'warning',
    '已入库': 'success',
    '部分入库': 'info'
  }
  return statusMap[status] || 'default'
}

// 查询处理
const handleSearch = () => {
  ElMessage.success('查询成功')
}

// 重置过滤条件
const resetFilter = () => {
  filterForm.value = {
    status: '',
    dateRange: [],
    orderNumber: ''
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

// 入库操作
const handleStockin = (row) => {
  stockinForm.stockinNumber = row.stockinNumber
  stockinForm.orderNumber = row.orderNumber
  stockinForm.products = [
    {
      name: '示例商品',
      sku: 'SKU001',
      orderQuantity: 100,
      stockinQuantity: 0
    }
  ]
  stockinDrawer.value = true
}

// 查看详情
const viewDetail = (row) => {
  ElMessage.info(`查看入库单 ${row.stockinNumber} 的详情`)
}

// 验证入库数量
const validateStockinQuantity = () => {
  // 验证逻辑
}

// 提交入库
const submitStockin = () => {
  ElMessageBox.confirm('确认提交入库单？', '确认提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('入库成功')
    stockinDrawer.value = false
  })
}

// 新建入库单
const createStockin = () => {
  ElMessage.info('新建入库单')
}

// 导出记录
const exportStockin = () => {
  ElMessage.success('导出成功')
}
</script>

<style scoped>
.purchase-stockin-container {
  padding: 24px;
  background-color: #f5f7fa;
}

.page-header {
  margin-bottom: 24px;
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

.header-actions {
  display: flex;
  gap: 12px;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.stockin-list-section {
  margin-bottom: 24px;
}

.custom-table {
  margin-bottom: 16px;
}

.pagination-section {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.products-list {
  margin: 20px 0;
}

.products-list h3 {
  margin-bottom: 16px;
  color: #303133;
}

.drawer-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background: #fff;
  border-top: 1px solid #dcdfe6;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.filter-select {
  width: 200px;
}

.date-picker {
  width: 360px;
}
</style>
