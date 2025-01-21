<template>
  <div class="purchase-order">
    <!-- 顶部区域 -->
    <div class="top-section">
      <div class="title-area">
        <h2>采购订单管理</h2>
        <el-button type="primary" @click="exportOrders">
          <el-icon><Download /></el-icon>导出当前页订单
        </el-button>
      </div>

      <!-- 搜索表单 -->
      <el-form :inline="true" :model="filterForm" class="search-form">
        <el-form-item label="供应商">
          <el-select v-model="filterForm.supplierId" placeholder="选择供应商" clearable>
            <el-option label="北京电子" :value="1" />
            <el-option label="上海机械" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="filterForm.status" placeholder="选择状态" clearable>
            <el-option label="待审核" value="待审核" />
            <el-option label="已通过" value="已通过" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="订单日期">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchOrders">查询</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 表格区域 -->
    <div class="table-section">
      <el-table
        :data="orders"
        border
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="80" />
        <el-table-column prop="orderNumber" label="订单编号" min-width="180" />
        <el-table-column prop="supplier" label="供应商" min-width="150" />
        <el-table-column prop="totalAmount" label="总金额" min-width="150">
          <template #default="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="订单状态" min-width="120">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" min-width="180" />
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="viewOrder(scope.row)">查看</el-button>
            <el-button 
              link 
              type="warning" 
              :disabled="scope.row.status !== '待审核'"
              @click="handleAudit(scope.row)"
            >审核</el-button>
            <el-button link type="danger" @click="deleteOrder(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchOrders"
          @current-change="fetchOrders"
          background
        />
      </div>
    </div>

    <!-- 订单详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="订单详情"
      width="800px"
      destroy-on-close
    >
      <el-descriptions
        title="订单基本信息"
        :column="2"
        border
      >
        <el-descriptions-item label="订单编号">{{ orderDetail.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ orderDetail.supplier }}</el-descriptions-item>
        <el-descriptions-item label="总金额">￥{{ orderDetail.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(orderDetail.status)">{{ orderDetail.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ orderDetail.createdAt }}</el-descriptions-item>
      </el-descriptions>

      <div class="detail-items">
        <h3>订单商品明细</h3>
        <el-table
          :data="orderDetail.items"
          border
        >
          <el-table-column prop="productName" label="商品名称" min-width="200" />
          <el-table-column prop="quantity" label="数量" width="120" />
          <el-table-column prop="price" label="单价" width="150">
            <template #default="scope">￥{{ scope.row.price }}</template>
          </el-table-column>
          <el-table-column prop="totalPrice" label="总价" width="150">
            <template #default="scope">￥{{ scope.row.totalPrice }}</template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Download, Search, Refresh, View, Check, Delete } from '@element-plus/icons-vue';

// 过滤表单
const filterForm = ref({
  supplierId: '',
  status: '',
  dateRange: []
});

// 订单列表示例数据
const orders = ref([
  { orderNumber: 'PO202401001', supplier: '北京电子', totalAmount: 5000, status: '待审核', createdAt: '2025-01-10' },
  { orderNumber: 'PO202401002', supplier: '上海机械', totalAmount: 12000, status: '已通过', createdAt: '2025-01-08' }
]);

// 订单详情示例数据
const orderDetail = ref({
  orderNumber: 'PO202401001',
  supplier: '北京电子',
  totalAmount: 5000,
  status: '待审核',
  createdAt: '2025-01-10',
  items: [
    { productName: '电机', quantity: 10, price: 500, totalPrice: 5000 }
  ]
});

// 分页信息
const pagination = ref({
  page: 1,
  pageSize: 10,
  total: 2
});

// 查询订单
const fetchOrders = () => {
  ElMessage.success('订单查询成功');
};

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    supplierId: '',
    status: '',
    dateRange: []
  };
};

// 获取状态标签颜色
const getStatusTag = (status) => {
  switch (status) {
    case '待审核': return 'warning';
    case '已通过': return 'success';
    case '已完成': return 'info';
    default: return 'default';
  }
};

// 订单详情弹窗
const dialogVisible = ref(false);
const viewOrder = (row) => {
  orderDetail.value = row;
  dialogVisible.value = true;
};

// 导出订单
const exportOrders = () => {
  ElMessage.success('当前页订单导出成功');
};

// 删除订单
const deleteOrder = (id) => {
  ElMessage.warning(`订单 ${id} 已删除`);
};

// 审核订单
const handleAudit = (row) => {
  ElMessageBox.confirm('确认审核通过该订单吗？', '审核确认', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = '已通过';
    ElMessage.success(`订单 ${row.orderNumber} 审核通过`);
  }).catch(() => {
    ElMessage.info('已取消审核');
  });
};
</script>

<style lang="scss" scoped>
.purchase-order {
  padding: 20px;

  .top-section {
    margin-bottom: 20px;

    .title-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h2 {
        margin: 0;
        font-size: 24px;
      }
    }
  }

  .table-section {
    .pagination {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }

  .detail-items {
    margin-top: 20px;

    h3 {
      margin-bottom: 15px;
    }
  }
}
</style>
