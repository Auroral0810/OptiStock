<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, Refresh } from '@element-plus/icons-vue'

// 数据列表相关
const loading = ref(false)
const supplierList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(20)

// 过滤表单
const filterForm = reactive({
  name: '',
  contact: '',
  phone: ''
})

// 供应商表单
const dialogVisible = ref(false)
const dialogType = ref('create')
const supplierFormRef = ref(null)
const supplierForm = reactive({
  id: null,
  name: '',
  contact: '',
  phone: '',
  email: '',
  address: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入供应商名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 25 个字符', trigger: 'blur' }
  ],
  contact: [
    { required: true, message: '请输入联系人姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 15 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    //邮箱格式正则表达式
    { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '输入的邮箱地址格式不正确', trigger: 'blur' }
  ]
}

// 初始化加载
onMounted(() => {
  fetchSupplierList()
})
import { getSupplierList, addSupplier, updateSupplier,deleteSupplier } from '@/api/supplier'
import { useSupplierStore } from '@/stores/supplier'
const supplierStore = useSupplierStore()
// 获取供应商列表
const fetchSupplierList = async () => {
  console.log(currentPage.value, pageSize.value, filterForm)
  const res = await getSupplierList(currentPage.value, pageSize.value, filterForm)
  console.log(res)
  //绑定数据
  supplierList.value = res.data.rows
  total.value = res.data.total
  // 将数据存储到store中
  supplierStore.setSupplierList(res.data.rows)
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  fetchSupplierList()
}

// 重置过滤
const resetFilter = () => {
  Object.keys(filterForm).forEach(key => {
    filterForm[key] = ''
  })
  fetchSupplierList()
}

// 分页处理
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchSupplierList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchSupplierList()
}
// 导出当前页供应商数据
//导出supplierList数据中的当前页的数据为csv文件，同时要求先加入表头
const exportSuppliers = async () => {
 const headers = ['ID', '供应商名称', '联系人', '联系电话', '电子邮件', '地址']
 const data = supplierList.value.slice((currentPage.value - 1) * pageSize.value, currentPage.value * pageSize.value)
 const csvContent = [headers.join(','), ...data.map(item => Object.values(item).join(','))].join('\n')
 const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
 const url = URL.createObjectURL(blob)
 const a = document.createElement('a')
 a.href = url
 a.download = 'suppliers.csv'
 a.click()
}


// 打开供应商表单对话框
const openSupplierDialog = (row = null) => {
  dialogType.value = row ? 'edit' : 'create'
  if (row) {
    Object.keys(supplierForm).forEach(key => {
      supplierForm[key] = row[key]
    })
  } else {
    Object.keys(supplierForm).forEach(key => {
      supplierForm[key] = ''
    })
  }
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!supplierFormRef.value) return
  await supplierFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogType.value === 'create') {
          //提示用户是否确认新增
          ElMessageBox.confirm('确认新增供应商吗？', '提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            //调用新增供应商接口
            await addSupplier(supplierForm)
            // 刷新供应商列表
            fetchSupplierList()
            ElMessage.success('新增供应商成功')
            dialogVisible.value = false
          })
        } else {
          //提示用户是否确认编辑
          ElMessageBox.confirm('确认编辑供应商吗？', '提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(async () => {
            //调用编辑供应商接口
            await updateSupplier(supplierForm)
            // 刷新供应商列表
            fetchSupplierList()
            ElMessage.success('编辑供应商成功')
            dialogVisible.value = false
          })
        }
      } catch (error) {
        ElMessage.error(`${dialogType.value === 'create' ? '新增' : '更新'}供应商失败`)
      }
    }
  })
}

// 删除供应商
const handleDelete = (row) => {
  ElMessageBox.confirm(
    '确认删除该供应商吗？',
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    //调用删除供应商接口
    const res = await deleteSupplier(row.id)
    ElMessage.success(res.msg?res.msg:'删除供应商成功')
    // 刷新供应商列表
    fetchSupplierList()
    // ElMessage.success('删除供应商成功')
  })
}
</script>
<template>
  <div class="supplier-container">
    <!-- 页面标题区域 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="main-title">供应商管理</h1>
        <div class="header-actions">
          <el-button type="primary" :icon="Plus" @click="openSupplierDialog()">
            新增供应商
          </el-button>
          <el-button type="success" :icon="Download" @click="exportSuppliers">
            导出当前页
          </el-button>
        </div>
      </div>
    </div>

    <!-- 搜索过滤区域 -->
    <el-card class="filter-section" shadow="hover">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="供应商名称">
          <el-input v-model="filterForm.name" placeholder="输入供应商名称" clearable />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="filterForm.contact" placeholder="输入联系人姓名" clearable />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="filterForm.phone" placeholder="输入联系电话" clearable />
        </el-form-item>
        <el-form-item class="filter-buttons">
          <el-button type="primary" :icon="Search" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 供应商列表 -->
    <el-card class="supplier-list" shadow="hover">
      <el-table 
        :data="supplierList"
        border
        stripe
        v-loading="loading"
        class="supplier-table"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="供应商名称" min-width="180" />
        <el-table-column prop="contact" label="联系人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="email" label="电子邮件" min-width="200" />
        <el-table-column prop="address" label="地址" min-width="250" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="openSupplierDialog(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <div class="pagination-section">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="jumper,total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 供应商表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'create' ? '新增供应商' : '编辑供应商'"
      width="50%"
      :destroy-on-close="true"
    >
      <el-form
        ref="supplierFormRef"
        :model="supplierForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="供应商名称" prop="name">
          <el-input v-model="supplierForm.name" placeholder="请输入供应商名称" />
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="supplierForm.contact" placeholder="请输入联系人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="supplierForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="supplierForm.email" placeholder="请输入电子邮件" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input
            v-model="supplierForm.address"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.supplier-container {
  padding: 20px;
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
  gap: 12px;
}

.supplier-list {
  margin-bottom: 20px;
}

.supplier-table {
  margin-bottom: 16px;
}

.pagination-section {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

:deep(.el-dialog__body) {
  padding-top: 20px;
}
</style>
