// collapse
import { ElCollapseTransition } from 'element-plus'
// fade/zoom
import '/node_modules/element-plus/theme-chalk/base.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import App from './App.vue'
import router from './router'
import '/node_modules/element-plus/dist/index.css'
import locale from 'element-plus/dist/locale/zh-cn.js'
const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
app.component(ElCollapseTransition.name, ElCollapseTransition, { locale: locale })
