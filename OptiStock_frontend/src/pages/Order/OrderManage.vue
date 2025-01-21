<template>
  <div class="order-manage">
    <div class="header">
      <h1>订单管理</h1>
      <div class="search-bar">
        <el-input
          v-model="searchKeyword"
          placeholder="请输入订单号/商品名称"
          :prefix-icon="Search"
          @keyup.enter="handleSearch"
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
      </div>
    </div>

    <div class="filter-section">
      <el-select v-model="orderStatus" placeholder="订单状态">
        <el-option label="全部" value="" />
        <el-option label="待付款" value="unpaid" />
        <el-option label="待发货" value="unshipped" />
        <el-option label="已发货" value="shipped" />
        <el-option label="已完成" value="completed" />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
      />
    </div>

    <el-table :data="orderList" border style="width: 100%">
      <el-table-column prop="orderId" label="订单号" width="180" />
      <el-table-column prop="createTime" label="下单时间" width="180" />
      <el-table-column prop="productName" label="商品名称" />
      <el-table-column prop="amount" label="金额" width="120" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button :size="small" @click="handleDetail(scope.row)">详情</el-button>
          <el-button 
            :size="small"
            type="primary" 
            v-if="scope.row.status === 'unshipped'"
            @click="handleShip(scope.row)"
          >
            发货
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const searchKeyword = ref('')
const orderStatus = ref('')
const dateRange = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const small = ref('small')

// 模拟订单数据
const orderList = ref([
  {
    orderId: 'ORD2023001',
    createTime: '2023-05-20 10:30:00',
    productName: 'iPhone 14 Pro Max',
    amount: '¥9999.00',
    status: 'unpaid'
  },
  {
    orderId: 'ORD2023002', 
    createTime: '2023-05-20 11:20:00',
    productName: 'MacBook Pro M2',
    amount: '¥12999.00',
    status: 'unshipped'
  },
  {
    orderId: 'ORD2023003',
    createTime: '2023-05-20 14:30:00', 
    productName: 'AirPods Pro',
    amount: '¥1999.00',
    status: 'shipped'
  },
  {
    orderId: 'ORD2023004',
    createTime: '2023-05-20 16:45:00',
    productName: 'iPad Air',
    amount: '¥4999.00', 
    status: 'completed'
  }
])

const fetchOrders = () => {
  // 模拟API调用
  total.value = orderList.value.length
}

const handleSearch = () => {
  currentPage.value = 1
  fetchOrders()
  ElMessage.success('搜索成功')
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchOrders()
}

const handleDetail = (row) => {
  ElMessage.info(`查看订单 ${row.orderId} 的详情`)
}

const handleShip = (row) => {
  ElMessage.success(`订单 ${row.orderId} 已发货`)
}

const getStatusType = (status) => {
  const statusMap = {
    unpaid: 'warning',
    unshipped: 'primary', 
    shipped: 'success',
    completed: 'info'
  }
  return statusMap[status]
}

const getStatusText = (status) => {
  const statusMap = {
    unpaid: '待付款',
    unshipped: '待发货',
    shipped: '已发货',
    completed: '已完成'
  }
  return statusMap[status]
}

onMounted(() => {
  fetchOrders()
})
</script>

<style lang="scss" scoped>
.order-manage {
  padding: 20px;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h1 {
      margin: 0;
      font-size: 24px;
      color: #303133;
    }

    .search-bar {
      display: flex;
      gap: 10px;

      .el-input {
        width: 300px;
      }
    }
  }

  .filter-section {
    margin-bottom: 20px;
    display: flex;
    gap: 15px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
