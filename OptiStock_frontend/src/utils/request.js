import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import { useAuthStore } from '@/utils/auth'

// 创建axios实例
const service = axios.create({
  baseURL: '/api', // 修改为 /api
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore() // 获取 Pinia Store
    if (authStore.userToken) {
      config.headers['Authorization'] = 'Bearer ' + authStore.userToken
    }

    // 处理 GET 请求参数（格式化对象参数）
    if (config.method === 'get' && config.params) {
      let url = config.url + '?'
      for (const key of Object.keys(config.params)) {
        const value = config.params[key]
        if (value !== null && value !== undefined) {
          url += `${encodeURIComponent(key)}=${encodeURIComponent(value)}&`
        }
      }
      config.url = url.slice(0, -1) // 去掉最后的 `&`
      config.params = {}
    }
    //显示我的请求
    console.log(config)
    return config
  },
  (error) => {
    console.error('请求拦截错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) { // 按你的后端格式，成功应该是 `code: 200`
      ElMessage.error(res.msg || '系统错误')

      if (res.code === 401) { // Token 失效
        const authStore = useAuthStore()
        
        // 弹出重新登录提示
        ElMessageBox.confirm(
          '登录状态已过期，请重新登录',
          '系统提示',
          {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          authStore.logout() // 清空 Pinia 状态
          router.push('/login')
        }).catch(() => {
          console.log('用户取消了重新登录')
        })
      }
      return Promise.reject(res)
    }
    return res
  },
  (error) => {
    console.error('响应拦截错误:', error)
    let errorMessage = '请求失败'

    if (error.message.includes('Network Error')) {
      errorMessage = '网络连接异常，请检查后端服务是否启动'
    } else if (error.message.includes('timeout')) {
      errorMessage = '系统接口请求超时'
    } else if (error.message.includes('Request failed with status code')) {
      errorMessage = `系统接口 ${error.message.slice(-3)} 异常`
    }

    ElMessage.error(errorMessage)
    return Promise.reject(error)
  }
)

export default service
