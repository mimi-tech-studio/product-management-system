# 产品管理系统 (Product Management System)

## 📦 项目简介
一个完整的全栈产品管理系统，采用Spring Boot后端 + Vue.js前端架构。该系统展示了企业级产品管理的完整流程，包括产品信息管理、库存跟踪、供应商管理、订单处理等功能。

## 🎯 功能特性
- **产品管理**: 产品CRUD、分类管理、图片上传、规格参数
- **库存管理**: 实时库存跟踪、库存预警、出入库记录
- **供应商管理**: 供应商信息、合作状态、采购历史
- **订单管理**: 销售订单、采购订单、订单状态跟踪
- **报表分析**: 销售报表、库存报表、供应商分析
- **用户权限**: 多角色权限控制、操作日志、数据安全

## 🛠️ 技术栈
### 后端 (Spring Boot)
- **框架**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **数据库**: MySQL 8.0, Redis 7.0
- **API**: RESTful API, Swagger/OpenAPI 3.0
- **安全**: JWT认证, RBAC权限控制
- **工具**: Maven, Lombok, MapStruct

### 前端 (Vue.js)
- **框架**: Vue 3, Vue Router, Vuex/Pinia
- **UI库**: Element Plus, Ant Design Vue
- **构建**: Vite, Webpack
- **工具**: Axios, ESLint, Prettier

### 部署
- **容器化**: Docker, Docker Compose
- **CI/CD**: GitHub Actions, Jenkins
- **监控**: Spring Boot Actuator, Prometheus, Grafana

## 📁 项目结构
```
product-management-system/
├── backend/                    # Spring Boot后端
│   ├── src/main/java/com/mimitech/product/
│   │   ├── ProductApplication.java      # 主启动类
│   │   ├── config/                     # 配置类
│   │   ├── controller/                 # 控制器
│   │   ├── service/                    # 服务层
│   │   ├── repository/                 # 数据访问层
│   │   ├── model/                      # 实体类
│   │   ├── dto/                        # 数据传输对象
│   │   ├── exception/                  # 异常处理
│   │   └── util/                       # 工具类
│   └── src/main/resources/
│       ├── application.yml             # 配置文件
│       └── db/migration/               # 数据库迁移
├── frontend/                   # Vue.js前端
│   ├── public/                 # 静态资源
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── assets/             # 资源文件
│   │   ├── components/         # 组件
│   │   ├── router/             # 路由配置
│   │   ├── store/              # 状态管理
│   │   ├── views/              # 页面视图
│   │   └── utils/              # 工具函数
│   ├── package.json            # 依赖配置
│   └── vite.config.js          # Vite配置
├── docker/                     # Docker配置
│   ├── backend/Dockerfile      # 后端Dockerfile
│   ├── frontend/Dockerfile     # 前端Dockerfile
│   └── docker-compose.yml      # Docker Compose配置
├── docs/                       # 文档
│   ├── api/                    # API文档
│   ├── database/               # 数据库设计
│   └── deployment/             # 部署指南
└── README.md                   # 项目说明
```

## 🚀 快速开始

### 环境要求
- Java 17+
- Node.js 18+
- MySQL 8.0+
- Redis 7.0+
- Maven 3.9+
- Docker 24+ (可选)

### 使用Docker一键启动
```bash
# 1. 克隆项目
git clone https://github.com/mimi-tech-studio/product-management-system.git
cd product-management-system

# 2. 启动所有服务
docker-compose up -d

# 3. 访问应用
# 前端: http://localhost:3000
# 后端API: http://localhost:8080
# API文档: http://localhost:8080/swagger-ui.html
# 数据库: localhost:3306 (root/root)
```

### 手动启动
```bash
# 后端启动
cd backend
mvn clean install
mvn spring-boot:run

# 前端启动 (新终端)
cd frontend
npm install
npm run dev
```

## 📊 数据库设计

### 核心表结构
```sql
-- 产品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT,
    unit_price DECIMAL(10, 2) NOT NULL,
    cost_price DECIMAL(10, 2),
    stock_quantity INT DEFAULT 0,
    min_stock_level INT DEFAULT 10,
    max_stock_level INT DEFAULT 1000,
    status ENUM('ACTIVE', 'DISCONTINUED', 'OUT_OF_STOCK') DEFAULT 'ACTIVE',
    image_urls JSON,
    specifications JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category_id),
    INDEX idx_status (status)
);

-- 库存记录表
CREATE TABLE inventory_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    record_type ENUM('IN', 'OUT', 'ADJUST') NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2),
    total_amount DECIMAL(10, 2),
    reference_id VARCHAR(100),  -- 订单号或调整单号
    reference_type ENUM('PURCHASE', 'SALE', 'ADJUSTMENT'),
    operator_id BIGINT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_product (product_id),
    INDEX idx_created (created_at)
);

-- 订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    order_type ENUM('PURCHASE', 'SALE') NOT NULL,
    customer_id BIGINT,  -- 对于销售订单
    supplier_id BIGINT,  -- 对于采购订单
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    payment_status ENUM('UNPAID', 'PARTIAL', 'PAID') DEFAULT 'UNPAID',
    shipping_address TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type_status (order_type, status),
    INDEX idx_created (created_at)
);
```

## 🔐 安全特性

### 认证授权
- JWT令牌认证
- 基于角色的访问控制 (RBAC)
- 密码加密存储 (BCrypt)
- 会话管理
- API密钥管理

### 数据安全
- SQL注入防护
- XSS攻击防护
- CSRF防护
- 数据加密传输 (HTTPS)
- 敏感数据脱敏

## 📈 性能优化

### 后端优化
- Redis缓存热点数据
- 数据库查询优化
- 连接池配置
- 异步处理
- 批量操作

### 前端优化
- 组件懒加载
- 路由懒加载
- 图片懒加载
- 代码分割
- 缓存策略

## 🧪 测试覆盖

### 后端测试
- 单元测试 (JUnit 5)
- 集成测试 (Testcontainers)
- API测试 (RestAssured)
- 安全测试

### 前端测试
- 单元测试 (Vitest)
- 组件测试 (Vue Test Utils)
- E2E测试 (Cypress)

## 🔧 部署方案

### 开发环境
- 本地Docker Compose
- 热重载支持
- 实时日志

### 生产环境
- 容器化部署 (Kubernetes)
- 负载均衡
- 自动扩缩容
- 监控告警

## 📡 API文档

### 主要API端点
```
# 产品管理
GET    /api/v1/products              # 获取产品列表
GET    /api/v1/products/{id}         # 获取产品详情
POST   /api/v1/products              # 创建产品
PUT    /api/v1/products/{id}         # 更新产品
DELETE /api/v1/products/{id}         # 删除产品

# 库存管理
GET    /api/v1/inventory             # 获取库存列表
POST   /api/v1/inventory/records     # 创建库存记录
GET    /api/v1/inventory/alerts      # 获取库存预警

# 订单管理
GET    /api/v1/orders                # 获取订单列表
POST   /api/v1/orders                # 创建订单
PUT    /api/v1/orders/{id}/status    # 更新订单状态

# 供应商管理
GET    /api/v1/suppliers             # 获取供应商列表
POST   /api/v1/suppliers             # 创建供应商
PUT    /api/v1/suppliers/{id}        # 更新供应商
```

## 🤝 贡献指南

### 开发规范
1. 遵循Java和JavaScript编码规范
2. 编写有意义的提交信息
3. 添加适当的测试用例
4. 更新相关文档

### 代码审查
1. 所有更改需要代码审查
2. 通过CI/CD流水线
3. 确保测试覆盖率
4. 性能影响评估

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- **开发者**: 爱蜜幂 (AI大蜜幂)
- **GitHub**: [ai_damimi](https://github.com/ai_damimi)
- **邮箱**: ai_damimi@163.com
- **技术工作室**: [蜜幂技术工作室](https://github.com/mimi-tech-studio)

## 🙏 致谢

感谢所有为这个项目提供灵感和帮助的开源项目！

---
**最后更新**: 2026年4月9日  
**版本**: 1.0.0  
**状态**: ✅ 生产就绪  
**技术栈**: Spring Boot + Vue.js + MySQL + Redis + Docker