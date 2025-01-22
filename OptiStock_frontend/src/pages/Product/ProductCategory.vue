<script setup>
import { ref, reactive } from 'vue'
import { Plus, Search, Edit, Delete, Upload, Download } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useProductCategoryStore } from '@/stores/productCategoryStore'

const productCategoryStore = useProductCategoryStore()
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(20)
//抽屉的名称
const title = ref('添加分类')
//搜索条件
const filterForm = reactive({
  name: '',
  parentName: ''
})

// 添加分类抽屉相关
const drawerVisible = ref(false)
const addForm = reactive({
  id:'',
  name: '',
  parentId: '',
  parentName: ''
})

// 表单校验规则
const rules = reactive({
  name: [
    { required: true, message: '请输入分类名称', trigger: 'blur' },
    { max: 15, message: '分类名称不能超过15个字符', trigger: 'blur' }
  ]
})

// 搜索功能
const handleSearch = () => {
  // 调用方法
ProductCategoryList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.name = ''
  filterForm.parentName = ''
  ProductCategoryList()
}
// 分类数据
const categoryData = ref([])
const parentCategoryDate = ref([])

// 处理分页
const handleSizeChange = (val) => {
  pageSize.value = val
  //调用方法
  ProductCategoryList()
}

const handlePageChange = (val) => {
  currentPage.value = val
  //调用方法
  ProductCategoryList()
}

import { getAllProductCategory, getParentCategoryList } from '@/api/product'
//获取全部的商品分类信息
const ProductCategoryList = async () => {
  //在parentCategoryDate中查找分类条件对应的id
  const parentId = parentCategoryDate.value.find(item => item.name == filterForm.parentName)?parentCategoryDate.value.find(item => item.name == filterForm.parentName).id:''
  //每次获取都加入所有的搜索条件
  const res = await getAllProductCategory(currentPage.value, pageSize.value,filterForm.name,parentId)
  console.log(res)
  //绑定数据
  categoryData.value = res.data.rows
  total.value = res.data.total
  //把数据存储到productstore中
  productCategoryStore.setCategoryList(res.data.rows)
}
//获取所有上级分类信息
const parentCategoryList = async () => {
  const res = await getParentCategoryList()
  //绑定数据
  parentCategoryDate.value = res.data
}
// 调用方法
ProductCategoryList()
parentCategoryList()

// 打开抽屉
const openDialog = (row) => {
  
  if(row){
    addForm.id = row.id
    addForm.name = row.name
    addForm.parentName = row.parentName
    }else{
    addForm.name = ''
    addForm.parentName = ''
    addForm.parentId = ''
  }
  drawerVisible.value = true
}

// 导入分类数据（后续需要添加导入功能）
// const handleImportSuccess = () => {
//   ElMessage.success('分类导入成功')
// }

// 导出分类数据
const exportData = () => {
  ElMessageBox.confirm('确定要导出分类数据吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 从store获取数据
    const data = productCategoryStore.getCategoryList()
    
    // 构建CSV内容
    const headers = ['ID', '分类名称', '上级分类', '商品数量', '创建时间']
    const csvContent = [
      headers.join(','),
      ...data.map(item => [
        item.id,
        item.name,
        item.parentName || '无',
        item.productCount,
        item.createdAt
      ].join(','))
    ].join('\n')

    // 创建Blob对象
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
    
    // 创建下载链接并触发下载
    const link = document.createElement('a')
    const url = URL.createObjectURL(blob)
    link.setAttribute('href', url)
    link.setAttribute('download', '商品分类数据.csv')
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    
    ElMessage.success('分类数据导出成功')
  }).catch(() => {})
}


import { addCategory, updateCategoryInfo,deleteProductCategory } from '@/api/product'
// 提交新增分类
const submitAdd = () => {
  if(!addForm.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  //判断是否要新增分类
  ElMessageBox.confirm('确定要新增分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then( async () => {
      //根据名称找出
  addForm.parentId= parentCategoryDate.value.find(item => item.name == addForm.parentName)?parentCategoryDate.value.find(item => item.name == addForm.parentName).id:''
  // console.log('提交新增分类:', addForm)
  await addCategory(addForm.name,addForm.parentId)
  ElMessage.success('添加成功')
  drawerVisible.value = false
  ProductCategoryList()
  parentCategoryList()
  }).catch(() => { })
}
// 更新分类
const submitEdit = () => {
  if(!addForm.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  console.log(addForm.parentId,addForm.id)
  addForm.parentId = parentCategoryDate.value.find(item => item.name == addForm.parentName)?parentCategoryDate.value.find(item => item.name == addForm.parentName).id:''
  //判断新的上级分类的id是否就是当前的id，因为自己的上级分类不能是自己
  if(addForm.parentId == addForm.id){
    ElMessage.warning('上级分类不能是当前分类')
    return
  }
  //判断是否要更新分类
  ElMessageBox.confirm('确定要更新分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then( async () => {
    //上级分类的id需要在parentCategoryDate中找到
    await updateCategoryInfo(addForm.id,addForm.name,addForm.parentId)
    // console.log(addForm.id,addForm.name,addForm.parentId)
    ElMessage.success('编辑成功')
    drawerVisible.value = false
    ProductCategoryList()
    parentCategoryList()
  }).catch(() => { })
}
// 删除分类
const handleDelete = (row) => {
  //判断是否还有商品，以及是否是顶级分类，顶级分类和有商品的分类都不能删除
  //TODO:（后续可以添加递归删除）
  // console.log(row)
  if(row.productCount > 0 || row.parentName == '无') {
    ElMessage.warning('顶级分类和有商品的分类不能删除')
    return
  }
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then( async () => {
    await deleteProductCategory(row.id)
    ElMessage.success('删除成功')
    ProductCategoryList()
    parentCategoryList()
  }).catch(() => { })
}

</script>

<template>
  <div class="category-container">
    <el-card class="box-card">
      <!-- 顶部标题和按钮区域 -->
      <div class="header">
        <div class="title">
          <h2>商品分类管理</h2>
          <span class="subtitle">管理和组织您的商品分类体系</span>
        </div>
        <div class="actions">
          <el-upload action="/api/upload" :show-file-list="false" accept=".csv, .xlsx" @success="handleImportSuccess">
            <!-- <el-button type="info" :icon="Upload">导入分类</el-button> -->
          </el-upload>
          <el-button type="primary" :icon="Download" @click="exportData">导出当前页</el-button>
          <el-button type="primary" :icon="Plus" @click="openDialog();title='添加分类'">添加分类</el-button>
        </div>
      </div>

      <!-- 搜索筛选区域 -->
      <div class="search-section">
        <el-form :inline="true" :model="filterForm" class="filter-form">
          <el-form-item label="分类名称">
            <el-input v-model="filterForm.name" placeholder="请输入分类名称" clearable :prefix-icon="Search" />
          </el-form-item>
          <el-form-item label="上级分类">
            <el-select v-model="filterForm.parentName" placeholder="请选择上级分类" style="width: 200px">
              <el-option label="全部" value="" />
              <el-option v-for="item in parentCategoryDate" :key="item.id" :label="item.name" :value="item.name" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <div class="search-buttons">
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="resetFilter">重置</el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <!-- 树形分类表格 -->
      <el-table :data="categoryData" border style="width: 100%; margin-top: 16px;" row-key="id" default-expand-all
        :header-cell-style="{ background: '#f5f7fa' }">
        <template v-for="column in [
          { prop: 'id', label: 'ID', width: '80', align: 'center', fixed: 'left' },
          { prop: 'name', label: '分类名称', minWidth: '180', fixed: 'left' },
          { prop: 'parentName', label: '上级分类', minWidth: '150' },
          { prop: 'productCount', label: '商品数量', width: '120', align: 'center' },
          { prop: 'createdAt', label: '创建时间', minWidth: '180', align: 'center' }
        ]" :key="column.prop">
          <el-table-column v-bind="column">
            <template #default="{ row }">
              {{ row[column.prop] }}
            </template>
          </el-table-column>
        </template>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link :icon="Edit" @click="openDialog(row);title='编辑分类'">编辑</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-section">
        <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 30, 50]"
          layout="jumper,total, sizes, prev, pager, next" :total="total" @size-change="handleSizeChange"
          @current-change="handlePageChange" />
      </div>
    </el-card>

    <!-- 分类抽屉 -->
    <el-drawer v-model="drawerVisible" :title="title" size="400px" :destroy-on-close="true">
      <el-form :model="addForm" :rules="rules" label-width="80px">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="上级分类">
          <el-select v-model="addForm.parentName" placeholder="请选择上级分类" style="width: 100%">
            <el-option label="无" value="" />
            <el-option v-for="item in parentCategoryDate" :key="item.id" :label="item.name" :value="item.name" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="title=='添加分类'?submitAdd():submitEdit()">确认</el-button>
          <el-button @click="drawerVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<style scoped>
.category-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.actions {
  display: flex;
  gap: 10px;
}

.search-section {
  margin-bottom: 16px;
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 4px;
}

.table-section {
  margin-top: 20px;
}

.pagination-section {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
