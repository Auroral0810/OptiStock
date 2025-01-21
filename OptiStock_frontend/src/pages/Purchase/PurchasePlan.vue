<template>
  <div class="purchase-plan-container">
    <!-- 顶部标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">采购计划管理</h1>
        <div class="header-actions">
          <el-button type="primary" :icon="Plus" class="action-button" @click="createPlan">
            新建计划
          </el-button>
          <el-button type="success" :icon="Download" class="action-button" @click="exportPlan">
            导出计划
          </el-button>
        </div>
      </div>
    </div>

    <!-- 过滤查询区域 -->
    <el-card class="filter-section" shadow="hover">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="计划状态">
          <el-select v-model="filterForm.status" placeholder="选择状态" class="filter-select">
            <el-option label="草稿" value="draft"></el-option>
            <el-option label="待审核" value="pending"></el-option>
            <el-option label="已通过" value="approved"></el-option>
            <el-option label="已完成" value="completed"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="计划日期">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="date-picker"
          ></el-date-picker>
        </el-form-item>

        <el-form-item class="filter-buttons">
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 采购计划列表 -->
    <el-card class="plan-list-section" shadow="hover">
      <el-table
        :data="planList"
        border
        stripe
        class="custom-table"
        :header-cell-style="{background:'#f5f7fa',color:'#606266'}"
        v-loading="loading"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="planNumber" label="计划编号" min-width="180" />
        <el-table-column prop="planName" label="计划名称" min-width="200" />
        <el-table-column prop="totalItems" label="商品数量" width="120" align="center" />
        <el-table-column prop="estimatedAmount" label="预算金额" min-width="150">
          <template #default="{ row }">
            <span class="amount">￥{{ row.estimatedAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="计划状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="180" />
        <el-table-column label="操作" fixed="right" min-width="250">
          <template #default="{ row }">
            <el-button type="primary" link :icon="View" @click="viewPlan(row)">查看</el-button>
            <el-button type="success" link :icon="Edit" @click="editPlan(row)">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="deletePlan(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, Refresh, View, Edit, Delete } from '@element-plus/icons-vue'

// 过滤表单数据
const filterForm = ref({
  status: '',
  dateRange: []
})

// 表格数据
const planList = ref([
  {
    planNumber: 'PP202401001',
    planName: '2024年Q1电子元件采购计划',
    totalItems: 15,
    estimatedAmount: 50000,
    status: 'draft',
    createTime: '2024-01-15 10:30:00'
  },
  {
    planNumber: 'PP202401002',
    planName: '2024年Q1机械零件采购计划',
    totalItems: 20,
    estimatedAmount: 80000,
    status: 'pending',
    createTime: '2024-01-16 14:20:00'
  }
])

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const loading = ref(false)

// 获取状态标签类型
const getStatusType = (status) => {
  const statusMap = {
    draft: 'info',
    pending: 'warning',
    approved: 'success',
    completed: 'primary'
  }
  return statusMap[status] || 'info'
}

// 搜索
const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('查询成功')
  }, 1000)
}

// 重置过滤器
const resetFilter = () => {
  filterForm.value = {
    status: '',
    dateRange: []
  }
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  handleSearch()
}

// 操作处理
const createPlan = () => {
  ElMessage.success('跳转到新建计划页面')
}

const viewPlan = (row) => {
  ElMessage.success(`查看计划：${row.planNumber}`)
}

const editPlan = (row) => {
  ElMessage.success(`编辑计划：${row.planNumber}`)
}

const deletePlan = (row) => {
  ElMessageBox.confirm(
    `确定要删除计划 ${row.planNumber} 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success(`删除成功：${row.planNumber}`)
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

const exportPlan = () => {
  ElMessage.success('导出计划成功')
}
</script>

<style scoped>
.purchase-plan-container {
  padding: 20px;
  background-color: #f5f7fa;
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

.header-actions {
  display: flex;
  gap: 12px;
}

.filter-section {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.filter-select {
  width: 200px;
}

.date-picker {
  width: 360px;
}

.plan-list-section {
  background-color: #fff;
}

.custom-table {
  margin: 15px 0;
}

.amount {
  color: #67c23a;
  font-weight: bold;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.action-button {
  min-width: 100px;
}

.filter-buttons {
  margin-left: auto;
}
</style>
