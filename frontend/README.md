# 产品管理前端

基于Vue.js 3 + Element Plus的产品管理前端系统。

## 功能特性
- 产品列表展示
- 产品添加、编辑、删除
- 产品搜索和筛选
- 响应式设计

## 技术栈
- Vue.js 3
- Vue Router 4
- Vuex 4
- Element Plus
- Axios

## 项目结构
```
frontend/
├── public/              # 静态资源
├── src/
│   ├── components/     # 组件
│   ├── views/         # 页面视图
│   ├── router/        # 路由配置
│   ├── store/         # 状态管理
│   ├── api/           # API接口
│   ├── App.vue        # 根组件
│   └── main.js        # 入口文件
├── package.json       # 依赖配置
└── README.md         # 说明文档
```

## 快速开始

### 安装依赖
```bash
npm install
```

### 开发运行
```bash
npm run serve
```

### 构建生产版本
```bash
npm run build
```

## 后端API
前端需要连接后端Spring Boot服务，默认运行在 `http://localhost:8082`

## 开发说明
1. 确保后端服务已启动
2. 运行 `npm run serve` 启动前端开发服务器
3. 访问 `http://localhost:8080` 查看应用

## 许可证
MIT License
