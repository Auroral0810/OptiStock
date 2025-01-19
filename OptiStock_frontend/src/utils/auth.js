import { defineStore } from 'pinia'

// 定义 Auth Store
export const useAuthStore = defineStore('auth', {
    state: () => ({
        userToken: localStorage.getItem('userToken') || null, // 从 localStorage 读取 Token
        userInfo: JSON.parse(localStorage.getItem('userInfo')) || null // 存储用户信息
    }),

    actions: {
        // 用户登录
        login(token, userInfo) {
            this.userToken = token
            this.userInfo = userInfo

            // 存储到 localStorage 实现持久化
            localStorage.setItem('userToken', token)
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
        },

        // 用户登出
        logout() {
            this.userToken = null
            this.userInfo = null

            // 清除 localStorage
            localStorage.removeItem('userToken')
            localStorage.removeItem('userInfo')
        },
        // 更新用户信息
        updateUserInfo(userInfo) {
            this.userInfo = userInfo
            localStorage.setItem('userInfo', JSON.stringify(userInfo))
        }
    }
}, { persist: true })