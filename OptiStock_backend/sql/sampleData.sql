use OptiStock;
-- ==============================================
-- 插入商品分类表数据
-- ==============================================
INSERT INTO `product_category` (`name`, `parent_id`)
VALUES ('电子产品', NULL),
       ('家用电器', NULL),
       ('手机', 1),
       ('笔记本电脑', 1),
       ('厨房电器', 2),
       ('客厅电器', 2),
       ('智能家居', 2);

-- ==============================================
-- 插入商品表数据
-- ==============================================
INSERT INTO `product` (`name`, `category_id`, `sku`, `price`, `cost_price`, `stock_quantity`, `unit`, `status`)
VALUES ('iPhone 14', 3, 'IP14-001', 7999.00, 5000.00, 50, '件', '上架'),
       ('MacBook Pro', 4, 'MBP-2022', 12999.00, 9000.00, 30, '件', '上架'),
       ('华为 Mate 50', 3, 'HWM50-001', 6899.00, 4500.00, 40, '件', '上架'),
       ('小米空调', 6, 'XM-AC-001', 2999.00, 2000.00, 60, '件', '上架'),
       ('智能电饭煲', 5, 'ZNDF-001', 499.00, 300.00, 100, '件', '上架'),
       ('三星 Galaxy S23', 3, 'SGS23-001', 7999.00, 5500.00, 35, '件', '上架'),
       ('海尔冰箱', 6, 'HR-BX-001', 3999.00, 2500.00, 25, '件', '上架');

-- ==============================================
-- 插入供应商表数据
-- ==============================================
INSERT INTO `supplier` (`name`, `contact`, `phone`, `email`, `address`)
VALUES ('苹果中国', '李先生', '13800000001', 'apple@china.com', '北京中关村'),
       ('华为供应链', '王小姐', '13900000002', 'huawei@supply.com', '深圳科技园'),
       ('小米科技', '张先生', '13600000003', 'xiaomi@supply.com', '北京亦庄'),
       ('京东供应商', '刘先生', '13700000004', 'jd@supply.com', '上海浦东'),
       ('三星电子', '金先生', '13500000005', 'samsung@supply.com', '韩国首尔'),
       ('海尔集团', '赵先生', '13100000006', 'haier@supply.com', '青岛高新区'),
       ('美的电器', '陈小姐', '13200000007', 'midea@supply.com', '佛山顺德');

-- ==============================================
-- 插入库存入库表数据
-- ==============================================
INSERT INTO `stock_in` (`product_id`, `supplier_id`, `quantity`, `cost_price`, `total_cost`, `warehouse_id`,
                        `received_by`)
VALUES (1, 1, 10, 5000.00, 50000.00, 1, 1),
       (2, 1, 5, 9000.00, 45000.00, 1, 2),
       (3, 2, 15, 4500.00, 67500.00, 1, 3),
       (4, 3, 20, 2000.00, 40000.00, 2, 4),
       (5, 4, 30, 300.00, 9000.00, 2, 5),
       (6, 5, 12, 5500.00, 66000.00, 1, 6),
       (7, 6, 8, 2500.00, 20000.00, 2, 7);

-- ==============================================
-- 插入库存出库表数据
-- ==============================================
INSERT INTO `stock_out` (`order_id`, `product_id`, `quantity`, `warehouse_id`, `shipped_by`)
VALUES (1, 1, 2, 1, 1),
       (2, 2, 1, 1, 2),
       (3, 3, 3, 1, 3),
       (4, 4, 5, 2, 4),
       (5, 5, 8, 2, 5),
       (6, 6, 2, 1, 6),
       (7, 7, 1, 2, 7);

-- ==============================================
-- 插入订单表数据
-- ==============================================
INSERT INTO `order` (`order_number`, `customer_id`, `total_amount`, `status`)
VALUES ('ORD20230101', 1, 15998.00, '已付款'),
       ('ORD20230102', 2, 2999.00, '待付款'),
       ('ORD20230103', 3, 6899.00, '待发货'),
       ('ORD20230104', 4, 3999.00, '已发货'),
       ('ORD20230105', 5, 2499.00, '已完成'),
       ('ORD20230106', 6, 499.00, '已完成'),
       ('ORD20230107', 7, 12999.00, '待发货');

-- ==============================================
-- 插入订单商品表数据
-- ==============================================
INSERT INTO `order_items` (`order_id`, `product_id`, `quantity`, `price`, `total_price`)
VALUES (1, 1, 2, 7999.00, 15998.00),
       (2, 4, 1, 2999.00, 2999.00),
       (3, 3, 1, 6899.00, 6899.00),
       (4, 7, 1, 3999.00, 3999.00),
       (5, 5, 5, 499.00, 2495.00),
       (6, 6, 1, 499.00, 499.00),
       (7, 2, 1, 12999.00, 12999.00);

-- ==============================================
-- 插入采购订单表数据
-- ==============================================
INSERT INTO `purchase_order` (`supplier_id`, `total_cost`, `status`)
VALUES (1, 50000.00, '已通过'),
       (2, 45000.00, '已完成'),
       (3, 67500.00, '待审核'),
       (4, 40000.00, '已完成'),
       (5, 9000.00, '已通过'),
       (6, 66000.00, '已驳回'),
       (7, 20000.00, '待审核');
-- ==============================================
-- 插入采购订单明细表数据
-- ==============================================
INSERT INTO `purchase_items` (`purchase_order_id`, `product_id`, `quantity`, `cost_price`, `total_price`)
VALUES (1, 1, 10, 5000.00, 50000.00),
       (1, 3, 15, 4500.00, 67500.00),
       (2, 2, 5, 9000.00, 45000.00),
       (2, 4, 20, 2000.00, 40000.00),
       (3, 5, 30, 300.00, 9000.00),
       (4, 6, 12, 5500.00, 66000.00),
       (5, 7, 8, 2500.00, 20000.00),
       (6, 1, 7, 5000.00, 35000.00),
       (7, 2, 4, 9000.00, 36000.00);

-- ==============================================
-- 插入退货订单表数据
-- ==============================================
INSERT INTO `return_orders` (`order_id`, `product_id`, `return_reason`, `return_status`)
VALUES (1, 1, '质量问题', '已完成'),
       (2, 3, '包装损坏', '待处理'),
       (3, 5, '客户不满意', '已完成'),
       (4, 4, '错发商品', '已完成'),
       (5, 6, '颜色不对', '待处理'),
       (6, 2, '性能不佳', '已完成'),
       (7, 7, '发货延误', '待处理');
-- ==============================================
-- 插入库存调整表数据
-- ==============================================
INSERT INTO `stock_adjustments` (`product_id`, `adjustment_type`, `quantity`, `remarks`)
VALUES (1, '损坏', 5, '运输过程中损坏'),
       (2, '过期', 3, '库存时间过长，超过保质期'),
       (3, '损坏', 2, '仓库存储不当导致损坏'),
       (4, '其他', 4, '系统库存盘点误差调整'),
       (5, '损坏', 1, '开箱检查时发现瑕疵'),
       (6, '过期', 6, '产品更新换代，旧货已过期'),
       (7, '其他', 2, '客户退货后重新入库调整');
-- 最近一周的数据
INSERT INTO stock_adjustments (product_id, adjustment_type, quantity, remarks, created_at)
VALUES
    (1, '损坏', 5, '运输损坏', DATE_SUB(NOW(), INTERVAL 2 DAY)),
    (2, '过期', 10, '过期退货', DATE_SUB(NOW(), INTERVAL 5 DAY)),
    (3, '其他', 3, '库存盘点调整', DATE_SUB(NOW(), INTERVAL 6 DAY));

-- 最近一个月的数据
INSERT INTO stock_adjustments (product_id, adjustment_type, quantity, remarks, created_at)
VALUES
    (1, '损坏', 7, '质量问题', DATE_SUB(NOW(), INTERVAL 10 DAY)),
    (1, '过期', 15, '存储不当', DATE_SUB(NOW(), INTERVAL 20 DAY)),
    (1, '其他', 8, '系统调整', DATE_SUB(NOW(), INTERVAL 25 DAY));

-- 最近一年（超出一个月的数据）
INSERT INTO stock_adjustments (product_id, adjustment_type, quantity, remarks, created_at)
VALUES
    (1, '损坏', 12, '仓库搬运损坏', DATE_SUB(NOW(), INTERVAL 40 DAY)),
    (2, '过期', 20, '批次过期', DATE_SUB(NOW(), INTERVAL 90 DAY)),
    (2, '其他', 4, '数据修正', DATE_SUB(NOW(), INTERVAL 200 DAY)),
    (2, '损坏', 9, '外部因素影响', DATE_SUB(NOW(), INTERVAL 300 DAY)),
    (2, '过期', 11, '临期未售出', DATE_SUB(NOW(), INTERVAL 350 DAY));
