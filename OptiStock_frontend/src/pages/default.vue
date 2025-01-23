<script setup>
import { ref, onMounted } from 'vue'
import { Setting, Tools, DataAnalysis, Document, TrendCharts, Goods, Warning, Money, List } from '@element-plus/icons-vue'
import { getDashboardData } from '@/api/statistics'
import gsap from 'gsap'

const features = ref([
  {
    icon: Goods,
    title: '商品管理',
    desc: '智能化商品分类与标签管理,多维度商品画像分析',
    color: '#6366F1'
  },
  {
    icon: List,
    title: '订单管理', 
    desc: '一体化订单处理流程,智能订单分发与追踪系统',
    color: '#06B6D4'
  },
  {
    icon: Money,
    title: '采购管理',
    desc: '智能供应链优化,自动化采购决策支持系统',
    color: '#10B981'
  },
  {
    icon: DataAnalysis,
    title: '数据可视化',
    desc: '多维数据深度分析,智能经营决策辅助平台',
    color: '#F59E0B'
  }
])

const statistics = ref({})

const welcomeText = ref('')
const targetText = 'Welcome to OptiStock'
let currentIndex = 0
const showCursor = ref(true)

const typeText = () => {
  welcomeText.value = ''
  currentIndex = 0
  showCursor.value = true
  typeNextChar()
}

const typeNextChar = () => {
  if (currentIndex < targetText.length) {
    welcomeText.value += targetText[currentIndex]
    currentIndex++
    setTimeout(typeNextChar, 100)
  } else {
    showCursor.value = false
  }
}

const animateNumbers = () => {
  const stats = document.querySelectorAll('.stat-value')
  stats.forEach(stat => {
    const value = stat.getAttribute('data-value')
    if (value.startsWith('¥')) {
      // 处理金额数字
      const numericValue = parseFloat(value.replace(/[¥,]/g, ''))
      const obj = { value: 0 }
      gsap.to(obj, {
        value: numericValue,
        duration: 2,
        ease: 'power1.out',
        onUpdate: () => {
          stat.innerHTML = `¥${Math.round(obj.value).toLocaleString('zh-CN')}`
        }
      })
    } else {
      // 处理普通数字
      const numericValue = parseInt(value.replace(/,/g, ''))
      const obj = { value: 0 }
      gsap.to(obj, {
        value: numericValue,
        duration: 2,
        ease: 'power1.out',
        onUpdate: () => {
          stat.innerHTML = Math.round(obj.value).toLocaleString('zh-CN')
        }
      })
    }
  })
}

const reloadAnimations = () => {
  typeText()
  animateNumbers()
}

onMounted(() => {
  loadDashboardData()
  reloadAnimations()
  
  // 每15秒重新加载一次动画
  setInterval(() => {loadDashboardData(); reloadAnimations()}, 15000)
})

// 后端获取数据
const loadDashboardData = async () => {
  try {
    const data = await getDashboardData()
    if(data.code === 200) {
      statistics.value = {
        totalProducts: data.data.productCount.toLocaleString('zh-CN'),
        lowStock: data.data.warningProductCount.toLocaleString('zh-CN'),
        monthlyRevenue: `¥${data.data.monthlyRevenue.toLocaleString('zh-CN')}`,
        orderCount: data.data.orderCount.toLocaleString('zh-CN')
      }
    }
    reloadAnimations()
  } catch (error) {
    console.error('加载数据失败:', error)
    statistics.value = {
      totalProducts: '0',
      lowStock: '0',
      monthlyRevenue: '¥0',
      orderCount: '0'
    }
  }
}
</script>

<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-header">
        <div class="welcome-content">
          <h1 class="welcome-title">
            <span class="welcome-text">{{ welcomeText }}</span>
            <span class="cursor" v-show="showCursor">|</span>
          </h1>
          <p class="welcome-desc">智能化库存管理系统 · 让企业管理更简单</p>
        </div>
        <div class="stats-container">
          <div class="stat-item" v-for="(value, key) in statistics" :key="key">
            <div class="stat-icon" :class="key">
              <component :is="
                key === 'totalProducts' ? Goods :
                key === 'lowStock' ? Warning :
                key === 'monthlyRevenue' ? Money : List
              "/>
            </div>
            <div class="stat-content">
              <div class="stat-value" :data-value="value">{{ value }}</div>
              <div class="stat-label">
                {{ key === 'totalProducts' ? '商品总数' :
                   key === 'lowStock' ? '低库存预警' :
                   key === 'monthlyRevenue' ? '月度营收' : '订单数量' 
                }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能特性区域 -->
    <div class="features-section">
      <h2 class="section-title">核心功能</h2>
      <div class="features-grid">
        <div 
          v-for="(feature, index) in features" 
          :key="index"
          class="feature-card"
        >
          <div class="feature-content">
            <div class="feature-icon" :style="{ backgroundColor: `${feature.color}15` }">
              <component :is="feature.icon" :style="{ color: feature.color }" class="icon-size"/>
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.desc }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 系统状态区域 -->
    <div class="system-status">
      <div class="status-card">
        <div class="status-header">
          <h3 class="status-title">系统状态</h3>
          <el-tag type="success" effect="dark" class="status-tag">运行正常</el-tag>
        </div>
        <div class="status-grid">
          <div class="status-item" v-for="(item, index) in 3" :key="index">
            <div class="status-icon" :class="index === 0 ? 'primary' : index === 1 ? 'success' : 'warning'">
              <component :is="index === 0 ? Setting : index === 1 ? Tools : DataAnalysis"/>
            </div>
            <div class="status-info">
              <div class="status-label">
                {{ index === 0 ? '系统版本' : index === 1 ? '服务器状态' : '数据同步' }}
              </div>
              <template v-if="index === 0">
                <div class="status-value">OptiStock v1.0.0</div>
              </template>
              <template v-else>
                <el-progress 
                  :percentage="index === 1 ? 95 : 100"
                  :status="index === 1 ? 'success' : 'warning'"
                  :stroke-width="8"
                  :show-text="false"
                  class="status-progress"
                />
              </template>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  background: #f8fafc;
  padding: 2rem;
}

.welcome-section {
  margin-bottom: 4rem;
}

.welcome-header {
  background: linear-gradient(135deg, #1e1b4b, #312e81);
  border-radius: 2rem;
  padding: 4rem 2rem;
  position: relative;
  overflow: hidden;
  box-shadow: 
    0 25px 50px -12px rgba(0, 0, 0, 0.25),
    0 0 0 1px rgba(255, 255, 255, 0.1) inset;
}

.welcome-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 30%, rgba(99, 102, 241, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 70%, rgba(6, 182, 212, 0.15) 0%, transparent 50%);
  animation: bgPulse 10s ease-in-out infinite;
  z-index: 0;
}

.welcome-content {
  position: relative;
  z-index: 1;
  text-align: center;
  margin-bottom: 3rem;
}

.welcome-title {
  font-size: 4rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
  line-height: 1.2;
}

.welcome-text {
  background: linear-gradient(to right, #fff, #94a3b8);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.welcome-desc {
  font-size: 1.5rem;
  color: #94a3b8;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  padding: 0 2rem;
  position: relative;
  z-index: 1;
}

.stat-item {
  background: rgba(255, 255, 255, 0.03);
  border-radius: 1.5rem;
  padding: 2rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.05);
  border-color: rgba(255, 255, 255, 0.2);
}

.stat-icon {
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.stat-icon.totalProducts { background: #6366F115; color: #6366F1; }
.stat-icon.lowStock { background: #F43F5E15; color: #F43F5E; }
.stat-icon.monthlyRevenue { background: #10B98115; color: #10B981; }
.stat-icon.orderCount { background: #F59E0B15; color: #F59E0B; }

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 2rem;
  font-weight: 700;
  color: #fff;
  margin-bottom: 0.5rem;
  line-height: 1;
  font-variant-numeric: tabular-nums;  /* 确保数字等宽，避免跳动 */
}

.stat-label {
  font-size: 0.875rem;
  color: #94a3b8;
}

.features-section {
  padding: 2rem;
  margin: 2rem auto;
  max-width: 1200px;
}

.section-title {
  text-align: center;
  font-size: 1.75rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 2rem;
  position: relative;
  padding-bottom: 1rem;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: #6366F1;
  border-radius: 3px;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-top: 2rem;
}

.feature-card {
  position: relative;
  background: #ffffff;
  border-radius: 1rem;
  border: 1px solid #e2e8f0;
  height: 100%;
  min-height: 200px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.feature-content {
  padding: 1.5rem;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.feature-icon {
  width: 3.5rem;
  height: 3.5rem;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1rem;
  background: #f8fafc;
}

.icon-size {
  width: 1.75rem;
  height: 1.75rem;
}

.feature-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin-bottom: 0.75rem;
}

.feature-desc {
  color: #64748b;
  line-height: 1.6;
  font-size: 0.875rem;
  margin: 0;
}

.system-status {
  max-width: 1200px;
  margin: 0 auto;
}

.status-card {
  background: white;
  border-radius: 1.5rem;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid #e2e8f0;
}

.status-header {
  padding: 1.5rem 2rem;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.status-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e293b;
  margin: 0;
}

.status-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  padding: 2rem;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.status-icon {
  width: 3rem;
  height: 3rem;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
}

.status-info {
  flex: 1;
}

.status-progress {
  height: 0.5rem !important;
  border-radius: 0.25rem;
}

:deep(.el-progress-bar__outer) {
  background-color: #f1f5f9 !important;
}

:deep(.el-progress-bar__inner) {
  transition: width 0.8s ease !important;
}

@media (max-width: 1024px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 1rem;
  }
}

@media (max-width: 640px) {
  .features-grid {
    grid-template-columns: 1fr;
  }
  
  .features-section {
    padding: 1rem;
  }
  
  .feature-card {
    min-height: 180px;
  }
}

.cursor {
  display: inline-block;
  animation: blink 1s infinite;
  color: #fff;
  -webkit-text-fill-color: #fff;
}

@keyframes blink {
  0%, 100% { opacity: 1; }
  50% { opacity: 0; }
}

/* 确保图标大小一致 */
.stat-icon :deep(svg),
.status-icon :deep(svg),
.feature-icon :deep(svg) {
  width: 1.75rem;
  height: 1.75rem;
}
</style>