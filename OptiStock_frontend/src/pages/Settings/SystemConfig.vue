<script setup>
import { ref } from 'vue'
import { Setting, Tools, Bell, Message, Lock, Link } from '@element-plus/icons-vue'

const systemInfo = ref({
  siteName: 'OptiStock 仓储管理系统',
  version: 'v2.0.0', 
  copyright: '© 2024 OptiStock 版权所有',
  author: '俞云烽',
  contact: '15968588744@163.com',
  description: 'OptiStock 是一个现代化的仓储管理系统，采用 Vue 3 + Spring Boot 技术栈开发，提供库存管理、商品管理、库存预警等核心功能。系统架构采用前后端分离设计，具有高性能、高可用、易扩展等特点。',
  license: 'MIT License',
  github: 'https://github.com/Auroral0810/OptiStock',
  techs: [
    'Vue 3',
    'Spring Boot', 
    'Redis',
    'Element Plus',
    'JWT',
    '阿里云OSS',
    'MyBatis-Plus'
  ],
  links: [
    {
      name: 'GitHub',
      url: 'https://github.com/Auroral0810/OptiStock',
      icon: 'https://github.githubassets.com/favicons/favicon.svg',
      color: '#24292e'
    },
    {
      name: 'CSDN博客',
      url: 'https://blog.csdn.net/Luck_ff?type=blog',
      icon: 'https://g.csdnimg.cn/static/logo/favicon32.ico', 
      color: '#fc5531'
    },
    {
      name: '掘金主页',
      url: 'https://juejin.cn/user/3481490045549488',
      icon: 'https://lf3-cdn-tos.bytescm.com/obj/static/xitu_juejin_web//static/favicons/favicon-32x32.png',
      color: '#1e80ff'
    }
  ]
})

const openLink = (url) => {
  window.open(url, '_blank')
}
</script>

<template>
  <div class="system-config-container">
    <div class="config-card">
      <div class="header">
        <div class="title-section">
          <h1 class="gradient-text">关于我们</h1>
          <p class="subtitle">查看系统基本信息与技术栈说明</p>
        </div>
        <div class="icon-section">
          <el-icon :size="24" color="#409EFF"><Setting /></el-icon>
        </div>
      </div>

      <!-- 友情链接区域 -->
      <div class="quick-links">
        <el-card v-for="link in systemInfo.links" 
                :key="link.name" 
                class="link-card"
                shadow="hover"
                :style="{ borderTop: `3px solid ${link.color}` }"
                @click="openLink(link.url)">
          <div class="link-content">
            <img :src="link.icon" :alt="link.name" class="link-icon">
            <span class="link-name" :style="{ color: link.color }">{{ link.name }}</span>
          </div>
        </el-card>
      </div>

      <el-form 
        :model="systemInfo"
        label-position="left"
        label-width="120px"
        class="config-form"
        disabled>
        
        <el-divider content-position="left">
          <el-icon class="divider-icon"><Tools /></el-icon>
          基本信息
        </el-divider>

        <el-form-item label="系统名称">
          <el-input v-model="systemInfo.siteName" readonly class="custom-input"/>
        </el-form-item>

        <el-form-item label="当前版本">
          <el-input v-model="systemInfo.version" readonly class="custom-input"/>
        </el-form-item>

        <el-form-item label="系统说明">
          <el-input 
            v-model="systemInfo.description"
            type="textarea"
            readonly
            resize="none"
            :rows="3"
            class="custom-textarea"/>
        </el-form-item>

        <el-divider content-position="left">
          <el-icon class="divider-icon"><Bell /></el-icon>
          版权信息
        </el-divider>

        <el-form-item label="版权声明">
          <el-input v-model="systemInfo.copyright" readonly class="custom-input"/>
        </el-form-item>

        <el-form-item label="开源协议">
          <el-input v-model="systemInfo.license" readonly class="custom-input"/>
        </el-form-item>

        <el-divider content-position="left">
          <el-icon class="divider-icon"><Message /></el-icon>
          联系方式
        </el-divider>

        <el-form-item label="作者">
          <el-input v-model="systemInfo.author" readonly class="custom-input"/>
        </el-form-item>

        <el-form-item label="联系邮箱">
          <el-input v-model="systemInfo.contact" readonly class="custom-input"/>
        </el-form-item>
        <el-form-item label="项目地址">
          <el-link 
            type="primary"
            :href="systemInfo.github"
            target="_blank"
            class="github-link">
            <el-icon><Link /></el-icon>
            {{ systemInfo.github }}
          </el-link>
        </el-form-item>
        
        <el-divider content-position="left">
          <el-icon class="divider-icon"><Lock /></el-icon>
          技术栈
        </el-divider>

        <el-form-item>
          <el-tag
            v-for="(tech, index) in systemInfo.techs"
            :key="tech"
            class="tech-tag"
            :type="['', 'success', 'warning', 'danger', 'info'][index % 5]"
            effect="light">
            {{ tech }}
          </el-tag>
        </el-form-item>

      </el-form>
    </div>
  </div>
</template>

<style scoped>
.system-config-container {
  padding: 16px;
  background-color: #f5f7fa;
  height: 100vh;
  overflow: hidden;
}

.config-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  padding: 20px;
  height: calc(100% - 40px);
  overflow-y: auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.gradient-text {
  margin: 0;
  font-size: 24px;
  background: linear-gradient(45deg, #409EFF, #36D1DC);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  margin: 8px 0 0;
  font-size: 14px;
  color: #909399;
}

.quick-links {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  justify-content: center;
}

.link-card {
  width: 200px;
  cursor: pointer;
  transition: all 0.3s;
}

.link-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
}

.link-content {
  display: flex;
  align-items: center;
  gap: 10px;
}

.link-icon {
  width: 24px;
  height: 24px;
}

.link-name {
  font-size: 16px;
  font-weight: 500;
}

.config-form {
  max-width: 800px;
  margin: 0 auto;
}

.el-divider {
  margin: 22px 0;
}

:deep(.el-divider__text) {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #409EFF;
  font-weight: bold;
}

.divider-icon {
  color: #409EFF;
  font-size: 18px;
}

.el-form-item {
  margin-bottom: 22px;
}

.custom-input {
  :deep(.el-input__wrapper) {
    background-color: #f8f9fa;
  }
}

.custom-textarea {
  :deep(.el-textarea__inner) {
    background-color: #f8f9fa;
  }
}

.tech-tag {
  margin-right: 6px;
  margin-bottom: 6px;
  padding: 0 10px;
  height: 28px;
  line-height: 26px;
  font-size: 13px;
  transition: all 0.3s;
}

.tech-tag:hover {
  transform: scale(1.05);
}

.github-link {
  text-decoration: none;
}

.github-button {
  display: flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(45deg, #409EFF, #36D1DC);
  border: none;
  transition: all 0.3s;
  cursor: pointer;
}

.github-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64,158,255,0.3);
}
</style>
