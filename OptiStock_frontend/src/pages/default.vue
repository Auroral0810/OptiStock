<template>
  <div class="dashboard-container">
    <!-- 顶部数据卡片 -->
    <div class="card-section">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="hover" class="data-card">
            <div class="card-content">
              <div class="card-icon sales-icon">
                <el-icon><Money /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">总销售额</div>
                <div class="card-number">¥ {{ salesData.total }}</div>
                <div class="card-trend">
                  <span :class="['trend-value', salesData.growth >= 0 ? 'up' : 'down']">
                    {{ salesData.growth >= 0 ? '↑' : '↓' }} {{ Math.abs(salesData.growth) }}%
                  </span>
                  <span class="trend-label">同比增长</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="hover" class="data-card">
            <div class="card-content">
              <div class="card-icon order-icon">
                <el-icon><List /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">订单总量</div>
                <div class="card-number">{{ orderData.total }}</div>
                <div class="card-trend">
                  <span :class="['trend-value', orderData.growth >= 0 ? 'up' : 'down']">
                    {{ orderData.growth >= 0 ? '↑' : '↓' }} {{ Math.abs(orderData.growth) }}%
                  </span>
                  <span class="trend-label">同比增长</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="hover" class="data-card">
            <div class="card-content">
              <div class="card-icon product-icon">
                <el-icon><Goods /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">商品总量</div>
                <div class="card-number">{{ productData.total }}</div>
                <div class="card-trend">
                  <span :class="['trend-value', productData.growth >= 0 ? 'up' : 'down']">
                    {{ productData.growth >= 0 ? '↑' : '↓' }} {{ Math.abs(productData.growth) }}%
                  </span>
                  <span class="trend-label">同比增长</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :xs="24" :sm="12" :md="6">
          <el-card shadow="hover" class="data-card">
            <div class="card-content">
              <div class="card-icon user-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="card-info">
                <div class="card-title">用户总量</div>
                <div class="card-number">{{ userData.total }}</div>
                <div class="card-trend">
                  <span :class="['trend-value', userData.growth >= 0 ? 'up' : 'down']">
                    {{ userData.growth >= 0 ? '↑' : '↓' }} {{ Math.abs(userData.growth) }}%
                  </span>
                  <span class="trend-label">同比增长</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 图表区域 -->
    <div class="chart-section">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">销售趋势</h3>
              <el-radio-group v-model="timeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
            <div ref="salesChart" class="sales-chart"></div>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">商品分类占比</h3>
            </div>
            <div ref="categoryChart" class="category-chart"></div>
          </el-card>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="mt-20">
        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">热销商品排行</h3>
            </div>
            <div ref="hotProductsChart" class="hot-products-chart"></div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card shadow="hover" class="chart-card">
            <div class="chart-header">
              <h3 class="chart-title">库存预警</h3>
            </div>
            <div ref="stockWarningChart" class="stock-warning-chart"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import * as echarts from 'echarts'
import { Money, List, Goods, User } from '@element-plus/icons-vue'

// 数据状态
const salesData = reactive({
  total: '126,560',
  growth: 12.3
})

const orderData = reactive({
  total: '1,234',
  growth: 8.5
})

const productData = reactive({
  total: '856',
  growth: -2.8
})

const userData = reactive({
  total: '3,256',
  growth: 15.4
})

// 图表相关
const timeRange = ref('month')
const salesChart = ref(null)
const categoryChart = ref(null)
const hotProductsChart = ref(null)
const stockWarningChart = ref(null)

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

// 图表初始化
const initCharts = () => {
  // 销售趋势图表
  const sChart = echarts.init(salesChart.value)
  sChart.setOption({
    grid: {
      top: 40,
      right: 20,
      bottom: 40,
      left: 60
    },
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.9)',
      borderColor: '#eee',
      borderWidth: 1,
      textStyle: {
        color: '#333'
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['1日', '5日', '10日', '15日', '20日', '25日', '30日'],
      axisLine: {
        lineStyle: {
          color: '#eee'
        }
      },
      axisTick: {
        show: false
      }
    },
    yAxis: {
      type: 'value',
      splitLine: {
        lineStyle: {
          color: '#eee'
        }
      }
    },
    series: [{
      data: [2200, 2900, 2800, 3500, 3200, 3700, 3600],
      type: 'line',
      smooth: true,
      symbolSize: 6,
      lineStyle: {
        width: 3,
        color: '#409EFF'
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [{
            offset: 0,
            color: 'rgba(64,158,255,0.2)'
          }, {
            offset: 1,
            color: 'rgba(64,158,255,0)'
          }]
        }
      }
    }]
  })

  // 商品分类图表
  const cChart = echarts.init(categoryChart.value)
  cChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      bottom: '5%',
      left: 'center',
      itemWidth: 10,
      itemHeight: 10
    },
    series: [{
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['50%', '40%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: false
      },
      data: [
        { value: 335, name: '服装' },
        { value: 310, name: '电子' },
        { value: 234, name: '食品' },
        { value: 135, name: '家居' },
        { value: 154, name: '其他' }
      ]
    }]
  })

  // 热销商品排行图表
  const hChart = echarts.init(hotProductsChart.value)
  hChart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'value',
      boundaryGap: [0, 0.01]
    },
    yAxis: {
      type: 'category',
      data: ['商品A', '商品B', '商品C', '商品D', '商品E']
    },
    series: [
      {
        type: 'bar',
        data: [1800, 1500, 1200, 1000, 800],
        itemStyle: {
          color: '#91cc75'
        }
      }
    ]
  })

  // 库存预警图表
  const wChart = echarts.init(stockWarningChart.value)
  wChart.setOption({
    tooltip: {
      trigger: 'item'
    },
    legend: {
      top: '5%',
      left: 'center'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '40',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 20, name: '库存充足' },
          { value: 15, name: '库存偏低' },
          { value: 5, name: '库存不足' }
        ]
      }
    ]
  })
}

// 窗口大小变化处理
const handleResize = () => {
  salesChart.value && echarts.getInstanceByDom(salesChart.value)?.resize()
  categoryChart.value && echarts.getInstanceByDom(categoryChart.value)?.resize()
  hotProductsChart.value && echarts.getInstanceByDom(hotProductsChart.value)?.resize()
  stockWarningChart.value && echarts.getInstanceByDom(stockWarningChart.value)?.resize()
}
</script>

<style scoped>
.dashboard-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.card-section {
  margin-bottom: 24px;
}

.data-card {
  background: #ffffff;
  border-radius: 8px;
  transition: all 0.3s;
}

.data-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.card-content {
  padding: 20px;
  display: flex;
  align-items: center;
}

.card-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 16px;
}

.card-icon :deep(.el-icon) {
  font-size: 24px;
  color: #fff;
}

.sales-icon {
  background: linear-gradient(135deg, #36d1dc, #5b86e5);
}

.order-icon {
  background: linear-gradient(135deg, #ff9a9e, #fad0c4);
}

.product-icon {
  background: linear-gradient(135deg, #a8edea, #fed6e3);
}

.user-icon {
  background: linear-gradient(135deg, #d299c2, #fef9d7);
}

.card-info {
  flex: 1;
}

.card-title {
  color: #909399;
  font-size: 14px;
  margin-bottom: 8px;
}

.card-number {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.card-trend {
  font-size: 13px;
}

.trend-value {
  margin-right: 8px;
  font-weight: 500;
}

.trend-value.up {
  color: #67c23a;
}

.trend-value.down {
  color: #f56c6c;
}

.trend-label {
  color: #909399;
}

.chart-section .el-card {
  margin-bottom: 24px;
  border-radius: 8px;
}

.chart-card {
  background: #ffffff;
  transition: all 0.3s;
}

.chart-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

.sales-chart {
  height: 350px;
  padding: 20px;
}

.category-chart {
  height: 350px;
  padding: 20px;
}

.hot-products-chart {
  height: 300px;
  padding: 20px;
}

.stock-warning-chart {
  height: 300px;
  padding: 20px;
}

.mt-20 {
  margin-top: 20px;
}
</style>