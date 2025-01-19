<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Setting, Tools, Bell, Message, Lock } from '@element-plus/icons-vue'

const configForm = ref({
  siteName: '后台管理系统',
  siteDesc: '一个现代化的后台管理系统',
  notifyEmail: 'admin@example.com',
  maxUploadSize: 10,
  allowRegister: true,
  maintenance: false
})

const handleSubmit = async () => {
  try {
    // TODO: 调用保存配置API
    ElMessage.success('配置保存成功')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}
</script>

<template>
  <div class="system-config-container">
    <div class="config-card">
      <div class="header">
        <div class="title-section">
          <h1>系统配置</h1>
          <p class="subtitle">管理系统的基本设置与参数配置</p>
        </div>
        <div class="icon-section">
          <el-icon :size="24" color="#409EFF"><Setting /></el-icon>
        </div>
      </div>

      <el-form 
        :model="configForm"
        label-position="left"
        label-width="120px"
        class="config-form">
        
        <el-divider content-position="left">
          <el-icon><Tools /></el-icon>
          基础设置
        </el-divider>

        <el-form-item label="系统名称">
          <el-input v-model="configForm.siteName" placeholder="请输入系统名称"/>
        </el-form-item>

        <el-form-item label="系统描述">
          <el-input 
            v-model="configForm.siteDesc"
            type="textarea"
            placeholder="请输入系统描述"/>
        </el-form-item>

        <el-divider content-position="left">
          <el-icon><Bell /></el-icon>
          通知设置
        </el-divider>

        <el-form-item label="通知邮箱">
          <el-input 
            v-model="configForm.notifyEmail"
            placeholder="请输入系统通知邮箱">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-divider content-position="left">
          <el-icon><Lock /></el-icon>
          安全设置
        </el-divider>

        <el-form-item label="最大上传大小">
          <el-input-number 
            v-model="configForm.maxUploadSize"
            :min="1"
            :max="100"
            :step="1"
            :step-strictly="true">
            <template #append>MB</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="开放注册">
          <el-switch v-model="configForm.allowRegister"/>
        </el-form-item>

        <el-form-item label="维护模式">
          <el-switch v-model="configForm.maintenance"/>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">
            保存配置
          </el-button>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<style scoped>
.system-config-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
}

.config-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.title-section h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.subtitle {
  margin: 8px 0 0;
  font-size: 14px;
  color: #909399;
}

.config-form {
  max-width: 800px;
  margin: 0 auto;
}

.el-divider {
  margin: 24px 0;
}

:deep(.el-divider__text) {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409EFF;
  font-weight: bold;
}

.el-form-item {
  margin-bottom: 22px;
}
</style>
