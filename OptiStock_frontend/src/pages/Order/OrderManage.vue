<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, View, Edit, Delete, Van, ArrowDown } from '@element-plus/icons-vue'
import { getOrderList, deleteOrder, addOrder, updateOrder,changeOrderStatus,getProductName} from '@/api/order'
import { useOrderStore } from '@/stores/order'

// store
const orderStore = useOrderStore()

// 数据
const loading = ref(false)
const orderList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const orderDetailVisible = ref(false)
const orderFormVisible = ref(false)
const dialogType = ref('add')
const selectedOrder = ref({})
const platforms = ref(['淘宝', '天猫', '抖音', '快手', '京东', '苏宁', '得物', '拼多多'])
const productOptions = ref([])

// 临时存储新增订单表单信息
const tempOrderForm = ref(null)

// 搜索表单
const searchForm = reactive({
  keyword: '',
  status: '',
  platform: '',
  dateRange: []
})

// 订单表单
const orderFormRef = ref(null)
const orderForm = reactive({
  productId: '',
  productName: '',
  price: 0,
  quantity: 1,
  totalPrice: 0,
  customerName: '',
  platform: '',
  phone: '',
  address: '',
  stockQuantity: 0
})

// 表单校验规则
const orderFormRules = {
  productId: [{ required: true, message: '请选择商品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }],
  customerName: [{ required: true, message: '请输入客户名称', trigger: 'blur' }],
  platform: [{ required: true, message: '请选择平台', trigger: 'change' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

// 获取订单列表数据
const getOrderData = async () => {
  loading.value = true
  try {
    const res = await getOrderList(currentPage.value, pageSize.value, searchForm)
    if(res.code === 200) {
      orderStore.setOrderList(res.data.rows)
      orderList.value = res.data.rows
      total.value = res.data.total
    }
  } catch(error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 方法
const handleSearch = () => {
  currentPage.value = 1
  getOrderData()
}

const resetSearch = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  searchForm.platform = ''
  searchForm.dateRange = []
  handleSearch()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getOrderData()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  getOrderData()
}

const handleDetail = (row) => {
  selectedOrder.value = row
  orderDetailVisible.value = true
}

const handleAddOrder = () => {
  dialogType.value = 'add'
  //如果存在临时保存的表单数据,则使用临时数据
  if(tempOrderForm.value) {
    Object.assign(orderForm, tempOrderForm.value)
  } else {
    //清空表单中的数据
    Object.assign(orderForm, {
      productId: '',
      productName: '',
      price: 0,
      quantity: 1,
      totalPrice: 0,
      customerName: '',
      platform: '',
      phone: '',
      address: '',
      stockQuantity: 0
    })
  }
  orderFormVisible.value = true
  // 获取商品列表
  productNameList()
}

const handleEdit = async (row) => {
  dialogType.value = 'edit'
  selectedOrder.value = row // 保存当前编辑的订单
  Object.assign(orderForm, row)
  orderFormVisible.value = true
  // 获取商品列表
  await productNameList()
  
  // 找到对应的商品选项
  const product = productOptions.value.find(p => p.productName === row.productName)
  
  if(product) {
    Object.assign(orderForm, {
      ...row,
      productId: product.productId,
      quantity: row.quantity, // 保持原数量不变
      productName: row.productName, // 保持原商品名称不变
      stockQuantity: product.stockQuantity // 设置对应商品的库存
    })
  }
  
  orderFormVisible.value = true
}

// 临时保存新增订单表单
const saveTempOrder = () => {
  tempOrderForm.value = {...orderForm}
  orderFormVisible.value = false
  ElMessage.success('表单已临时保存')
}

const handleDelete = async (row) => {
  //判断状态，只有待付款、已取消的订单可以被删除，如果不是请提醒用户
  if(row.status !== '待付款'&& row.status !== '已取消') {
    ElMessage.error('只有待付款、已取消的订单可以被删除')
    return
  }
  try {
    await deleteOrder(row.orderNumber)
    ElMessage.success('删除成功')
    getOrderData()
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

const handleProductChange = () => {
  // 编辑模式下不允许修改商品
  if(dialogType.value === 'edit') {
    return
  }
  
  const product = productOptions.value.find(p => p.productId === orderForm.productId)
  if(product) {
    orderForm.price = product.price
    orderForm.productName = product.productName
    orderForm.stockQuantity = product.stockQuantity
    // Reset quantity to 1 when product changes
    orderForm.quantity = 1
  } else {
    orderForm.price = 0
    orderForm.productName = ''
    orderForm.stockQuantity = 0
    orderForm.quantity = 1
  }
  calculateTotal()
}

const calculateTotal = () => {
  orderForm.totalPrice = orderForm.price * orderForm.quantity
}

const generateOrderId = () => {
  const now = new Date()
  const dateStr = now.getFullYear() +
    String(now.getMonth() + 1).padStart(2, '0') +
    String(now.getDate()).padStart(2, '0') +
    String(now.getHours()).padStart(2, '0') +
    String(now.getMinutes()).padStart(2, '0')
  const phone = orderForm.phone.slice(-4)
  return `OPT${dateStr}${orderForm.productId}${phone}${orderForm.quantity}`
}
const submitOrder = async () => {
  if (!orderFormRef.value) return
  
  await orderFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        let submitData = {}
        
        if (dialogType.value === 'add') {
          submitData = {
            ...orderForm,
            orderId: generateOrderId()
          }
        } else {
          // 编辑模式下只提交部分字段
          submitData = {
            customerName: orderForm.customerName,
            phone: orderForm.phone, 
            address: orderForm.address,
            orderNumber: selectedOrder.value.orderNumber // 使用保存的订单信息
          }
        }

        // 根据操作类型显示不同的确认提示
        const confirmMessage = dialogType.value === 'add' ? '是否确认新增订单?' : '是否确认更新订单?'
        
        const result = await ElMessageBox.confirm(confirmMessage, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        if (result === 'confirm') {

          
          if(dialogType.value === 'add') {
            await addOrder(submitData)
            // 清空临时表单数据
            tempOrderForm.value = null
          } else {
                      // 在控制台输出请求参数
          console.log('Request Params:', JSON.stringify(submitData))
            await updateOrder(submitData)
          }
          
          ElMessage.success('提交成功')
          orderFormVisible.value = false
          getOrderData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('提交失败')
        }
      }
    }
  })
}

const exportCurrentPage = () => {
  // 检查orderList是否为数组
  if (!Array.isArray(orderList.value)) {
    ElMessage.error('订单数据不是数组格式')
    return
  }

  // 定义表头
  const headers = ['订单编号', '商品名称', '客户名称', '平台', '数量', '金额', '状态', '下单时间']

  // 生成CSV内容
  const csvContent = headers.join(',') + '\n' + 
    orderList.value.map(order => [
      order.orderNumber,
      order.productName,
      order.customerName,
      order.platform,
      order.quantity,
      order.totalAmount?.toFixed(2),
      order.status,
      formatDate(order.createdAt)
    ].join(',')).join('\n')

  try {
    // 创建Blob对象并下载
    const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
    const url = URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `订单列表_${new Date().toISOString().split('T')[0]}.csv`
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

const exportOrderDetail = (order) => {
  // 读取XML模板
  fetch('/src/assets/order.xml')
    .then(response => response.text())
    .then(template => {
         // 添加调试日志
         console.log('订单数据:', order);
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
        .replace('${order.orderNumber}', selectedOrder.value.orderNumber || '暂无')
        .replace('${order.platform}', selectedOrder.value.platform || '暂无')
        .replace('${order.createdAt}', formatDate(selectedOrder.value.createdAt))
        .replace('${order.customerName}', selectedOrder.value.customerName || '暂无')
        .replace('${order.phone}', selectedOrder.value.phone || '暂无')
        .replace('${order.address}', selectedOrder.value.address || '暂无')
        .replace('${order.productName}', selectedOrder.value.productName || '暂无')
        .replace('${order.quantity}', selectedOrder.value.quantity || '0')
        .replace('${order.price}', formatMoney(selectedOrder.value.price))
        .replace('${order.totalPrice}', formatMoney(selectedOrder.value.totalPrice))
        .replace('${order.status}', selectedOrder.value.status || '暂无');

   // 添加调试日志
   console.log('替换后的内容片段:', docContent.substring(0, 500));

// 创建Blob对象
const blob = new Blob([docContent], {
  type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
});

// 创建下载链接并触发下载
const link = document.createElement('a');
link.href = URL.createObjectURL(blob);
link.download = `订单_${formatDate(new Date())}.docx`;
document.body.appendChild(link);
link.click();
document.body.removeChild(link);
URL.revokeObjectURL(link.href);

ElMessage.success(`订单已导出为Word文档`);
})
.catch(error => {
console.error('导出Word文档失败:', error);
ElMessage.error('导出Word文档失败');
});
};

const getStatusType = (status) => {
  const map = {
    '待付款': 'danger',
    '待发货': 'warning', 
    '已发货': 'primary',
    '已完成': 'success',
    '已取消': 'info',
    '已付款': 'default'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  return status
}

// 格式化日期,只显示年月日
const formatDate = (date) => {
  if(!date) return ''
  if(typeof date !== 'string') {
    return new Date(date).toISOString().split('T')[0]
  }
  return date.split(' ')[0]
}

// 处理更多操作
const handleCommand = (command, row) => {
  switch(command) {
    case 'detail':
      handleDetail(row)
      break
    case 'edit':
      handleEdit(row)
      break
    case 'delete':
      ElMessageBox.confirm('确定要删除该订单吗?', '提示', {
        type: 'warning'
      }).then(() => {
        handleDelete(row)
      })
      break
    case 'ship':
      handleShip(row)
      break
  }
}

onMounted(() => {
  getOrderData()
})
//订单发货
const handleShip = async (row) => {
  try {
    //提示用户是否确认发货
    await ElMessageBox.confirm('确定要发货吗?', '提示', {
      type: 'warning'
    })
    
    //调用后端api将状态变成发货
    const res = await changeOrderStatus(row.orderNumber)
    if(res.code === 200) {
      ElMessage.success('发货成功')
      //重新获取订单列表
      await getOrderData()
    } else {
      ElMessage.error(res.msg || '发货失败')
    }
  } catch(error) {
    if(error !== 'cancel') {
      console.error('发货失败:', error)
      ElMessage.error('发货失败')
    }
  }
}

//获取商品名称列表
const productNameList = async () => {
  const res = await getProductName()
  console.log(res)
  //绑定数据
  productOptions.value = res.data.productNameList
}
productNameList()
</script>

<template>
  <div class="order-manage">
    <div class="header">
      <h1>订单管理</h1>
      <div class="actions">
        <el-button type="primary" @click="handleAddOrder" :icon="Plus" size="large">新增订单</el-button>
        <el-button type="success" @click="exportCurrentPage" :icon="Download" size="large">导出当前页</el-button>
      </div>
    </div>

    <el-card class="filter-section">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="商品名称/客户名称" clearable />
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="订单状态" clearable style="width: 200px">
            <el-option label="全部" value="" />
            <el-option label="待付款" value="待付款" />
            <el-option label="待发货" value="待发货" />
            <el-option label="已发货" value="已发货" />
            <el-option label="已完成" value="已完成" />
            <el-option label="已取消" value="已取消" />
            <el-option label="退款中" value="退款中" />
            <el-option label="已退款" value="已退款" />
          </el-select>
        </el-form-item>
        <el-form-item label="平台">
          <el-select v-model="searchForm.platform" placeholder="平台" clearable style="width: 200px">
            <el-option v-for="p in platforms" :key="p" :label="p" :value="p" />
          </el-select>
        </el-form-item>
        <el-form-item label="下单时间">
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
          <el-button type="primary" @click="handleSearch" :icon="Search" size="default">搜索</el-button>
          <el-button @click="resetSearch" size="default">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-section">
      <el-table
        :data="orderList"
        border
        stripe
        v-loading="loading"
        style="width: 100%"
      >
        <el-table-column prop="orderNumber" label="订单编号" width="220" align="center"/>
        <el-table-column prop="productName" label="商品名称" align="center"/>
        <el-table-column prop="customerName" label="客户名称" align="center"/>
        <el-table-column prop="platform" label="平台" width="120" align="center"/>
        <el-table-column prop="quantity" label="数量" width="80" align="center"/>
        <el-table-column prop="totalAmount" label="金额" width="120" align="center">
          <template #default="{ row }">
            ¥{{ row.totalAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="light" round>
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="下单时间" width="120" align="center">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-dropdown @command="(command) => handleCommand(command, row)" trigger="click">
              <el-button type="primary" link>
                操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :icon="View" command="detail">查看详情</el-dropdown-item>
                  <el-dropdown-item :icon="Edit" command="edit">编辑</el-dropdown-item>
                  <el-dropdown-item :icon="Van" v-if="row.status === '待发货'" command="ship">发货</el-dropdown-item>
                  <el-dropdown-item :icon="Delete" command="delete" divided>删除</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          layout=" jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 订单详情弹窗 -->
    <el-dialog 
      title="订单详情" 
      v-model="orderDetailVisible" 
      width="800px"
      destroy-on-close
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ selectedOrder.orderNumber }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ selectedOrder.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="客户名称">{{ selectedOrder.customerName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ selectedOrder.phone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ selectedOrder.address }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ selectedOrder.productName }}</el-descriptions-item>
        <el-descriptions-item label="平台">{{ selectedOrder.platform }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ selectedOrder.quantity }}</el-descriptions-item>
        <el-descriptions-item label="单价">¥{{ selectedOrder.price?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ selectedOrder.totalPrice?.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag :type="getStatusType(selectedOrder.status)" effect="light" round>
            {{ selectedOrder.status }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="orderDetailVisible = false">关闭</el-button>
        <el-button type="success" @click="exportOrderDetail" :icon="Download">
          导出订单详情
        </el-button>
      </template>
    </el-dialog>

    <!-- 新增/编辑订单弹窗 -->
    <el-dialog 
      :title="dialogType === 'add' ? '新增订单' : '编辑订单'"
      v-model="orderFormVisible" 
      width="600px"
      destroy-on-close
    >
      <el-form 
        ref="orderFormRef"
        :model="orderForm"
        :rules="orderFormRules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="productId">
          <el-select 
            v-model="orderForm.productId" 
            @change="handleProductChange"
            placeholder="请选择商品"
            style="width: 100%"
            :disabled="dialogType === 'edit'"
          >
            <el-option 
              v-for="product in productOptions" 
              :key="product.productId" 
              :label="product.productName" 
              :value="product.productId" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input v-model="orderForm.stockQuantity" disabled>
          </el-input>
        </el-form-item>
        <el-form-item label="单价">
          <el-input v-model="orderForm.price" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="数量" prop="quantity">
          <el-input-number 
            v-model="orderForm.quantity" 
            :min="1"
            :max="orderForm.stockQuantity || 1"
            @change="calculateTotal"
            style="width: 100%"
            :disabled="dialogType === 'edit'"
          />
        </el-form-item>
        <el-form-item label="总价">
          <el-input v-model="orderForm.totalPrice" disabled>
            <template #append>元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="客户名称" prop="customerName">
          <el-input v-model="orderForm.customerName" />
        </el-form-item>
        <el-form-item label="平台" prop="platform">
          <el-select v-model="orderForm.platform" style="width: 100%">
            <el-option v-for="p in platforms" :key="p" :label="p" :value="p" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="orderForm.phone" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="orderForm.address" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="orderFormVisible = false">取消</el-button>
        <el-button v-if="dialogType === 'add'" type="warning" @click="saveTempOrder">保存草稿</el-button>
        <el-button type="primary" @click="submitOrder">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.order-manage {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.header h1 {
  margin: 0;
  font-size: 24px;
}

.filter-section {
  margin-bottom: 20px;
}

.table-section {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-form--inline .el-form-item) {
  margin-right: 20px;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>
