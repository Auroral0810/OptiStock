我帮你写一个完整的 README.md：

```markdown
# OptiStock 仓储管理系统

OptiStock 是一个现代化的仓储管理系统，采用 Vue 3 + Spring Boot 技术栈开发，提供库存管理、商品管理、库存预警等核心功能。

## 功能特性

### 用户管理
- 用户登录/注册
- 个人信息管理
- 头像上传（阿里云 OSS）
- 密码重置

### 商品管理
- 商品分类管理
- 商品信息维护
- SKU 管理
- 批量导入/导出

### 库存管理
- 实时库存监控
- 库存预警设置
- 库存变动记录
- 数据可视化展示

### 系统管理
- 用户权限控制
- 操作日志记录
- 系统配置管理

## 技术栈

### 前端技术
- Vue 3
- Vite
- Element Plus
- Pinia 状态管理
- Vue Router
- ECharts 图表
- Axios HTTP 客户端

### 后端技术
- Spring Boot 3.1.3
- MyBatis-Plus 3.5.3.1
- MySQL 8.0
- Redis
- JWT 认证
- 阿里云 OSS
- Lombok

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+

### 后端配置
1. 克隆项目
```bash
git clone https://github.com/YourUsername/OptiStock.git
```src/pages/Purchase/PurchaseSupplier.vue

2. 配置数据库
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/optistock?serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

3. 配置 Redis
```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

4. 配置阿里云 OSS（可选）
```yaml
aliyun:
  oss:
    endpoint: your-endpoint
    accessKeyId: your-access-key-id
    accessKeySecret: your-access-key-secret
    bucketName: your-bucket-name
```

5. 运行后端项目
```bash
mvn spring-boot:run
```

### 前端配置
1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

4. 构建生产版本
```bash
npm run build
```

## 项目结构
```
OptiStock/
├── backend/                # 后端项目目录
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   └── pom.xml
│
└── frontend/              # 前端项目目录
    ├── src/
    │   ├── api/          # API 接口
    │   ├── assets/       # 静态资源
    │   ├── components/   # 组件
    │   ├── router/       # 路由配置
    │   ├── stores/       # Pinia 状态管理
    │   └── views/        # 页面视图
    ├── package.json
    └── vite.config.js
```

## API 文档
API 文档通过 Swagger UI 提供，启动后端服务后访问：
```
http://localhost:9092/swagger-ui/index.html
```

## 贡献指南
欢迎提交 Issue 和 Pull Request 来帮助改进项目。

## 许可证
本项目采用 MIT 许可证。

## 联系方式
- 作者：俞云烽
- 邮箱：15968588744@163.com
- GitHub：[[Your GitHub Profile]](https://github.com/Auroral0810/OptiStock)
```
