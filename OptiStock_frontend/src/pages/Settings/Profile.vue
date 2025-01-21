<script setup>
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useAuthStore } from '@/utils/auth'
import { Edit, Check, Close, User, UserFilled, Message, Phone, Camera } from '@element-plus/icons-vue'

const authStore = useAuthStore()
const isEditing = ref(false)

const userForm = ref({
  username: authStore.userInfo?.username || '未登录',
  nickname: authStore.userInfo?.nickname || '',
  email: authStore.userInfo?.email || '',
  phone: authStore.userInfo?.phone || '',
  avatar: authStore.userInfo?.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
})

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '昵称长度在2-10个字符之间', trigger: 'blur' }
  ],
  email: [
    { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ]
}

const formRef = ref(null)

const handleEdit = () => {
  isEditing.value = true
}

const handleCancel = () => {
  isEditing.value = false
  userForm.value = { ...authStore.userInfo }
  formRef.value?.clearValidate()
}
import { userUpdateInfo } from '@/api/user'

const handleSubmit = async () => {
    ElMessageBox.confirm('确定要更新吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            const updatedUserInfo = { ...userForm.value }
            delete updatedUserInfo.username

            await userUpdateInfo(updatedUserInfo)
            authStore.updateUserInfo(userForm.value)

            ElMessage.success('更新成功')
            isEditing.value = false
        } catch (error) {
            console.error(error)
        }
    }).catch(() => {
        ElMessage.info('用户取消更新')
    })
}

const handleAvatarSuccess = (result)=>{
    userForm.value.avatar = result.data
}
</script>

<template>
  <div class="profile-container">
    <div class="ripple-background">
      <div class="circle xxlarge shade1"></div>
      <div class="circle xlarge shade2"></div>
      <div class="circle large shade3"></div>
      <div class="circle medium shade4"></div>
      <div class="circle small shade5"></div>
    </div>
    <div class="profile-card animate__animated animate__fadeIn">
      <div class="header">
        <div class="title-section">
          <h1 class="gradient-title">个人资料</h1>
          <p class="subtitle">完善您的个人信息，让我们更好地为您服务</p>
        </div>
        <div>
          <el-button v-if="!isEditing" type="primary" @click="handleEdit" :icon="Edit" size="large" class="edit-btn">编辑资料</el-button>
          <div v-else class="btn-group">
            <el-button type="success" @click="handleSubmit" :icon="Check" size="large" class="action-btn">保存</el-button>
            <el-button type="info" @click="handleCancel" :icon="Close" size="large" class="action-btn">取消</el-button>
          </div>
        </div>
      </div>

      <div class="profile-content">
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <el-avatar :size="270" :src="userForm.avatar" class="user-avatar" />
            <div v-if="isEditing" class="avatar-overlay">
              <el-upload
                ref="uploadRef"
                class="avatar-uploader" 
                :show-file-list="false"
                :auto-upload="true"
                action="/api/upload"
                name="file"
                :headers="{'Authorization': authStore.token}"
                :on-success="handleAvatarSuccess"
              >
                <el-icon size="40"><Camera /></el-icon>
                <span>更换头像</span>
              </el-upload>
            </div>
          </div>
          <div class="user-name">{{ userForm.nickname || userForm.username }}</div>
        </div>

        <el-form 
          ref="formRef"
          :model="userForm" 
          :rules="rules"
          label-width="140px" 
          class="profile-form"
          :disabled="!isEditing">
          <el-form-item label="用户名" size="large">
            <el-input v-model="userForm.username" disabled class="custom-input">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="昵称" prop="nickname" size="large">
            <el-input v-model="userForm.nickname" placeholder="请输入昵称" class="custom-input">
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email" size="large">
            <el-input v-model="userForm.email" placeholder="请输入邮箱" class="custom-input">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="phone" size="large">
            <el-input v-model="userForm.phone" placeholder="请输入手机号" class="custom-input">
              <template #prefix>
                <el-icon><Phone /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-container {
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

.shade1 { opacity: 0.2; }
.shade2 { opacity: 0.5; }
.shade3 { opacity: 0.7; }
.shade4 { opacity: 0.8; }
.shade5 { opacity: 0.9; }

@keyframes ripple {
  0% { transform: scale(0.8); }
  50% { transform: scale(1.2); }
  100% { transform: scale(0.8); }
}

.profile-card {
  width: 90%;
  max-width: 800px;
  height: 90%;
  max-height: 800px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 40px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
  overflow: hidden;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f2f5;
}

.gradient-title {
  margin: 0;
  font-size: 32px;
  background: linear-gradient(45deg, #409EFF, #36D1DC);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  margin: 8px 0 0;
  color: #909399;
  font-size: 16px;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 20px;
}

.user-avatar {
  border: 4px solid #fff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
  cursor: pointer;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.user-name {
  font-size: 32px;
  font-weight: 600;
  color: #2c3e50;
  margin-top: 12px;
}

.profile-form {
  max-width: 600px;
  margin: 0 auto;
}

.custom-input {
  :deep(.el-input__wrapper) {
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: none;
    border: 1px solid #e4e7ed;
    transition: all 0.3s;
    height: 56px;
    font-size: 22px;
  }

  :deep(.el-input__wrapper:hover) {
    border-color: #409EFF;
  }

  :deep(.el-input__wrapper.is-focus) {
    border-color: #409EFF;
    box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
  }
}

.edit-btn,
.action-btn {
  padding: 14px 36px;
  font-size: 18px;
  font-weight: 500;
  border-radius: 8px;
  transition: all 0.3s;
}

.edit-btn:hover,
.action-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.btn-group {
  display: flex;
  gap: 12px;
}

:deep(.el-form-item__label) {
  font-size: 18px;
  font-weight: 500;
}

@media screen and (max-width: 768px) {
  .profile-card {
    width: 95%;
    padding: 20px;
  }

  .gradient-title {
    font-size: 24px;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 20px;
  }

  .btn-group {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
