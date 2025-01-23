-- ==============================================
--               商品管理系统 SQL 文件
-- ==============================================
use
OptiStock;
drop table if exists `product`;
drop table if exists `product_category`;
drop table if exists `stock_in`;
drop table if exists `stock_out`;
drop table if exists `supplier`;
drop table if exists `order`;
drop table if exists `order_items`;
drop table if exists `purchase_order`;
drop table if exists `purchase_items`;
drop table if exists `return_orders`;
drop table if exists `stock_adjustments`;
-- ==============================================
--               1️⃣ 商品管理模块
-- ==============================================

CREATE TABLE `product`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品唯一标识符',
    `name`           VARCHAR(255)       NOT NULL COMMENT '商品名称',
    `category_id`    BIGINT             NOT NULL COMMENT '商品分类ID，关联product_category表',
    `sku`            VARCHAR(50) UNIQUE NOT NULL COMMENT '库存单位编号（SKU）',
    `price`          DECIMAL(10, 2)     NOT NULL COMMENT '商品售价',
    `cost_price`     DECIMAL(10, 2)     NOT NULL COMMENT '商品成本价',
    `stock_quantity` INT         DEFAULT 0 COMMENT '库存数量',
    `unit`           VARCHAR(20) DEFAULT '件' COMMENT '计量单位，如件、盒、瓶等',
    `status`         ENUM('上架', '下架', '停产') DEFAULT '上架' COMMENT '商品状态',
    `warning_threshold` DECIMAL(10,2) DEFAULT 0.00 COMMENT '库存预警阈值',
    `created_at`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '商品创建时间',
    `updated_at`     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '商品信息更新时间'
) COMMENT '商品信息表';

-- 商品分类表
CREATE TABLE `product_category`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类唯一标识符',
    `name`       VARCHAR(100) UNIQUE NOT NULL COMMENT '分类名称',
    `parent_id`  BIGINT    DEFAULT NULL COMMENT '父分类ID，顶级分类为NULL',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '分类创建时间'
) COMMENT '商品分类表，存储商品的层级分类';

-- ==============================================
--               2️⃣ 库存管理模块
-- ==============================================

CREATE TABLE `stock_in`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库记录唯一标识符',
    `product_id`   BIGINT         NOT NULL COMMENT '入库商品ID',
    `supplier_id`  BIGINT COMMENT '供应商ID',
    `quantity`     INT            NOT NULL COMMENT '入库数量',
    `cost_price`   DECIMAL(10, 2) NOT NULL COMMENT '入库成本单价',
    `total_cost`   DECIMAL(10, 2) NOT NULL COMMENT '入库总成本',
    `warehouse_id` BIGINT         NOT NULL COMMENT '仓库ID',
    `received_by`  BIGINT         NOT NULL COMMENT '经办人员ID',
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间'
) COMMENT '库存入库记录表';

CREATE TABLE `stock_out`
(
    `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '出库记录唯一标识符',
    `order_id`     BIGINT COMMENT '关联的订单ID',
    `product_id`   BIGINT NOT NULL COMMENT '出库商品ID',
    `quantity`     INT    NOT NULL COMMENT '出库数量',
    `warehouse_id` BIGINT NOT NULL COMMENT '仓库ID',
    `shipped_by`   BIGINT NOT NULL COMMENT '经办人员ID',
    `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间'
) COMMENT '库存出库记录表';

CREATE TABLE `supplier`
(
    `id`         BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商唯一标识符',
    `name`       VARCHAR(255) NOT NULL COMMENT '供应商名称',
    `contact`    VARCHAR(100) COMMENT '联系人姓名',
    `phone`      VARCHAR(20) COMMENT '联系电话',
    `email`      VARCHAR(255) COMMENT '电子邮件',
    `address`    VARCHAR(255) COMMENT '供应商地址',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '供应商信息表';

-- -- ==============================================
-- --               3️⃣ 订单管理模块
-- -- ==============================================
--
-- CREATE TABLE `order`
-- (
--     `id`           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单唯一标识符',
--     `order_number` VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
--     `customer_id`  BIGINT             NOT NULL COMMENT '客户ID',
--     `total_amount` DECIMAL(10, 2)     NOT NULL COMMENT '订单总金额',
--     `status`       ENUM('待付款', '已付款', '待发货', '已发货', '已完成', '已取消') NOT NULL DEFAULT '待付款' COMMENT '订单状态',
--     `created_at`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间'
-- ) COMMENT '订单信息表';
--
-- CREATE TABLE `order_items`
-- (
--     `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单商品唯一标识符',
--     `order_id`    BIGINT         NOT NULL COMMENT '关联的订单ID',
--     `product_id`  BIGINT         NOT NULL COMMENT '商品ID',
--     `quantity`    INT            NOT NULL COMMENT '商品数量',
--     `price`       DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
--     `total_price` DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
--     `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
-- ) COMMENT '订单商品详情表';
-- ==============================================
--               3️⃣ 订单管理模块（更新版）
-- ==============================================

-- 订单信息表（移除 customer_id，新增客户信息字段）
CREATE TABLE `order`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单唯一标识符',
    `order_number`   VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
    `customer_name`  VARCHAR(100) NOT NULL COMMENT '客户姓名',
    `platform`       VARCHAR(50)  NOT NULL COMMENT '客户下单平台（如淘宝、拼多多等）',
    `phone`          VARCHAR(20)  NOT NULL COMMENT '客户联系电话',
    `address`        VARCHAR(255) NOT NULL COMMENT '客户收货地址',
    `total_amount`   DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    `status`         ENUM('待付款', '已付款', '待发货', '已发货', '已完成', '已取消') NOT NULL DEFAULT '待付款' COMMENT '订单状态',
    `created_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间'
) COMMENT '订单信息表，包含客户信息';

-- 订单商品详情表
CREATE TABLE `order_items`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单商品唯一标识符',
    `order_id`    BIGINT         NOT NULL COMMENT '关联的订单ID',
    `product_id`  BIGINT         NOT NULL COMMENT '商品ID',
    `quantity`    INT            NOT NULL COMMENT '商品数量',
    `price`       DECIMAL(10, 2) NOT NULL COMMENT '商品单价',
    `total_price` DECIMAL(10, 2) NOT NULL COMMENT '商品总价',
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`) ON DELETE CASCADE
) COMMENT '订单商品详情表';

-- 退货订单表（新增状态、拒绝原因字段）
CREATE TABLE `return_orders`
(
    `id`             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '退货订单唯一标识符',
    `order_id`       BIGINT NOT NULL COMMENT '关联的订单ID',
    `product_id`     BIGINT NOT NULL COMMENT '退货商品ID',
    `return_reason`  TEXT COMMENT '退货原因',
    `return_status`  ENUM('待处理', '同意', '拒绝') DEFAULT '待处理' COMMENT '退货处理状态',
    `reject_reason`  TEXT COMMENT '拒绝申请原因（仅当退货被拒绝时填写）',
    `return_date`    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '退货时间',
    FOREIGN KEY (`order_id`) REFERENCES `order`(`id`) ON DELETE CASCADE
) COMMENT '退货订单表，记录退货状态和拒绝原因';

-- ==============================================
--               4️⃣ 采购管理模块
-- ==============================================

CREATE TABLE `purchase_order`
(
    `id`          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '采购订单唯一标识符',
    `supplier_id` BIGINT         NOT NULL COMMENT '供应商ID',
    `total_cost`  DECIMAL(10, 2) NOT NULL COMMENT '采购总成本',
    `status`      ENUM('待审核', '已通过', '已驳回', '已完成') NOT NULL DEFAULT '待审核' COMMENT '采购订单状态',
    `created_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '采购订单创建时间'
) COMMENT '采购订单表';

CREATE TABLE `purchase_items`
(
    `id`                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '采购商品唯一标识符',
    `purchase_order_id` BIGINT         NOT NULL COMMENT '采购订单ID',
    `product_id`        BIGINT         NOT NULL COMMENT '商品ID',
    `quantity`          INT            NOT NULL COMMENT '采购数量',
    `cost_price`        DECIMAL(10, 2) NOT NULL COMMENT '采购成本单价',
    `total_price`       DECIMAL(10, 2) NOT NULL COMMENT '采购总价格',
    `created_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT '采购商品详情表';

-- ==============================================
--               5️⃣ 损耗管理模块
-- ==============================================
--

CREATE TABLE `stock_adjustments`
(
    `id`              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库存调整唯一标识符',
    `product_id`      BIGINT NOT NULL COMMENT '调整商品ID',
    `adjustment_type` ENUM('损坏', '过期', '其他') NOT NULL COMMENT '库存调整类型',
    `quantity`        INT    NOT NULL COMMENT '调整数量',
    `remarks`         TEXT COMMENT '调整备注',
    `created_at`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '调整时间'
) COMMENT '库存调整记录表';
