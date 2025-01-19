

-- ==============================================
--               商品管理系统 SQL 文件
-- ==============================================
use OptiStock;
-- ==============================================
--               1️⃣ 商品管理模块
-- ==============================================

CREATE TABLE `product` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `category_id` BIGINT NOT NULL,
    `sku` VARCHAR(50) UNIQUE NOT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `cost_price` DECIMAL(10,2) NOT NULL,
    `stock_quantity` INT DEFAULT 0,
    `unit` VARCHAR(20) DEFAULT '件',
    `status` ENUM('上架', '下架', '停产') DEFAULT '上架',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ==============================================
-- 商品分类表
-- ==============================================

CREATE TABLE `product_category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) UNIQUE NOT NULL,
    `parent_id` BIGINT DEFAULT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
--               2️⃣ 库存管理模块
-- ==============================================

CREATE TABLE `stock_in` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `supplier_id` BIGINT,
    `quantity` INT NOT NULL,
    `cost_price` DECIMAL(10,2) NOT NULL,
    `total_cost` DECIMAL(10,2) NOT NULL,
    `warehouse_id` BIGINT NOT NULL,
    `received_by` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `stock_out` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `warehouse_id` BIGINT NOT NULL,
    `shipped_by` BIGINT NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `supplier` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `contact` VARCHAR(100),
    `phone` VARCHAR(20),
    `email` VARCHAR(255),
    `address` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
--               3️⃣ 订单管理模块
-- ==============================================

CREATE TABLE `order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_number` VARCHAR(50) UNIQUE NOT NULL,
    `customer_id` BIGINT NOT NULL,
    `total_amount` DECIMAL(10,2) NOT NULL,
    `status` ENUM('待付款', '已付款', '待发货', '已发货', '已完成', '已取消') NOT NULL DEFAULT '待付款',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `order_items` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
--               4️⃣ 采购管理模块
-- ==============================================

CREATE TABLE `purchase_order` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `supplier_id` BIGINT NOT NULL,
    `total_cost` DECIMAL(10,2) NOT NULL,
    `status` ENUM('待审核', '已通过', '已驳回', '已完成') NOT NULL DEFAULT '待审核',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `purchase_items` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `purchase_order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `cost_price` DECIMAL(10,2) NOT NULL,
    `total_price` DECIMAL(10,2) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
--               5️⃣ 退货 & 损耗管理模块
-- ==============================================

CREATE TABLE `return_orders` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `return_reason` TEXT,
    `return_status` ENUM('待处理', '已完成') DEFAULT '待处理',
    `return_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `stock_adjustments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `adjustment_type` ENUM('损坏', '过期', '其他') NOT NULL,
    `quantity` INT NOT NULL,
    `remarks` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

