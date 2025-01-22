<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Download, ArrowDown } from '@element-plus/icons-vue'

// 搜索表单数据
const searchForm = reactive({
  orderNumber: '',
  productName: '',
  returnStatus: '',
  dateRange: []
})

// 分页相关数据
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(100)
const loading = ref(false)

// 退货单列表数据
const returnList = ref([])

// 处理表单相关
const dialogVisible = ref(false)
const currentRow = ref(null)
const processForm = reactive({
  status: '',
  rejectReason: ''
})

// 状态映射
const statusMap = {
  '待处理': { type: 'warning', text: '待处理' },
  '同意': { type: 'success', text: '同意' },
  '拒绝': { type: 'danger', text: '拒绝' }
}

// 获取状态样式
const getStatusType = (status) => statusMap[status]?.type || ''
const getStatusText = (status) => statusMap[status]?.text || status

// 格式化日期
const formatDate = (date) => {
  if(!date) return ''
  return date.split(' ')[0]
}

// 重置搜索
const resetSearch = () => {
  searchForm.orderNumber = ''
  searchForm.productName = ''
  searchForm.returnStatus = ''
  searchForm.dateRange = []
  returnOrderList()
}

// 导出当前页
const exportCurrentPage = () => {
  // 检查returnList是否为数组
  if (!Array.isArray(returnList.value)) {
    ElMessage.error('退货订单数据不是数组格式')
    return
  }

  // 定义表头
  const headers = ['关联订单编号', '退货商品名称', '退货原因', '退货处理状态', '拒绝申请原因', '退货时间']

  // 生成CSV内容
  const csvContent = headers.join(',') + '\n' + 
    returnList.value.map(order => [
      order.orderNumber,
      order.productName,
      order.returnReason,
      getStatusText(order.returnStatus),
      order.rejectReason || '-',
      order.returnTime
    ].join(',')).join('\n')

  try {
    // 创建Blob对象并下载
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `退货订单列表_${new Date().toISOString().split('T')[0]}.csv`
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    URL.revokeObjectURL(url)

    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error('导出失败')
  }
}

// 打开处理表单
const openProcessDialog = (row) => {
  currentRow.value = row
  processForm.status = ''
  processForm.rejectReason = ''
  dialogVisible.value = true
}
import { handleReturnOrder,deleteReturnOrder } from '@/api/order'
// 提交处理表单
const submitProcess =() => {
  if(!processForm.status) {
    ElMessage.warning('请选择处理结果')
    return
  }
  if(processForm.status === '拒绝' && !processForm.rejectReason) {
    ElMessage.warning('请输入拒绝原因')
    return
  }
  //提示用户是否提交审核
  ElMessageBox.confirm('确认提交审核？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    //调用api实现审核
    await handleReturnOrder(currentRow.value.id, processForm.status, processForm.rejectReason)
    ElMessage.success(processForm.status === '同意' ? '已同意退货申请' : '已拒绝退货申请')
    dialogVisible.value = false;
    returnOrderList();
  })
}

// 删除
const handleDelete = (row) => {
  if(row.returnStatus !== '拒绝') {
    ElMessage.warning('只能删除已拒绝的退货申请')
    return
  }
  
  ElMessageBox.confirm('确认删除该退货单？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    // // 打印删除参数
    // console.log('删除退货订单参数:', {
    //   id: row.id
    // })
    //调用api
    await deleteReturnOrder(row.id)
    ElMessage.success('删除成功')
    returnOrderList();
  })
}

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  returnOrderList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  returnOrderList()
}

import { getReturnOrderList } from '@/api/order'
//调用API获取退货订单数据
const returnOrderList = async () => {
  try {
    loading.value = true
    const res = await getReturnOrderList(currentPage.value, pageSize.value, {
      orderNumber: searchForm.orderNumber,
      productName: searchForm.productName,
      returnStatus: searchForm.returnStatus,
      dateRange: searchForm.dateRange || []
    })
    returnList.value = res.data.rows
    total.value = res.data.total
  } catch (error) {
    console.error('获取退货订单列表失败:', error)
    ElMessage.error('获取退货订单列表失败')
  } finally {
    loading.value = false
  }
}

returnOrderList()

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 重置页码
  returnOrderList()
}
</script>

<template>
  <div class="order-return">
    <!-- 顶部区域 -->
    <div class="top-section">
      <div class="title-area">
        <h2>退货管理</h2>
        <el-button type="success" @click="exportCurrentPage">
          <el-icon><Download /></el-icon>导出当前页
        </el-button>
      </div>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单编号">
          <el-input v-model="searchForm.orderNumber" placeholder="请输入关联订单编号" clearable />
        </el-form-item>
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.productName" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="退货状态">
          <el-select v-model="searchForm.returnStatus" placeholder="请选择状态" clearable style="width: 200px;">
            <el-option label="待处理" value="待处理" />
            <el-option label="同意" value="同意" />
            <el-option label="拒绝" value="拒绝" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
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
        <el-table-column prop="orderNumber" label="关联订单编号" min-width="120" />
        <el-table-column prop="productName" label="退货商品名称" min-width="120" />
        <el-table-column prop="returnReason" label="退货原因" min-width="150" show-overflow-tooltip>
          <template #default="scope">
            <el-tooltip :content="scope.row.returnReason" placement="top" effect="light">
              <span>{{ scope.row.returnReason }}</span>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="returnStatus" label="退货处理状态" width="120" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.returnStatus)">
              {{ getStatusText(scope.row.returnStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rejectReason" label="拒绝申请原因" width="150" show-overflow-tooltip>
          <template #default="scope">
            <el-tooltip v-if="scope.row.rejectReason" :content="scope.row.rejectReason" placement="top" effect="light">
              <span>{{ scope.row.rejectReason }}</span>
            </el-tooltip>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="returnTime" label="退货时间" width="180">
          <template #default="scope">
            {{ scope.row.returnTime }}
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="150" align="center">
          <template #default="scope">
            <el-dropdown>
              <el-button type="primary" size="small">
                操作<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item 
                    v-if="scope.row.returnStatus === '待处理'"
                    @click="openProcessDialog(scope.row)"
                  >处理</el-dropdown-item>
                  <el-dropdown-item
                    v-if="scope.row.returnStatus === '拒绝'"
                    @click="handleDelete(scope.row)"
                  >删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
          layout="jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 处理表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="处理退货申请"
      width="500px"
    >
      <el-form :model="processForm" label-width="100px">
        <el-form-item label="处理结果">
          <el-radio-group v-model="processForm.status">
            <el-radio label="同意">同意</el-radio>
            <el-radio label="拒绝">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="拒绝原因" v-if="processForm.status === '拒绝'">
          <el-input
            v-model="processForm.rejectReason"
            type="textarea"
            :rows="3"
            placeholder="请输入拒绝原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitProcess">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>


<style lang="scss" scoped>
.order-return {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);

  .top-section {
    background: #fff;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);

    .title-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;
      
      h2 {
        margin: 0;
        font-size: 22px;
        font-weight: 600;
        color: #303133;
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
    border-radius: 8px;
    padding: 24px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);

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
