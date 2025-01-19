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
    // 提示是否真的要更新
    ElMessageBox.confirm('确定要更新吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }).then(async () => {
        try {
            // 创建一个副本，并去掉 `username`
            const updatedUserInfo = { ...userForm.value }
            delete updatedUserInfo.username  // 删除 username

            // 调用更新信息的接口
            console.log(updatedUserInfo)  // 确保 `username` 不在这里
            await userUpdateInfo(updatedUserInfo)

            // 更新 Pinia 中的用户信息
            authStore.updateUserInfo(userForm.value)

            ElMessage.success('更新成功')
            isEditing.value = false
        } catch (error) {
            // 处理请求失败
            // ElMessage.error('更新失败，请稍后重试')
            console.error(error)
        }
    }).catch(() => {
        // 只有用户主动点击 “取消” 才会执行这里
        ElMessage.info('用户取消更新')
    })
}
//图片上传成功的回调函数
const handleAvatarSuccess = (result)=>{
    userForm.value.avatar = result.data
    // console.log(userForm.value.avatar)
}
</script>

<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="header">
        <h1>个人资料</h1>
        <div>
          <el-button v-if="!isEditing" type="primary" @click="handleEdit" :icon="Edit">编辑资料</el-button>
          <div v-else class="btn-group">
            <el-button type="success" @click="handleSubmit" :icon="Check">保存</el-button>
            <el-button type="info" @click="handleCancel" :icon="Close">取消</el-button>
          </div>
        </div>
      </div>

      <div class="profile-content">
        <!-- 头像区域 -->
        <div class="avatar-section">
          <div class="avatar-wrapper">
            <el-avatar :size="120" :src="userForm.avatar" />
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
                <el-icon size="24"><Camera /></el-icon>
                <span>更换头像</span>
              </el-upload>
            </div>
          </div>
          <div class="user-name">{{ userForm.nickname || userForm.username }}</div>
        </div>

        <!-- 个人信息表单 -->
        <el-form 
          ref="formRef"
          :model="userForm" 
          :rules="rules"
          label-width="90px" 
          class="profile-form"
          :disabled="!isEditing">
          <el-form-item label="用户名">
            <el-input v-model="userForm.username" disabled>
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="昵称" prop="nickname">
            <el-input v-model="userForm.nickname" placeholder="请输入昵称">
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="userForm.email" placeholder="请输入邮箱">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item label="手机号" prop="phone">
            <el-input v-model="userForm.phone" placeholder="请输入手机号">
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
/* 页面容器 */
.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 90vh;
  padding: 20px;
  background: linear-gradient(135deg, #f6f8fb 0%, #f0f4f8 100%);
}

/* 卡片样式 */
.profile-card {
  width: 90%;
  max-width: 700px;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.1);
  padding: 30px;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease-in-out;
}

/* 头部样式 */
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f2f5;
}

.header h1 {
  font-size: 24px;
  color: #1a1f36;
  margin: 0;
  font-weight: 600;
  letter-spacing: 0.5px;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-wrapper {
  position: relative;
  margin-bottom: 16px;
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
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

.avatar-overlay span {
  margin-top: 8px;
  font-size: 14px;
}

.user-name {
  font-size: 18px;
  font-weight: 500;
  color: #1a1f36;
  margin-top: 12px;
}

/* 表单样式 */
.profile-form {
  max-width: 500px;
  margin: 0 auto;
}

.el-form-item {
  margin-bottom: 24px;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border-radius: 8px;
  padding: 8px 12px;
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff;
}

:deep(.el-input__prefix) {
  margin-right: 8px;
}

/* 按钮样式 */
.btn-group {
  display: flex;
  gap: 12px;
}

:deep(.el-button) {
  padding: 10px 24px;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s;
}

:deep(.el-button:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

:deep(.el-button .el-icon) {
  margin-right: 6px;
}

/* 响应式调整 */
@media screen and (max-width: 768px) {
  .profile-card {
    width: 95%;
    padding: 20px;
  }

  .header h1 {
    font-size: 20px;
  }

  :deep(.el-form-item__label) {
    float: none;
    display: block;
    text-align: left;
    margin-bottom: 8px;
  }

  .el-form-item {
    margin-bottom: 20px;
  }
}
</style>
