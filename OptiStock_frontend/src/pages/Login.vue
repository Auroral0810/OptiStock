<script setup>
import { ref, reactive } from 'vue'
import { User, Lock, Message } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { userLogin, userRegister } from '@/api/user'

// 是否处于注册模式
const isRegister = ref(false)
const router = useRouter()

// 统一表单
const form = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: ''
})

// 表单验证规则
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { pattern: /^\S{5,16}$/, message: '用户名长度为5-16位，不能包含空格', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]{2,10}$/, message: '昵称长度为2-10位，可包含中文、字母、数字', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { pattern: /^\S{5,16}$/, message: '密码长度为5-16位，不能包含空格', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const loginFormRef = ref(null)

// 处理注册
const handleRegister = async () => {
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      const res = await userRegister(form.username, form.nickname, form.password)
      if (res.code === 200) {
        ElMessage.success('注册成功')
        isRegister.value = false
      }
    }
  })
}

import { useAuthStore } from '@/utils/auth'

const authStore = useAuthStore()
// 处理登录
const handleLogin = async () => {
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      const res = await userLogin(form.username, form.password)
      if (res.code === 200) {
        ElMessage.success('登录成功')
        console.log(res.data.token)
        // 存储 Token 和用户信息到 Pinia + LocalStorage
        authStore.login(res.data.token, res.data.userInfo)
        await router.push('/homepage/default')
      }
    }
  })
}

// 处理找回密码
const handleForgotPassword = () => {
  ElMessage.info('请联系管理员或通过邮箱找回密码')
}

// 处理第三方登录
const handleThirdPartyLogin = (type) => {
  ElMessage.info(`使用 ${type.toUpperCase()} 登录`)
}
</script>

<template>
  <div class="login-container">
    <div class="animated-bg">
      <div class="wave"></div>
      <div class="wave"></div>
      <div class="wave"></div>
    </div>
    <el-card class="login-card" shadow="hover">
      <div class="header-section">
        <h1 class="system-title">OptiStock</h1>
        <h2 class="login-title">{{ isRegister ? '用户注册' : '欢迎登录' }}</h2>
      </div>

      <el-form ref="loginFormRef" :model="form" :rules="rules" class="login-form" label-width="0">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="用户名"
            :prefix-icon="User"
            clearable
            class="custom-input"
          />
        </el-form-item>

        <el-form-item v-if="isRegister" prop="nickname">
          <el-input 
            v-model="form.nickname" 
            placeholder="昵称" 
            :prefix-icon="User" 
            clearable 
            class="custom-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            :prefix-icon="Lock"
            show-password
            clearable
            class="custom-input"
          />
        </el-form-item>

        <el-form-item v-if="isRegister" prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            type="password" 
            placeholder="确认密码" 
            :prefix-icon="Lock"
            show-password 
            clearable 
            class="custom-input"
          />
        </el-form-item>

        <el-form-item class="button-container">
          <el-button 
            type="primary" 
            @click="isRegister ? handleRegister() : handleLogin()" 
            class="submit-button"
          >
            {{ isRegister ? '注册' : '登录' }}
          </el-button>
        </el-form-item>

        <div class="operation-links">
          <el-link type="primary" @click="isRegister = !isRegister">
            {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
          </el-link>
          <el-link v-if="!isRegister" type="primary" @click="handleForgotPassword">
            忘记密码？
          </el-link>
        </div>

        <template v-if="!isRegister">
          <div class="divider">
            <span class="divider-text">其他登录方式</span>
          </div>

          <div class="third-party-login">
            <el-button @click="handleThirdPartyLogin('email')" class="third-party-button">
              <el-icon><Message /></el-icon>
            </el-button>
            <el-button @click="handleThirdPartyLogin('qq')" class="third-party-button">
              <el-icon><img src="@/assets/QQ.svg" alt="QQ" width="20" height="20" /></el-icon>
            </el-button>
            <el-button @click="handleThirdPartyLogin('wechat')" class="third-party-button">
              <el-icon><img src="@/assets/wechat-fill.svg" alt="WeChat" width="20" height="20" /></el-icon>
            </el-button>
          </div>
        </template>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  position: relative;
  overflow: hidden;
}

.animated-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, #00c6fb, #005bea);
  overflow: hidden;
}

.wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320"><path fill="rgba(255,255,255,0.3)" d="M0,192L48,197.3C96,203,192,213,288,229.3C384,245,480,267,576,250.7C672,235,768,181,864,181.3C960,181,1056,235,1152,234.7C1248,235,1344,181,1392,154.7L1440,128L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"></path></svg>') repeat-x;
  background-size: 1440px 100px;
  animation: wave 10s linear infinite;
}

.wave:nth-child(2) {
  bottom: 10px;
  opacity: 0.5;
  animation: wave 7s linear infinite;
}

.wave:nth-child(3) {
  bottom: 20px;
  opacity: 0.2;
  animation: wave 4s linear infinite;
}

@keyframes wave {
  0% {
    transform: translateX(0);
  }
  100% {
    transform: translateX(-1440px);
  }
}

.login-card {
  width: 420px;
  padding: 40px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  animation: cardFloat 6s ease-in-out infinite;
}

@keyframes cardFloat {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.header-section {
  text-align: center;
  margin-bottom: 40px;
}

.system-title {
  font-size: 36px;
  color: #409EFF;
  font-weight: bold;
  margin-bottom: 16px;
  letter-spacing: 1px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
}

.login-title {
  font-size: 24px;
  color: #2c3e50;
  font-weight: 500;
  margin: 0;
}

.login-form {
  margin-top: 20px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  height: 44px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.button-container {
  margin-top: 30px;
}

.submit-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  border-radius: 8px;
  background: linear-gradient(120deg, #409EFF, #3a8ee6);
  border: none;
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.operation-links {
  display: flex;
  justify-content: space-between;
  margin: 20px 0;
  padding: 0 10px;
}

.divider {
  position: relative;
  text-align: center;
  margin: 30px 0;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 35%;
  height: 1px;
  background-color: #e0e0e0;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider-text {
  background-color: #fff;
  padding: 0 15px;
  color: #909399;
  font-size: 14px;
}

.third-party-login {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 20px;
}

.third-party-button {
  width: 44px;
  height: 44px;
  padding: 0;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.third-party-button:hover {
  transform: translateY(-2px);
  background-color: #f5f7fa;
}

.third-party-button i {
  font-size: 20px;
}

.icon-QQ {
  color: #12B7F5;
}

.icon-weixin {
  color: #07C160;
}
</style>
