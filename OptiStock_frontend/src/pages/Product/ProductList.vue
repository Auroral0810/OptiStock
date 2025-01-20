<script setup>
import { ref } from 'vue'
import { Plus, Download } from '@element-plus/icons-vue'
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
// 商品数据
const allProductData = ref([])
import { useProductCategoryStore } from '@/stores/productCategoryStore'
const productCategoryStore = useProductCategoryStore()
// 商品分类数据
const categoryOptions = ref([])
// SKU选项
const skuOptions = ref([])

// 获取状态标签类型
const getStatusType = (status) => {
  const types = { '上架': 'success', '下架': 'warning', '停产': 'danger' }
  return types[status] || 'info'
}

// 获取库存数量标签类型
const getStockQuantityType = (quantity) => {
  if (quantity === 0) return 'danger'
  if (quantity < 10) return 'warning'
  return 'success'
}

// 分页大小切换
const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  productList()
}
//更改当前页
const handlePageChange = (val) => {
  currentPage.value = val
  productList()
}
// 重置筛选条件
const resetFilter = () => {
  filterForm.value = { name: '', categoryName: '', status: '', sku: '' }
  productList()
}
//获取Sku列表和商品分类列表
import { getSkuListAndCategoryList,getProductList } from '@/api/product'
const skuListAndCategoryList = async () => {
  const res = await getSkuListAndCategoryList()
  // 直接将返回的skuList数组赋值给skuOptions
  skuOptions.value = res.data.skuList.map(sku => ({
    value: sku,
    label: sku
  }))
  // 直接将返回的categoryNameList数组赋值给categoryOptions 
  categoryOptions.value = res.data.categoryNameList.map(category => ({
    value: category,
    label: category
  }))
  //把skuList和categoryNameList存储到Store中
  productCategoryStore.setSkuList(res.data.skuList)
  productCategoryStore.setCategoryNameList(res.data.categoryNameList)
}
skuListAndCategoryList()
import { useProductStore } from '@/stores/productStore'
const productStore = useProductStore()
// 获取商品列表，直接将搜索的条件一起发送
const productList = async () => {
  const res = await getProductList(currentPage.value, pageSize.value, filterForm.value)
  // console.log(res)
  allProductData.value = res.data.rows
  total.value = res.data.total
  //将当前页存储在store中
  productStore.setProductList(res.data.rows)
  // console.log(allProductData.value)
  // console.log(total.value)
}
productList()
// 搜索商品
const handleSearch = () => {
  currentPage.value = 1
  productList()
}
//导出当前页，根据store中存储的商品信息导出成csv文件
const exportCurrentPage = () => {
  const productList = productStore.getProductList()
  if (!productList || productList.length === 0) {
    ElMessage.warning('当前没有可导出的数据')
    return
  }

  // 定义CSV表头
  const headers = ['ID', '商品名称', '商品分类', 'SKU', '售价', '成本价', '库存数量', '单位', '状态']
  
  // 将数据转换为CSV格式
  const csvContent = [
    headers.join(','), // 添加表头
    ...productList.map(item => [
      item.id,
      `"${item.name}"`, // 使用双引号包裹可能包含逗号的文本
      `"${item.categoryName}"`,
      item.sku,
      item.price.toFixed(2),
      item.costPrice.toFixed(2), 
      item.stockQuantity,
      item.unit,
      item.status
    ].join(','))
  ].join('\n')

  // 创建Blob对象
  const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv;charset=utf-8;' })
  
  // 创建下载链接
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `商品列表_${new Date().toLocaleDateString()}.csv`
  
  // 触发下载
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  
  ElMessage.success('导出成功')
}
import { addProduct, updateProduct, deleteProduct } from '@/api/product'
//处理删除，先判断当前行的库存数量是否为0，如果为0，则删除，否则提示库存数量不为0
//TODO:还没实现接口
const handleDelete = (row) => {
  if(row.stockQuantity != 0){
    ElMessage.warning('库存数量不为0，无法删除')
    return
  }
  ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await deleteProduct(row.id)
    ElMessage.success('删除成功')
    // 刷新商品列表
    productList()
  })
}
/*
处理添加和编辑
公用一个抽屉，根据title判断是添加还是编辑
抽屉的信息：名称、分类、SKU、售价、成本价、单位、状态
库存数量不能修改和设置，新增的后端默认为0
分类是选择的，SKU是输入的，状态也是选择的，售价和成本价是输入的
*/
const drawerVisible = ref(false)
const title = ref('添加商品') 
// 商品表单
const productFormRef = ref(null)
const productForm = ref({
  id: '',
  name: '',
  categoryId: '', // 修改为categoryId
  categoryName: '',
  sku: '',
  price: 0,
  costPrice: 0,
  unit: '',
  status: ''
})
//校验规则
// 商品表单校验规则
const productRules = {
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 2, max: 20, message: '商品名称长度应在2-20个字符之间', trigger: 'blur' }
  ],
  categoryName: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  sku: [
    { required: true, message: '请输入商品SKU', trigger: 'blur' },
    { min: 3, max: 15, message: 'SKU长度应在3-15个字符之间', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]+$/, message: 'SKU只能包含字母、数字、下划线或短横线', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '价格必须大于0', trigger: 'blur' }
  ],
  costPrice: [
    { required: true, message: '请输入商品成本价', trigger: 'blur' },
    { type: 'number', min: 0.01, message: '成本价必须大于0', trigger: 'blur' }
  ],
  unit: [
    { required: true, message: '请输入商品单位', trigger: 'blur' },
    { min: 1, max: 5, message: '单位长度应在1-5个字符之间', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择商品状态', trigger: 'change' },
    { validator: (rule, value, callback) => {
        const validStatuses = ['上架', '下架', '停产']
        if (!validStatuses.includes(value)) {
          callback(new Error('商品状态必须是 上架/下架/停产'))
        } else {
          callback()
        }
      }, trigger: 'change'
    }
  ]
}

// 打开添加商品抽屉
const openAddDialog = () => {
  title.value = '添加商品'
  productForm.value = {
    id: '',
    name: '',
    categoryId: '',
    categoryName: '',
    sku: '',
    price: 0,
    costPrice: 0,
    unit: '',
    status: ''
  }
  drawerVisible.value = true
}
// 打开编辑商品抽屉
const openEditDialog = (row) => {
  title.value = '编辑商品'
  productForm.value = {
    ...row,
    categoryId: row.categoryId // 确保使用categoryId
  }
  drawerVisible.value = true
}
// 点击添加商品按钮
const handleAdd = () => {
  //先进行校验根据表单提交校验
  productFormRef.value.validate((valid) => {
    if (valid) {
      // 提示是否要添加商品
      ElMessageBox.confirm('确定要添加该商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await addProduct(productForm.value)
        // console.log(productForm.value)
        ElMessage.success('添加成功')
        drawerVisible.value = false
        // 刷新商品列表
        productList()
      }).catch(() => {
      })
    }
  })
}
// 点击编辑商品按钮
const handleEdit = () => {
  //先进行校验根据表单提交校验
  productFormRef.value.validate((valid) => {
    if (valid) {
      // 提示是否要编辑商品
      ElMessageBox.confirm('确定要编辑该商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await updateProduct(productForm.value)
        // console.log(productForm.value)
        ElMessage.success('编辑成功')
        drawerVisible.value = false
        // 刷新商品列表
        productList()
      }).catch(() => {
        ElMessage.info('取消编辑')
      })
    }
  })
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
          <el-button type="primary" :icon="Download" @click="exportCurrentPage">导出当前页</el-button>
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
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button type="primary" plain @click="resetFilter">重置筛选</el-button>
            </el-form-item>
          </div>
          <div class="form-row">
            <el-form-item label="SKU:">
              <el-select 
                v-model="filterForm.sku" 
                filterable 
                allow-create 
                placeholder="请输入或选择SKU" 
                clearable 
                style="width: 178px"
              >
                <el-option
                  v-for="item in skuOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
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
      <el-table :data="allProductData" border class="responsive-table">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="name" label="商品名称" min-width="120" />
        <el-table-column prop="categoryName" label="商品分类" min-width="120" />
        <el-table-column prop="sku" label="SKU" width="100" />
        <el-table-column label="售价" width="100" align="center">
          <template #default="{ row }">
            ¥{{ row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="成本价" width="100" align="center">
          <template #default="{ row }">
            ¥{{ row.costPrice.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="库存数量" width="85" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getStockQuantityType(row.stockQuantity)"
              effect="plain"
              class="stock-tag"
            >
              {{ row.stockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="unit" label="单位" width="55" align="center" />
        <el-table-column label="状态" width="70" align="center">
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
                  <el-dropdown-item @click="handleDelete(row)">删除</el-dropdown-item>
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

    <!-- 添加/编辑商品抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="title"
      direction="rtl"
      size="500px"
    >
      <el-form 
        ref="productFormRef"
        :model="productForm" 
        :rules="productRules"
        label-width="100px"
      >
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="productForm.name" placeholder="请输入商品名称"/>
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryName">
          <el-select v-model="productForm.categoryName" placeholder="请选择分类">
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="SKU" prop="sku">
          <el-input v-model="productForm.sku" placeholder="请输入SKU"/>
        </el-form-item>
        <el-form-item label="售价(元)" prop="price">
          <el-input-number v-model="productForm.price" :precision="2" :step="0.1" :min="0"/>
        </el-form-item>
        <el-form-item label="成本价(元)" prop="costPrice">
          <el-input-number v-model="productForm.costPrice" :precision="2" :step="0.1" :min="0"/>
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="productForm.unit" placeholder="请输入单位"/>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="productForm.status" placeholder="请选择状态">
            <el-option label="上架" value="上架"/>
            <el-option label="下架" value="下架"/>
            <el-option label="停产" value="停产"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="title == '添加商品' ? handleAdd() : handleEdit()">确认</el-button>
        </div>
      </template>
    </el-drawer>
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
.stock-tag {
  min-width: 40px;
  text-align: center;
  font-weight: bold;
}
</style>
