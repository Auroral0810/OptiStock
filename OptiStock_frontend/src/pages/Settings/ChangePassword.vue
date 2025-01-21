<script setup>
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Lock, Key, Check } from '@element-plus/icons-vue'

const loading = ref(false)
const formRef = ref(null)

const passwordForm = ref({
  oldPassword: '',
  newPassword: '', 
  confirmPassword: ''
})

const rules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' },
    { min: 5, max: 16, message: '原密码长度为5-16位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 5, max: 16, message: '新密码长度为5-16位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}
import { changePassword } from '@/api/user'
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid, fields) => {
    if (valid) {
      ElMessageBox.confirm('确定要修改密码吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        try {
          loading.value = true
          await changePassword(passwordForm.value.oldPassword, passwordForm.value.newPassword)
          ElMessage.success('修改密码成功')
          resetForm()
        } catch (error) {
          console.error(error)
        } finally {
          loading.value = false
        }
      }).catch(() => {
        ElMessage.info('用户取消修改密码')
      })
    } else {
      const firstField = Object.keys(fields)[0]
      const firstError = fields[firstField][0]
      ElMessage.error(firstError.message)
      return false
    }
  })
}

const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
}
</script>

<template>
  <div class="change-password-container">
    <div class="ripple-background">
      <div class="circle xxlarge shade1"></div>
      <div class="circle xlarge shade2"></div>
      <div class="circle large shade3"></div>
      <div class="circle medium shade4"></div>
      <div class="circle small shade5"></div>
    </div>
    <div class="change-password-card animate__animated animate__fadeIn">
      <div class="header">
        <div class="title-section">
          <h1 class="gradient-title">修改密码</h1>
          <p class="subtitle">为了保护您的账户安全，请定期更换密码</p>
        </div>
        <div class="security-icon">
          <el-icon :size="32" color="#3B82F6"><Lock /></el-icon>
        </div>
      </div>

      <el-form 
        ref="formRef"
        :model="passwordForm"
        :rules="rules"
        label-position="right"
        label-width="100px"
        class="password-form">
        
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
            :prefix-icon="Lock"
            size="large"
            class="custom-input">
          </el-input>
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
            :prefix-icon="Key"
            size="large"
            class="custom-input">
          </el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
            :prefix-icon="Check"
            size="large"
            class="custom-input">
          </el-input>
        </el-form-item>

        <div class="form-actions">
          <el-button 
            @click="resetForm" 
            plain 
            size="large"
            class="reset-btn">
            重置
          </el-button>
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :loading="loading"
            size="large"
            class="submit-btn">
            确认修改
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.change-password-container {
  height: calc(100vh - 60px);
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
  padding: 20px;
  position: relative;
  overflow: hidden;
}

.ripple-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: white;
  animation: ripple 15s infinite;
  box-shadow: 0 0 1px 0 rgba(255, 255, 255, 0.3);
}

.small {
  width: 200px;
  height: 200px;
  left: -100px;
  bottom: -100px;
}

.medium {
  width: 400px;
  height: 400px;
  left: -200px;
  bottom: -200px;
}

.large {
  width: 600px;
  height: 600px;
  left: -300px;
  bottom: -300px;
}

.xlarge {
  width: 800px;
  height: 800px;
  left: -400px;
  bottom: -400px;
}

.xxlarge {
  width: 1000px;
  height: 1000px;
  left: -500px;
  bottom: -500px;
}

.shade1 {
  opacity: 0.2;
}
.shade2 {
  opacity: 0.15;
}
.shade3 {
  opacity: 0.1;
}
.shade4 {
  opacity: 0.05;
}
.shade5 {
  opacity: 0.02;
}

@keyframes ripple {
  0% {
    transform: scale(0.8);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(0.8);
  }
}

.change-password-card {
  width: 100%;
  max-width: 600px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.15);
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  z-index: 1;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.gradient-title {
  font-size: 28px;
  background: linear-gradient(45deg, #409EFF, #36D1DC);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0 0 8px 0;
}

.subtitle {
  color: #909399;
  font-size: 14px;
  margin: 0;
}

.security-icon {
  background: #ecf5ff;
  padding: 16px;
  border-radius: 12px;
}

.custom-input {
  width: 100%;
}

.custom-input :deep(.el-input__wrapper) {
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  padding: 4px 12px;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 32px;
}

.reset-btn,
.submit-btn {
  min-width: 120px;
  height: 44px;
  font-size: 16px;
  border-radius: 8px;
  transition: all 0.3s;
}

.reset-btn:hover {
  background: #f5f7fa;
  border-color: #dcdfe6;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64,158,255,0.3);
}

@media screen and (max-width: 768px) {
  .change-password-container {
    padding: 16px;
  }
  
  .change-password-card {
    padding: 24px;
  }
}
</style>
