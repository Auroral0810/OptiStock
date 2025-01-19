import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server:{
    proxy:{
      '/api':{//获取路径中包含了/API的请求
        target:'http://localhost:9092',//后台服务所在的源
        changeOrigin:true,//修改源
        rewrite:(path)=>path.replace(/^\/api/,'')
      }
    }
  }
})
