<template>
  <div class="order-return">
    <!-- 顶部区域 -->
    <div class="top-section">
      <div class="title-area">
        <h2>退货管理</h2>
        <el-button type="primary" @click="handleNewReturn">
          <el-icon><Plus /></el-icon>新建退货单
        </el-button>
      </div>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="退货单号">
          <el-input v-model="searchForm.returnNo" placeholder="请输入退货单号" clearable />
        </el-form-item>
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入关联订单编号" clearable />
        </el-form-item>
        <el-form-item label="退货状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待审核" value="pending" />
            <el-option label="已审核" value="approved" />
            <el-option label="已退款" value="refunded" />
            <el-option label="已拒绝" value="rejected" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-section">
      <el-table 
        :data="returnList" 
        border 
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="returnNo" label="退货单号" min-width="150" />
        <el-table-column prop="orderNo" label="关联订单号" min-width="150" />
        <el-table-column prop="customerName" label="客户名称" min-width="100" />
        <el-table-column prop="amount" label="退货金额" min-width="100">
          <template #default="scope">
            ¥{{ scope.row.amount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" min-width="150" />
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button 
              v-if="scope.row.status === 'pending'"
              link 
              type="success" 
              @click="handleApprove(scope.row)"
            >审核</el-button>
            <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination">
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
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 搜索表单数据
const searchForm = reactive({
  returnNo: '',
  orderNo: '',
  status: ''
})

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const loading = ref(false)

// 模拟退货单列表数据
const returnList = ref([
  {
    returnNo: 'RT202401010001',
    orderNo: 'OR202401010001',
    customerName: '张三',
    amount: 299.00,
    status: 'pending',
    createTime: '2024-01-01 10:00:00'
  },
  {
    returnNo: 'RT202401010002',
    orderNo: 'OR202401010002',
    customerName: '李四',
    amount: 599.00,
    status: 'approved',
    createTime: '2024-01-01 11:30:00'
  },
  {
    returnNo: 'RT202401010003',
    orderNo: 'OR202401010003',
    customerName: '王五',
    amount: 899.00,
    status: 'refunded',
    createTime: '2024-01-01 14:20:00'
  },
  {
    returnNo: 'RT202401010004',
    orderNo: 'OR202401010004',
    customerName: '赵六',
    amount: 199.00,
    status: 'rejected',
    createTime: '2024-01-01 16:45:00'
  }
])

// 状态映射
const statusMap = {
  pending: { type: 'warning', text: '待审核' },
  approved: { type: 'success', text: '已审核' },
  refunded: { type: 'primary', text: '已退款' },
  rejected: { type: 'danger', text: '已拒绝' }
}

// 获取状态样式
const getStatusType = (status) => statusMap[status]?.type || ''
const getStatusText = (status) => statusMap[status]?.text || status

// 处理搜索
const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    console.log('搜索条件：', searchForm)
  }, 500)
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => searchForm[key] = '')
  handleSearch()
}

// 新建退货单
const handleNewReturn = () => {
  ElMessage.info('新建退货单功能开发中...')
}

// 查看详情
const handleView = (row) => {
  console.log('查看详情：', row)
  ElMessage.info('查看详情功能开发中...')
}

// 审核
const handleApprove = (row) => {
  ElMessageBox.confirm('确认审核通过该退货申请？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('审核成功')
  })
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该退货单？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    ElMessage.success('删除成功')
  })
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  handleSearch()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  handleSearch()
}
</script>

<style lang="scss" scoped>
.order-return {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);

  .top-section {
    background: #fff;
    border-radius: 4px;
    padding: 20px;
    margin-bottom: 20px;

    .title-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h2 {
        margin: 0;
        font-size: 20px;
        font-weight: 500;
      }
    }

    .search-form {
      .el-form-item {
        margin-bottom: 0;
        margin-right: 20px;
      }
    }
  }

  .table-section {
    background: #fff;
    border-radius: 4px;
    padding: 20px;

    .el-tag {
      min-width: 70px;
      text-align: center;
    }

    .pagination {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
