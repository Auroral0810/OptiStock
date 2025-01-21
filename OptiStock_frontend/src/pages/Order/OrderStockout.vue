<template>
  <div class="stockout-container">
    <div class="header">
      <h1>出库记录</h1>
      <div class="search-bar">
        <el-input
          v-model="searchQuery"
          placeholder="请输入出库单号/仓库名称"
          :prefix-icon="Search"
          clearable
        />
        <el-button type="primary" @click="handleSearch">搜索</el-button>
        <el-button type="success" @click="handleAdd">新增出库</el-button>
      </div>
    </div>

    <el-table
      :data="tableData"
      border
      stripe
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="id" label="出库单号" width="180" />
      <el-table-column prop="date" label="出库日期" width="180" />
      <el-table-column prop="warehouse" label="仓库" />
      <el-table-column prop="operator" label="操作人" />
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
            {{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button link type="primary" @click="handleView(scope.row)">查看</el-button>
          <el-button
            link
            type="primary"
            @click="handleEdit(scope.row)"
          >编辑</el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const searchQuery = ref('')
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 模拟数据
const tableData = ref([
  {
    id: 'CK20230601001',
    date: '2023-06-01',
    warehouse: '主仓库',
    operator: '张三',
    status: '已完成'
  },
  {
    id: 'CK20230601002', 
    date: '2023-06-01',
    warehouse: '分仓库A',
    operator: '李四',
    status: '处理中'
  },
  {
    id: 'CK20230601003',
    date: '2023-06-01', 
    warehouse: '分仓库B',
    operator: '王五',
    status: '已完成'
  },
  {
    id: 'CK20230601004',
    date: '2023-06-01',
    warehouse: '主仓库',
    operator: '赵六',
    status: '处理中'
  }
])

const fetchData = () => {
  loading.value = true
  // 模拟接口请求
  setTimeout(() => {
    total.value = 100
    loading.value = false
  }, 500)
}

const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

const handleAdd = () => {
  ElMessage.success('点击了新增按钮')
}

const handleView = (row) => {
  ElMessage.info(`查看记录：${row.id}`)
}

const handleEdit = (row) => {
  ElMessage.info(`编辑记录：${row.id}`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该记录吗？', '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success(`删除成功：${row.id}`)
  }).catch(() => {})
}

const handleSizeChange = (val) => {
  pageSize.value = val
  fetchData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchData()
}

// 初始化
fetchData()
</script>

<style lang="scss" scoped>
.stockout-container {
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

  .el-table {
    margin-bottom: 20px;
  }

  .pagination {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
