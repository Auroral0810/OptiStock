
use OptiStock;
-- -- ==========================================
-- -- 用户表示例数据
-- -- ==========================================
-- INSERT INTO users (username, password, email, phone, role, created_at, updated_at) VALUES
-- ('admin', 'e99a18c428cb38d5f260853678922e03', 'admin@example.com', '1234567890', 'admin', NOW(), NOW()),
-- ('user1', 'e99a18c428cb38d5f260853678922e03', 'user1@example.com', '1234567891', 'user', NOW(), NOW()),
-- ('user2', 'e99a18c428cb38d5f260853678922e03', 'user2@example.com', '1234567892', 'user', NOW(), NOW()),
-- ('user3', 'e99a18c428cb38d5f260853678922e03', 'user3@example.com', '1234567893', 'user', NOW(), NOW()),
-- ('user4', 'e99a18c428cb38d5f260853678922e03', 'user4@example.com', '1234567894', 'user', NOW(), NOW()),
-- ('user5', 'e99a18c428cb38d5f260853678922e03', 'user5@example.com', '1234567895', 'user', NOW(), NOW());

-- ==========================================
-- 商品分类表示例数据
-- ==========================================
INSERT INTO product_categories (name, description, created_at, updated_at) VALUES
('电子产品', '各类电子设备', NOW(), NOW()),
('服装', '男女服饰', NOW(), NOW()),
('食品', '生鲜与加工食品', NOW(), NOW()),
('家居用品', '家具与家庭用品', NOW(), NOW()),
('办公用品', '办公及文具', NOW(), NOW()),
('体育用品', '运动及户外装备', NOW(), NOW());

-- ==========================================
-- 商品表示例数据
-- ==========================================
INSERT INTO products (name, category_id, price, stock, created_at, updated_at) VALUES
('智能手机', 1, 3999.99, 100, NOW(), NOW()),
('笔记本电脑', 1, 5999.99, 50, NOW(), NOW()),
('T恤', 2, 99.99, 200, NOW(), NOW()),
('运动鞋', 2, 299.99, 150, NOW(), NOW()),
('办公椅', 4, 499.99, 80, NOW(), NOW()),
('篮球', 6, 199.99, 120, NOW(), NOW());

-- ==========================================
-- 供应商表示例数据
-- ==========================================
INSERT INTO suppliers (name, contact_person, phone, email, address, created_at, updated_at) VALUES
('电子配件供应商A', '张三', '1234567890', 'supplierA@example.com', '北京市海淀区XX路', NOW(), NOW()),
('电子配件供应商B', '李四', '1234567891', 'supplierB@example.com', '上海市浦东新区XX路', NOW(), NOW()),
('服装供应商A', '王五', '1234567892', 'supplierC@example.com', '广州市天河区XX路', NOW(), NOW()),
('食品供应商A', '赵六', '1234567893', 'supplierD@example.com', '深圳市南山区XX路', NOW(), NOW()),
('办公用品供应商A', '钱七', '1234567894', 'supplierE@example.com', '杭州市西湖区XX路', NOW(), NOW()),
('运动用品供应商A', '孙八', '1234567895', 'supplierF@example.com', '成都市高新区XX路', NOW(), NOW());

-- ==========================================
-- 入库记录表示例数据
-- ==========================================
INSERT INTO stock_in_records (product_id, supplier_id, quantity, cost_price, created_at) VALUES
(1, 1, 50, 3500.00, NOW()),
(2, 2, 30, 5500.00, NOW()),
(3, 3, 100, 80.00, NOW()),
(4, 3, 120, 250.00, NOW()),
(5, 5, 40, 450.00, NOW()),
(6, 6, 60, 150.00, NOW());

-- ==========================================
-- 出库记录表示例数据
-- ==========================================
INSERT INTO stock_out_records (product_id, quantity, sale_price, created_at) VALUES
(1, 10, 3999.99, NOW()),
(2, 5, 5999.99, NOW()),
(3, 20, 99.99, NOW()),
(4, 15, 299.99, NOW()),
(5, 8, 499.99, NOW()),
(6, 12, 199.99, NOW());

-- ==========================================
-- 订单表示例数据
-- ==========================================
INSERT INTO orders (user_id, total_price, status, created_at, updated_at) VALUES
(2, 19999.95, '已完成', NOW(), NOW()),
(3, 5999.99, '已发货', NOW(), NOW()),
(4, 299.99, '待付款', NOW(), NOW()),
(5, 999.98, '已取消', NOW(), NOW()),
(6, 3999.99, '待发货', NOW(), NOW()),
(2, 899.97, '已完成', NOW(), NOW());

-- ==========================================
-- 订单详情表示例数据
-- ==========================================
INSERT INTO order_details (order_id, product_id, quantity, unit_price) VALUES
(1, 1, 5, 3999.99),
(1, 3, 2, 99.99),
(2, 2, 1, 5999.99),
(3, 4, 1, 299.99),
(4, 5, 2, 499.99),
(5, 6, 1, 199.99);
