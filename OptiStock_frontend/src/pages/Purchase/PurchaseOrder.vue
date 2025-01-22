<script setup>
import { ref,watch} from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Download, Search, Refresh, View, Check, Delete } from '@element-plus/icons-vue';
// 获取状态标签颜色
const getStatusTag = (status) => {
  switch (status) {
    case '待审核': return 'warning';
    case '已通过': return 'success';
    case '已完成': return 'info';
    case '已驳回': return 'danger';
    default: return 'default';
  }
};
// 过滤表单
const filterForm = ref({
  supplierId: '',
  status: '',
  dateRange: []
});

// 订单列表数据
const orders = ref([]);

// 订单详情数据应该是当前行的数据
const orderDetail = ref({});

import {getSupplierList} from "@/api/supplier"
// 订单详情弹窗
const dialogVisible = ref(false);
const viewOrder = async (row) => {
  // 查看详情时显示完整时间
  const fullTimeRow = {
    ...row,
    createdAt: row.fullCreatedAt || row.createdAt // 使用完整时间或原始时间
  };
  
  const filterForm = {
    name: row.supplierName,
    contact: '',
    phone: ''
  }
  
  const res = await getSupplierList(1, 1, filterForm)
  const supplierInfo = res.data.rows[0]
  
  if(supplierInfo) {
    fullTimeRow.supplierPhone = supplierInfo.phone;
    fullTimeRow.supplierEmail = supplierInfo.email;
    fullTimeRow.supplierAddress = supplierInfo.address;
    fullTimeRow.supplierContact = supplierInfo.contact;
  }
  
  orderDetail.value = fullTimeRow;
  dialogVisible.value = true;
};

// 分页信息
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(20);

// 查询订单
const fetchOrders = () => {
  PurchaseOrderList()
};

// 重置筛选条件
const resetFilter = () => {
  filterForm.value = {
    supplierId: '',
    status: '',
    dateRange: []
  };
  PurchaseOrderList()
};

// 处理每页显示数量变化
const handleSizeChange = (val) => {
  pageSize.value = val;
  PurchaseOrderList();
}

// 处理当前页码变化
const handleCurrentChange = (val) => {
  currentPage.value = val;
  PurchaseOrderList();
}

// 导出当前页订单
const exportOrders = () => {
  // 检查orders是否为数组
  if (!Array.isArray(orders.value)) {
    ElMessage.error('订单数据不是数组格式');
    return;
  }

  // 定义表头
  const headers = ['订单编号', '供应商名称', '商品名称', '采购数量', '单价', '总金额', '创建时间', '状态'];

  // 生成CSV内容
  const csvContent = headers.join(',') + '\n' + 
    orders.value.map(order => [
      order.orderId,
      order.supplierName,
      order.productName,
      order.quantity,
      order.costPrice,
      order.totalCost, 
      order.createdAt,
      order.status
    ].join(',')).join('\n');

  try {
    // 创建Blob对象并下载
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `采购订单_${new Date().toISOString().split('T')[0]}.csv`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);

    ElMessage.success('当前页订单导出成功');
  } catch (error) {
    console.error('导出订单失败:', error);
    ElMessage.error('导出订单失败');
  }
};

// 导出当前订单为Word
const exportCurrentOrder = (order) => {
  // 读取XML模板
  fetch('/src/assets/purchaseOrder.xml')
    .then(response => response.text())
    .then(template => {
      // 添加调试日志
      console.log('订单数据:', order);
      console.log('供应商名称:', order.supplierName);

      // 格式化金额,添加千分位分隔符和保留2位小数
      const formatMoney = (num) => {
        return Number(num).toLocaleString('zh-CN', {
          minimumFractionDigits: 2,
          maximumFractionDigits: 2
        });
      };

      // 格式化日期为 YYYY-MM-DD
      const formatDate = (date) => {
        return new Date(date).toLocaleDateString('zh-CN', {
          year: 'numeric',
          month: '2-digit', 
          day: '2-digit'
        }).replace(/\//g, '-');
      };

      // 替换模板中的变量
      let docContent = template
        .replace('${order.orderId}', order.orderId)
        .replace('${order.createdAt}', formatDate(order.createdAt))
        .replace('${order.supplierName}', order.supplierName || '暂无')
        .replace('${order.supplierContact}', order.supplierContact || '暂无')
        .replace('${order.supplierPhone}', order.supplierPhone || '暂无')
        .replace('${order.supplierAddress}', order.supplierAddress || '暂无')
        .replace('${order.productName}', order.productName)
        .replace('${order.quantity}', order.quantity)
        .replace('${order.costPrice}', formatMoney(order.costPrice))
        .replace('${order.totalCost}', formatMoney(order.totalCost));

      // 添加调试日志
      console.log('替换后的内容片段:', docContent.substring(0, 500));

      // 创建Blob对象
      const blob = new Blob([docContent], {
        type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
      });

      // 创建下载链接并触发下载
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = `采购订单_${order.orderId}_${formatDate(new Date())}.docx`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(link.href);

      ElMessage.success(`订单 ${order.orderId} 已导出为Word文档`);
    })
    .catch(error => {
      console.error('导出Word文档失败:', error);
      ElMessage.error('导出Word文档失败');
    });
};

import {getPurchaseOrderList,deletePurchaseOrder,addPurchaseOrder,auditPurchaseOrder,getSupplierAndProduct}from "@/api/purchase"
import {usePurchaseStore} from "@/stores/purchase"

const purchaseStore = usePurchaseStore()

// 格式化日期函数
const formatDate = (dateString, format = 'short') => {
  if (!dateString) return '';
  const date = new Date(dateString);
  if (format === 'short') {
    // 年月日格式
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  } else {
    // 完整格式，包含时分秒
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`;
  }
}

//获取订单数据
const PurchaseOrderList = async () =>{
  //查看请求体是怎么样的
  const requestData = {
    pageNum: currentPage.value,
    pageSize: pageSize.value,
    filterForm: filterForm.value
  }
  // console.log('请求数据格式:', JSON.stringify(requestData, null, 2))
  
  //调用API获取订单数据
  const res = await getPurchaseOrderList(currentPage.value,pageSize.value,filterForm.value)
  // 处理日期格式
  orders.value = res.data.rows.map(order => ({
    ...order,
    fullCreatedAt: order.createdAt, // 保存完整时间
    createdAt: formatDate(order.createdAt, 'short') // 显示年月日
  }))
  total.value = res.data.total
  //将订单数据存储到store中
  purchaseStore.setPurchaseOrderList(res.data.rows)
}
PurchaseOrderList()

// 删除订单
//逻辑：如果当前订单待审核或者已驳回，就可以删除订单，前端先要进行校验
const deleteOrder = (row) => {
  //如果当前行订单为以完成
  if(row.status!="待审核"&&row.status!="已驳回"){
    {
      ElMessage.error("当前订单状态不能删除")
      return
    }
  }
  //提示该操作不可撤销(提示语中要求说明不能撤销)
ElMessageBox.confirm('确认删除该订单吗？', '提示', {
    confirmButtonText: '确认',
    cancelButtonText: '取消',
    type: 'warning',
    message: '确认删除该订单？该操作不可撤销，请谨慎操作'
  }).then(async () => {
    //调用后端api删除已完成的订单
    const res = await deletePurchaseOrder(row.id)
    if(res.code==200){
      ElMessage.success("删除成功")
      PurchaseOrderList()
    }else{
      ElMessage.error(res.msg?res.msg:"删除失败")
    }
  }).catch(() => {
    ElMessage.info('已取消删除');
  });
};

// 审核订单
const handleAudit = (row, action) => {
  const actionText = action === 'approve' ? '通过' : '驳回';
  ElMessageBox.confirm(
    `确认${actionText}该订单吗？`,
    '审核确认',
    {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    // 这里应该调用后端API进行状态更新
    const res = await auditPurchaseOrder(row.orderId, action === 'approve' ? '已通过' : '已驳回');
    if(res.code==200){
      ElMessage.success("审核成功")
      PurchaseOrderList()
    }else{
      ElMessage.error(res.msg?res.msg:"审核失败")
    }
  }).catch(() => {
    ElMessage.info('已取消审核');
  });
};

// 新增采购订单相关数据和方法
const createDialogVisible = ref(false);
const createOrderForm = ref({
  productId: '',
  supplierId: '',
  quantity: '',
  costPrice: '',
  totalPrice: 0
});

const formRef = ref(null);

// 表单校验规则
const rules = ref({
  productId: [
    { required: true, message: '请选择商品', trigger: 'blur' }
  ],
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'blur' }
  ],
  quantity: [
    { required: true, message: '请输入采购数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '数量必须大于0', trigger: 'blur' }
  ]
});

// 供应商和商品列表
const supplierOptions = ref([]);
const productOptions = ref([]);

// 获取供应商和商品列表
const SupplierAndProductList = async () => {
  try {
    const res = await getSupplierAndProduct();
    supplierOptions.value = res.data.suppliers;
    productOptions.value = res.data.products;
  } catch (error) {
    console.error('获取供应商和商品列表失败:', error);
    ElMessage.error('获取供应商和商品列表失败');
  }
};

SupplierAndProductList()

// 监听商品选择变化,自动设置单价
watch(() => createOrderForm.value.productId, (newProductId) => {
  if(newProductId) {
    const selectedProduct = productOptions.value.find(p => p.productId === newProductId);
    if(selectedProduct) {
      createOrderForm.value.costPrice = selectedProduct.costPrice;
      calculateTotal();
    }
  }
});

// 计算总金额
const calculateTotal = () => {
  const quantity = Number(createOrderForm.value.quantity) || 0;
  const price = Number(createOrderForm.value.costPrice) || 0;
  createOrderForm.value.totalPrice = (quantity * price).toFixed(2);
};

// 监听数量的变化
watch(() => createOrderForm.value.quantity, () => {
  calculateTotal();
}, { deep: true });

// 新增采购订单
const createOrder = () => {
  createDialogVisible.value = true;
};

// 提交新增订单
const submitOrder = async () => {
  try {
    // 表单校验
    await formRef.value.validate((valid, fields) => {
      if (!valid) {
        // 校验不通过,获取第一个错误信息
        const firstField = Object.values(fields)[0];
        const errorMsg = firstField[0].message;
        ElMessage.error(errorMsg);
        throw new Error('validation failed');
      }
    });

    // 校验通过,弹出确认框
    try {
      await ElMessageBox.confirm(
        '确认要新增此采购订单吗？',
        '提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }
      );
      
      // 用户确认,调用API
      const res = await addPurchaseOrder({
        productId: createOrderForm.value.productId,
        supplierId: createOrderForm.value.supplierId,
        quantity: parseInt(createOrderForm.value.quantity),
        costPrice: parseFloat(createOrderForm.value.costPrice),
        totalPrice: parseFloat(createOrderForm.value.totalPrice)
      });

      if (res.code === 200) {
        ElMessage.success('新增订单成功');
        createDialogVisible.value = false;
        // 重置表单
        createOrderForm.value = {
          productId: '',
          supplierId: '', 
          quantity: '',
          costPrice: '',
          totalPrice: 0
        };
        // 刷新订单列表
        PurchaseOrderList();
      } else {
        ElMessage.error(res.msg || '新增订单失败');
      }
    } catch (error) {
      if (error === 'cancel') {
        ElMessage.info('已取消新增');
        return;
      }
      throw error;
    }
  } catch (error) {
    if(error.message === 'validation failed') {
      return;
    }
    console.error('新增订单失败:', error);
    ElMessage.error('新增订单失败');
  }
};
</script>

<template>
  <div class="purchase-order">
    <!-- 顶部区域 -->
    <div class="top-section">
      <div class="title-area">
        <h1 class="main-title">采购订单管理</h1>
        <div class="action-buttons">
          <el-button type="primary" :icon="Plus" class="action-button" @click="createOrder">
            新增采购订单
          </el-button>
          <el-button type="success" :icon="Download" class="action-button" @click="exportOrders">
            导出当前页
          </el-button>
        </div>
      </div>

      <!-- 搜索表单 -->
      <el-card class="filter-card" shadow="hover">
        <el-form :inline="true" :model="filterForm" class="search-form">
          <el-form-item label="供应商">
            <el-input v-model="filterForm.supplierId" placeholder="请输入供应商名称" clearable class="filter-select" />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="filterForm.status" placeholder="选择状态" clearable class="filter-select">
              <el-option label="待审核" value="待审核" />
              <el-option label="已通过" value="已通过" />
              <el-option label="已完成" value="已完成" />
              <el-option label="已驳回" value="已驳回" />
            </el-select>
          </el-form-item>
          <el-form-item label="订单日期">
            <el-date-picker
              v-model="filterForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="date-picker"
            />
          </el-form-item>
          <el-form-item class="filter-buttons">
            <el-button type="primary" :icon="Search" @click="fetchOrders">查询</el-button>
            <el-button :icon="Refresh" @click="resetFilter">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="hover">
      <el-table
        :data="orders"
        border
        style="width: 100%"
      >
        <el-table-column prop="orderId" label="订单ID" width="90" align="center" />
        <el-table-column prop="productName" label="商品名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="supplierName" label="供应商名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="quantity" label="数量" width="90" align="right" />
        <el-table-column prop="costPrice" label="单价" width="100" align="right">
          <template #default="scope">
            ¥{{ scope.row.costPrice }}
          </template>
        </el-table-column>
        <el-table-column prop="totalCost" label="总金额" width="120" align="right">
          <template #default="scope">
            ¥{{ scope.row.totalCost }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)" size="small">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="110" align="center" />
        <el-table-column label="操作" fixed="right" width="120" align="center">
          <template #default="scope">
            <el-dropdown>
              <el-button type="primary" link>
                操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="viewOrder(scope.row)">查看</el-dropdown-item>
                  <template v-if="scope.row.status === '待审核'">
                    <el-dropdown-item @click="handleAudit(scope.row, 'approve')">通过</el-dropdown-item>
                    <el-dropdown-item @click="handleAudit(scope.row, 'reject')">驳回</el-dropdown-item>
                  </template>
                  <el-dropdown-item @click="deleteOrder(scope.row)">删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>
    </el-card>

    <!-- 订单详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      title="订单详情"
      width="600px"
      destroy-on-close
    >
      <el-descriptions
        :column="2"
        border
      >
        <el-descriptions-item label="订单ID">{{ orderDetail.orderId }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ orderDetail.productName }}</el-descriptions-item>
        <el-descriptions-item label="供应商">{{ orderDetail.supplierName }}</el-descriptions-item>
        <el-descriptions-item label="联系人">{{ orderDetail.supplierContact }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ orderDetail.supplierPhone }}</el-descriptions-item>
        <el-descriptions-item label="联系邮箱">{{ orderDetail.supplierEmail }}</el-descriptions-item>
        <el-descriptions-item label="供应商地址" :span="2">{{ orderDetail.supplierAddress }}</el-descriptions-item>
        <el-descriptions-item label="采购数量">{{ orderDetail.quantity }}</el-descriptions-item>
        <el-descriptions-item label="采购单价">￥{{ orderDetail.costPrice }}</el-descriptions-item>
        <el-descriptions-item label="采购总金额">￥{{ orderDetail.totalCost }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(orderDetail.status)">{{ orderDetail.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ orderDetail.createdAt }}</el-descriptions-item>
        <el-descriptions-item v-if="orderDetail.auditComment" label="审核意见" :span="2">
          {{ orderDetail.auditComment }}
        </el-descriptions-item>
        <el-descriptions-item v-if="orderDetail.auditTime" label="审核时间" :span="2">
          {{ orderDetail.auditTime }}
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="exportCurrentOrder(orderDetail)">
            导出Word
          </el-button>
          <template v-if="orderDetail.status === '待审核'">
            <el-button type="success" @click="handleAudit(orderDetail, 'approve')">通过</el-button>
            <el-button type="danger" @click="handleAudit(orderDetail, 'reject')">驳回</el-button>
          </template>
        </span>
      </template>
    </el-dialog>

    <!-- 新增采购订单弹窗 -->
    <el-dialog
      v-model="createDialogVisible"
      title="新增采购订单"
      width="500px"
      destroy-on-close
    >
      <el-form 
        ref="formRef"
        :model="createOrderForm" 
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="商品" prop="productId">
          <el-select 
            v-model="createOrderForm.productId" 
            placeholder="请选择商品" 
            style="width: 100%"
          >
            <el-option
              v-for="item in productOptions"
              :key="item.productId"
              :label="item.productName"
              :value="item.productId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="供应商" prop="supplierId">
          <el-select 
            v-model="createOrderForm.supplierId" 
            placeholder="请选择供应商" 
            style="width: 100%"
          >
            <el-option
              v-for="item in supplierOptions"
              :key="item.supplierId"
              :label="item.supplierName"
              :value="item.supplierId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="采购数量" prop="quantity">
          <el-input-number
            v-model="createOrderForm.quantity"
            :min="1"
            style="width: 100%"
            placeholder="请输入采购数量"
          />
        </el-form-item>
        <el-form-item label="采购单价">
          <el-input
            v-model="createOrderForm.costPrice"
            disabled
            style="width: 100%"
          >
            <template #prefix>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="总金额">
          <el-input
            v-model="createOrderForm.totalPrice"
            disabled
            style="width: 100%"
          >
            <template #prefix>¥</template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitOrder">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.purchase-order {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;

  .top-section {
    margin-bottom: 24px;

    .title-area {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 24px;

      .main-title {
        margin: 0;
        font-size: 28px;
        font-weight: 600;
        color: #303133;
      }

      .action-buttons {
        display: flex;
        gap: 12px;

        .action-button {
          padding: 12px 20px;
          font-size: 14px;
        }
      }
    }
  }

  .filter-card {
    .search-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
      padding: 16px;

      .filter-select {
        width: 180px;
      }

      .date-picker {
        width: 360px;
      }

      .filter-buttons {
        margin-left: auto;
        margin-right: 0;
      }
    }
  }

  .table-card {
    margin-bottom: 24px;

    .action-group {
      display: flex;
      justify-content: center;
      gap: 8px;
    }

    .pagination {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
      padding: 16px 0 0;
    }
  }
}
</style>
