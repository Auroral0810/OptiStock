-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (arm64)
--
-- Host: localhost    Database: OptiStock
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Login_Logs`
--

DROP TABLE IF EXISTS `Login_Logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Login_Logs` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '日志唯一标识',
  `user_id` bigint unsigned NOT NULL COMMENT '用户ID',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `ip_address` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录IP地址',
  `device` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登录设备信息',
  `status` enum('success','failed') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录是否成功',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `login_logs_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `Users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Login_Logs`
--

LOCK TABLES `Login_Logs` WRITE;
/*!40000 ALTER TABLE `Login_Logs` DISABLE KEYS */;
INSERT INTO `Login_Logs` VALUES (1,1,'2025-01-19 13:43:58','192.168.1.1','Windows 10 - Chrome','success'),(2,2,'2025-01-19 13:43:58','203.0.113.45','MacOS - Safari','success'),(3,3,'2025-01-19 13:43:58','198.51.100.23','Android - Firefox','failed'),(4,1,'2025-01-19 13:43:58','192.168.1.1','Windows 10 - Edge','success'),(5,4,'2025-01-19 13:43:58','192.0.2.55','iPhone - Safari','failed');
/*!40000 ALTER TABLE `Login_Logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单唯一标识符',
  `order_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `customer_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户姓名',
  `platform` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户下单平台（如淘宝、拼多多等）',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户联系电话',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '客户收货地址',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` enum('待付款','已付款','待发货','已发货','已完成','已取消') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '待付款' COMMENT '订单状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES (1,'ORD20230101','张三','淘宝','13800138000','北京市朝阳区CBD',15998.00,'已发货','2025-01-19 17:02:57'),(2,'ORD20230102','李四','京东','13900239000','上海市浦东新区陆家嘴',2999.00,'待付款','2025-01-19 17:02:57'),(3,'ORD20230103','王五','拼多多','13600336000','广州市天河区天河北路',6899.00,'已发货','2025-01-19 17:02:57'),(4,'ORD20230104','赵六','天猫','13700447000','深圳市南山区科技园',7999.00,'待发货','2025-01-19 17:02:57'),(5,'ORD20230105','孙七','苏宁','13500555000','成都市锦江区春熙路',2499.00,'已完成','2025-01-19 17:02:57'),(6,'ORD20230106','周八','抖音','13400666000','杭州市西湖区西湖风景区',499.00,'已发货','2025-01-19 17:02:57'),(7,'ORD20230107','吴九','快手','13300777000','武汉市江汉区建设大道',12999.00,'已发货','2025-01-19 17:02:57'),(8,'OPT202501221818287444','俞云烽','淘宝','15968588744','南京市建邺区',51996.00,'待付款','2025-01-22 02:55:48'),(10,'OPT202501221859287444','俞云烽','淘宝','15968588744','南京市建邺区',51996.00,'待付款','2025-01-22 10:58:07'),(12,'OPT202501221934487441','俞云烽','天猫','15968588744','南京',2999.00,'待付款','2025-01-22 11:34:02'),(13,'OPT202501221938387441','俞云烽','淘宝','15968588744','江苏',6899.00,'待付款','2025-01-22 11:38:03');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单商品唯一标识符',
  `order_id` bigint NOT NULL COMMENT '关联的订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '商品数量',
  `price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '商品总价',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单商品详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,2,7999.00,15998.00,'2025-01-19 17:02:57'),(2,2,3,1,2999.00,2999.00,'2025-01-19 17:02:57'),(3,3,3,1,6899.00,6899.00,'2025-01-19 17:02:57'),(4,4,6,1,3999.00,7999.00,'2025-01-19 17:02:57'),(5,5,5,5,499.00,2495.00,'2025-01-19 17:02:57'),(6,6,6,1,499.00,499.00,'2025-01-19 17:02:57'),(7,7,2,1,12999.00,12999.00,'2025-01-19 17:02:57'),(8,8,2,4,12999.00,51996.00,'2025-01-22 02:55:49'),(10,10,2,4,12999.00,51996.00,'2025-01-22 10:58:07'),(12,12,4,1,2999.00,2999.00,'2025-01-22 11:34:02'),(13,13,3,1,6899.00,6899.00,'2025-01-22 11:38:03');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品唯一标识符',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '商品分类ID，关联product_category表',
  `sku` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '库存单位编号（SKU）',
  `price` decimal(10,2) NOT NULL COMMENT '商品售价',
  `cost_price` decimal(10,2) NOT NULL COMMENT '商品成本价',
  `stock_quantity` int DEFAULT '0' COMMENT '库存数量',
  `unit` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT '件' COMMENT '计量单位，如件、盒、瓶等',
  `status` enum('上架','下架','停产') COLLATE utf8mb4_unicode_ci DEFAULT '上架' COMMENT '商品状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商品创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '商品信息更新时间',
  `warning_threshold` decimal(10,2) DEFAULT '0.00' COMMENT '库存预警阈值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku` (`sku`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'iPhone 14',3,'IP14-001',7999.00,5000.00,44,'件','上架','2025-01-19 17:02:57','2025-01-21 08:23:34',30.00),(2,'MacBook Pro',4,'MBP-2022',12999.00,9000.00,29,'件','上架','2025-01-19 17:02:57','2025-01-21 08:23:34',35.00),(3,'华为 Mate 50',3,'HWM50-001',6899.00,4500.00,41,'件','上架','2025-01-19 17:02:57','2025-01-19 17:02:57',0.00),(4,'小米空调',6,'XM-AC-001',2999.00,2000.00,60,'件','上架','2025-01-19 17:02:57','2025-01-21 08:23:34',70.00),(5,'智能电饭煲',5,'ZNDF-001',499.00,300.00,100,'件','上架','2025-01-19 17:02:57','2025-01-21 10:14:35',90.00),(6,'三星 Galaxy S23',3,'SGS23-001',7999.00,5500.00,34,'件','上架','2025-01-19 17:02:57','2025-01-21 07:33:58',0.00),(10,'鞋子',1,'IP14-0011',200.00,150.00,0,'件','上架','2025-01-20 15:08:28','2025-01-20 15:08:28',0.00),(11,'测试商品',12,'SKu112',222.00,111.00,0,'件','下架','2025-01-20 15:39:56','2025-01-20 15:39:56',0.00),(12,'测试商品1',12,'SKu1121',222.00,111.00,0,'件','下架','2025-01-20 15:42:20','2025-01-20 15:42:20',0.00),(13,'测试商品3',6,'SKU123',222.00,22.00,0,'件','上架','2025-01-20 15:43:27','2025-01-20 15:43:27',0.00);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_category`
--

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类唯一标识符',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `parent_id` bigint DEFAULT NULL COMMENT '父分类ID，顶级分类为NULL',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分类创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品分类表，存储商品的层级分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_category`
--

LOCK TABLES `product_category` WRITE;
/*!40000 ALTER TABLE `product_category` DISABLE KEYS */;
INSERT INTO `product_category` VALUES (1,'电子器件',NULL,'2025-01-19 17:02:57'),(2,'家用电器',NULL,'2025-01-19 17:02:57'),(3,'手机',1,'2025-01-19 17:02:57'),(4,'笔记本电脑',1,'2025-01-19 17:02:57'),(5,'厨房电器',2,'2025-01-19 17:02:57'),(6,'客厅电器',2,'2025-01-19 17:02:57'),(8,'纸巾',NULL,'2025-01-20 07:49:14'),(10,'鞋子',NULL,'2025-01-20 08:18:53'),(12,'书包',NULL,'2025-01-20 08:56:06'),(13,'电子狗',NULL,'2025-01-20 11:59:56');
/*!40000 ALTER TABLE `product_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_items`
--

DROP TABLE IF EXISTS `purchase_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购商品唯一标识符',
  `purchase_order_id` bigint NOT NULL COMMENT '采购订单ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `quantity` int NOT NULL COMMENT '采购数量',
  `cost_price` decimal(10,2) NOT NULL COMMENT '采购成本单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '采购总价格',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购商品详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_items`
--

LOCK TABLES `purchase_items` WRITE;
/*!40000 ALTER TABLE `purchase_items` DISABLE KEYS */;
INSERT INTO `purchase_items` VALUES (1,1,1,10,5000.00,50000.00,'2025-01-19 17:02:57'),(3,2,2,5,9000.00,45000.00,'2025-01-19 17:02:57'),(5,3,5,30,300.00,9000.00,'2025-01-19 17:02:57'),(6,4,6,12,5500.00,66000.00,'2025-01-19 17:02:57'),(7,5,7,8,2500.00,20000.00,'2025-01-19 17:02:57'),(8,6,1,7,5000.00,35000.00,'2025-01-19 17:02:57'),(9,7,2,4,9000.00,36000.00,'2025-01-19 17:02:57'),(10,8,1,1,5000.00,5000.00,'2025-01-22 05:01:10'),(11,9,1,1,5000.00,5000.00,'2025-01-22 05:03:44'),(12,10,1,1,5000.00,5000.00,'2025-01-22 05:05:03');
/*!40000 ALTER TABLE `purchase_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单唯一标识符',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `total_cost` decimal(10,2) NOT NULL COMMENT '采购总成本',
  `status` enum('待审核','已通过','已驳回','已完成') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '待审核' COMMENT '采购订单状态',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '采购订单创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
INSERT INTO `purchase_order` VALUES (1,1,50000.00,'已通过','2025-01-19 17:02:57'),(2,2,45000.00,'已完成','2025-01-19 17:02:57'),(3,3,9000.00,'已通过','2025-01-19 17:02:57'),(4,4,66000.00,'已完成','2025-01-19 17:02:57'),(5,5,20000.00,'已通过','2025-01-19 17:02:57'),(6,6,35000.00,'已驳回','2025-01-19 17:02:57'),(7,7,36000.00,'已驳回','2025-01-19 17:02:57'),(8,1,5000.00,'待审核','2025-01-22 05:01:10'),(9,1,5000.00,'待审核','2025-01-22 05:03:44'),(10,1,5000.00,'已驳回','2025-01-22 05:05:03');
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `return_orders`
--

DROP TABLE IF EXISTS `return_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `return_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '退货订单唯一标识符',
  `order_id` bigint NOT NULL COMMENT '关联的订单ID',
  `product_id` bigint NOT NULL COMMENT '退货商品ID',
  `return_reason` text COLLATE utf8mb4_unicode_ci COMMENT '退货原因',
  `return_status` enum('待处理','同意','拒绝') COLLATE utf8mb4_unicode_ci DEFAULT '待处理' COMMENT '退货处理状态',
  `reject_reason` text COLLATE utf8mb4_unicode_ci COMMENT '拒绝申请原因（仅当退货被拒绝时填写）',
  `return_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '退货时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退货订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `return_orders`
--

LOCK TABLES `return_orders` WRITE;
/*!40000 ALTER TABLE `return_orders` DISABLE KEYS */;
INSERT INTO `return_orders` VALUES (1,1,1,'质量问题','同意','测试','2025-01-19 17:02:57'),(2,2,3,'包装损坏','同意',NULL,'2025-01-19 17:02:57'),(3,3,3,'客户不满意','同意',NULL,'2025-01-19 17:02:57'),(4,4,6,'错发商品','同意',NULL,'2025-01-19 17:02:57'),(5,5,5,'颜色不对','拒绝','测试拒绝','2025-01-19 17:02:57'),(6,6,6,'性能不佳','同意',NULL,'2025-01-19 17:02:57'),(7,7,2,'发货延误','拒绝','测试','2025-01-19 17:02:57');
/*!40000 ALTER TABLE `return_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_adjustments`
--

DROP TABLE IF EXISTS `stock_adjustments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_adjustments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存调整唯一标识符',
  `product_id` bigint NOT NULL COMMENT '调整商品ID',
  `adjustment_type` enum('损坏','过期','其他') COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '库存调整类型',
  `quantity` int NOT NULL COMMENT '调整数量',
  `remarks` text COLLATE utf8mb4_unicode_ci COMMENT '调整备注',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '调整时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存调整记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_adjustments`
--

LOCK TABLES `stock_adjustments` WRITE;
/*!40000 ALTER TABLE `stock_adjustments` DISABLE KEYS */;
INSERT INTO `stock_adjustments` VALUES (1,1,'损坏',5,'运输过程中损坏','2025-01-19 17:02:57'),(2,2,'过期',3,'库存时间过长，超过保质期','2025-01-19 17:02:57'),(3,3,'损坏',2,'仓库存储不当导致损坏','2025-01-19 17:02:57'),(4,4,'其他',4,'系统库存盘点误差调整','2025-01-19 17:02:57'),(5,5,'损坏',1,'开箱检查时发现瑕疵','2025-01-19 17:02:57'),(6,6,'过期',6,'产品更新换代，旧货已过期','2025-01-19 17:02:57'),(7,7,'其他',2,'客户退货后重新入库调整','2025-01-19 17:02:57'),(8,1,'损坏',2,'测试调整','2025-01-21 07:18:36'),(9,1,'损坏',2,'测试调整','2025-01-21 07:20:52'),(10,1,'损坏',2,'测试调整','2025-01-21 07:21:37'),(11,1,'损坏',2,'调整库存前端测试','2025-01-21 07:27:47'),(12,6,'过期',1,'测试调整库存2','2025-01-21 07:33:58'),(24,1,'损坏',5,'运输损坏','2025-01-19 08:12:03'),(25,2,'过期',10,'过期退货','2025-01-16 08:12:03'),(26,3,'其他',3,'库存盘点调整','2025-01-15 08:12:03'),(27,1,'损坏',7,'质量问题','2025-01-11 08:12:03'),(28,1,'过期',15,'存储不当','2025-01-01 08:12:03'),(29,1,'其他',8,'系统调整','2024-12-27 08:12:03'),(30,1,'损坏',12,'仓库搬运损坏','2024-12-12 08:12:03'),(31,2,'过期',20,'批次过期','2024-10-23 08:12:03'),(32,2,'其他',4,'数据修正','2024-07-05 08:12:03'),(33,2,'损坏',9,'外部因素影响','2024-03-27 08:12:03'),(34,2,'过期',11,'临期未售出','2024-02-06 08:12:03');
/*!40000 ALTER TABLE `stock_adjustments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_in`
--

DROP TABLE IF EXISTS `stock_in`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '入库记录唯一标识符',
  `product_id` bigint NOT NULL COMMENT '入库商品ID',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `quantity` int NOT NULL COMMENT '入库数量',
  `cost_price` decimal(10,2) NOT NULL COMMENT '入库成本单价',
  `total_cost` decimal(10,2) NOT NULL COMMENT '入库总成本',
  `received_by` bigint NOT NULL COMMENT '经办人员ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存入库记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_in`
--

LOCK TABLES `stock_in` WRITE;
/*!40000 ALTER TABLE `stock_in` DISABLE KEYS */;
INSERT INTO `stock_in` VALUES (1,1,1,10,5000.00,50000.00,1,'2025-01-19 17:02:57'),(2,2,1,5,9000.00,45000.00,2,'2025-01-19 17:02:57'),(3,3,2,15,4500.00,67500.00,3,'2025-01-19 17:02:57'),(4,4,3,20,2000.00,40000.00,4,'2025-01-19 17:02:57'),(5,5,4,30,300.00,9000.00,5,'2025-01-19 17:02:57'),(6,6,5,12,5500.00,66000.00,6,'2025-01-19 17:02:57'),(7,7,6,8,2500.00,20000.00,7,'2025-01-19 17:02:57');
/*!40000 ALTER TABLE `stock_in` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_out`
--

DROP TABLE IF EXISTS `stock_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库记录唯一标识符',
  `order_id` bigint DEFAULT NULL COMMENT '关联的订单ID',
  `product_id` bigint NOT NULL COMMENT '出库商品ID',
  `quantity` int NOT NULL COMMENT '出库数量',
  `shipped_by` bigint NOT NULL COMMENT '经办人员ID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '出库时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存出库记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_out`
--

LOCK TABLES `stock_out` WRITE;
/*!40000 ALTER TABLE `stock_out` DISABLE KEYS */;
INSERT INTO `stock_out` VALUES (1,1,1,2,1,'2025-01-19 17:02:57'),(2,2,2,1,2,'2025-01-19 17:02:57'),(3,3,3,3,3,'2025-01-19 17:02:57'),(4,4,4,5,4,'2025-01-19 17:02:57'),(5,5,5,8,5,'2025-01-19 17:02:57'),(6,6,6,2,6,'2025-01-19 17:02:57'),(7,7,7,1,7,'2025-01-19 17:02:57'),(8,1,1,2,1,'2025-01-22 12:13:37');
/*!40000 ALTER TABLE `stock_out` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商唯一标识符',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供应商名称',
  `contact` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系人姓名',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '电子邮件',
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '供应商地址',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='供应商信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'苹果中国','王先生','13800000001','apple@china.com','北京中关村','2025-01-19 17:02:57'),(3,'小米科技','张先生','13600000003','xiaomi@supply.com','北京亦庄','2025-01-19 17:02:57'),(5,'三星电子','金先生','13500000019','samsung@supply.com','韩国首尔','2025-01-19 17:02:57'),(6,'海尔集团','赵先生','13100000006','haier@supply.com','青岛高新区','2025-01-19 17:02:57'),(7,'美的电器','陈小姐','13200000007','midea@supply.com','佛山顺德','2025-01-19 17:02:57'),(9,'你好中国','俞云烽','13345678912','12311231@13.com','测试地址','2025-01-21 12:31:06');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（唯一）',
  `nickname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称（可选）',
  `password_hash` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MD5加密后的密码',
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱地址（可选）',
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号（可选）',
  `avatar` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '头像URL',
  `role` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户角色：0=管理员，1=普通用户',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '账户状态：0=未激活，1=正常，2=封禁',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `last_login` datetime DEFAULT NULL COMMENT '最近一次登录时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'admin','系统管理员','0e7517141fb53f21ee439b355b5a1d0a','admin@example.com','13800000001','https://example.com/avatar/admin.png',0,1,'2025-01-19 05:43:39','2025-01-19 05:43:39','2025-01-19 13:43:39'),(2,'john_doe','John','448ddd517d3abb70045aea6929f02367','john@example.com','13800000002','https://example.com/avatar/john.png',1,1,'2025-01-19 05:43:39','2025-01-19 05:43:39',NULL),(3,'jane_smith','Jane','0c41442ec923176a044438053b1ce09d','jane@example.com',NULL,'https://example.com/avatar/jane.png',1,1,'2025-01-19 05:43:39','2025-01-19 05:43:39',NULL),(4,'test_user','测试用户','2c9341ca4cf3d87b9e4eb905d6a3ec45',NULL,'13800000003',NULL,1,0,'2025-01-19 05:43:39','2025-01-19 05:43:39',NULL),(5,'banned_user','违规用户','e214affd3b7ea10a51a562e098df5037','banned@example.com','13800000004',NULL,1,2,'2025-01-19 05:43:39','2025-01-19 05:43:39',NULL),(6,'zaaaaa','wangba','25c2ad22f126c7360b503ea91c73ef99',NULL,NULL,NULL,1,1,'2025-01-19 07:00:20','2025-01-19 07:12:56',NULL),(7,'zaaa1','wangba','25c2ad22f126c7360b503ea91c73ef99',NULL,NULL,NULL,1,1,'2025-01-19 07:01:08','2025-01-19 07:12:56',NULL),(8,'zaa11','wangba','25c2ad22f126c7360b503ea91c73ef99',NULL,NULL,NULL,1,1,'2025-01-19 07:07:36','2025-01-19 07:12:56',NULL),(9,'zaa144','wangba','25c2ad22f126c7360b503ea91c73ef99',NULL,NULL,NULL,1,1,'2025-01-19 07:08:05','2025-01-19 07:12:56',NULL),(10,'yuyunfeng','yyfa','e10adc3949ba59abbe56e057f20f883e','15968588744@163.com','15968588744','https://big-event20040810.oss-cn-beijing.aliyuncs.com/178a5075-7e18-438d-bc48-1dab154e00f5.jpg',1,1,'2025-01-19 07:11:34','2025-01-21 03:12:40',NULL),(11,'xiaoyu','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:12:26','2025-01-19 07:12:56',NULL),(12,'xiaoyu1','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:13:00','2025-01-19 07:13:09',NULL),(13,'xiaoyu11','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:14:05','2025-01-19 07:17:29',NULL),(14,'xiaoyu112','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:17:33','2025-01-19 07:18:34',NULL),(15,'zhangsan','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:18:52','2025-01-19 07:18:52',NULL),(16,'zhangsan1','dawang','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,NULL,1,1,'2025-01-19 07:19:24','2025-01-19 07:19:24',NULL),(17,'user1','dasdsa','220466675e31b9d20c051d5e57974150',NULL,NULL,NULL,1,1,'2025-01-19 08:14:34','2025-01-19 08:14:34',NULL),(18,'aaaaa','adad','b0baee9d279d34fa1dfd71aadb908c3f',NULL,NULL,NULL,1,1,'2025-01-19 09:55:43','2025-01-19 09:55:43',NULL),(19,'liuyixuan','lyx','b0baee9d279d34fa1dfd71aadb908c3f',NULL,NULL,NULL,1,1,'2025-01-19 09:57:33','2025-01-19 09:57:33',NULL),(20,'wangba','yyf','e10adc3949ba59abbe56e057f20f883e','','','https://big-event20040810.oss-cn-beijing.aliyuncs.com/aded88de-da60-4f7a-9084-6e2bd5905b79.png',1,1,'2025-01-20 06:27:45','2025-01-20 09:36:33',NULL);
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-23 17:18:22
