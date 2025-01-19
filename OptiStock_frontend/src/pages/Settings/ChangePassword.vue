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
    { min: 5, max: 16, message: '密码长度为5-16位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 5, max: 16, message: '密码长度为5-16位', trigger: 'blur' }
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
// 提示是否真的要修改密码
ElMessageBox.confirm('确定要修改密码吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            // 调用修改密码的接口
            await changePassword(passwordForm.value.oldPassword, passwordForm.value.newPassword)
            ElMessage.success('修改密码成功')
        } catch (error) {
            // 处理请求失败
            // ElMessage.error('修改密码失败，请稍后重试')
            console.error(error)
        }
    }).catch(() => {
        // 只有用户主动点击 “取消” 才会执行这里
        ElMessage.info('用户取消修改密码')
    })
}

const resetForm = () => {
  if (!formRef.value) return
  formRef.value.resetFields()
}
</script>

<template>
  <div class="change-password-container">
    <div class="change-password-card">
      <div class="header">
        <div class="title-section">
          <h1>修改密码</h1>
          <p class="subtitle">为了保护您的账户安全，请定期更换密码</p>
        </div>
        <div class="security-icon">
          <el-icon :size="24" color="#409EFF"><Lock /></el-icon>
        </div>
      </div>

      <el-form 
        ref="formRef"
        :model="passwordForm"
        :rules="rules"
        label-position="left"
        label-width="100px"
        class="password-form">
        
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
            :prefix-icon="Lock">
          </el-input>
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
            :prefix-icon="Key">
          </el-input>
        </el-form-item>

        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
            :prefix-icon="Check">
          </el-input>
        </el-form-item>

        <div class="form-actions">
          <el-button @click="resetForm" plain>重置</el-button>
          <el-button 
            type="primary" 
            @click="handleSubmit" 
            :loading="loading"
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
  padding: 20px;
  background: linear-gradient(135deg, #f6f8fb 0%, #f0f4f8 100%);
  min-height: calc(100vh - 60px);
}

.change-password-card {
  width: 100%;
  max-width: 600px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  margin-left: 24px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.title-section h1 {
  font-size: 20px;
  color: #1a1f36;
  margin: 0 0 4px 0;
  font-weight: 600;
}

.subtitle {
  color: #64748b;
  font-size: 13px;
  margin: 0;
}

.security-icon {
  background: rgba(64, 158, 255, 0.1);
  padding: 12px;
  border-radius: 8px;
}

.password-form {
  margin-top: 16px;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}

:deep(.el-form-item__label) {
  font-size: 14px;
  color: #1a1f36;
  font-weight: 500;
  padding-right: 12px;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
  border-radius: 6px;
  padding: 4px 12px;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 32px;
  padding-top: 16px;
  border-top: 1px solid #e5e7eb;
}

:deep(.el-button) {
  padding: 8px 24px;
  font-size: 14px;
  border-radius: 6px;
  height: auto;
  font-weight: 500;
  transition: all 0.3s;
}

.submit-btn {
  min-width: 120px;
}

:deep(.el-button:hover) {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .change-password-container {
    padding: 16px;
  }

  .change-password-card {
    margin-left: 0;
    padding: 20px;
  }

  .header {
    margin-bottom: 20px;
  }

  .title-section h1 {
    font-size: 18px;
  }

  .security-icon {
    padding: 10px;
  }

  :deep(.el-form-item__label) {
    padding-bottom: 4px;
  }

  .form-actions {
    margin-top: 24px;
  }
}
</style>
