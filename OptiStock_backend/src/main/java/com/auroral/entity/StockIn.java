package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 库存入库记录表(StockIn)表实体类
 *
 * @author makejava
 * @since 2025-01-21 12:06:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stock_in")
public class StockIn  {
//入库记录唯一标识符@TableId
@TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//入库商品ID
    private Long productId;
//供应商ID
    private Long supplierId;
//入库数量
    private Integer quantity;
//入库成本单价
    private Double costPrice;
//入库总成本
    private Double totalCost;
//仓库ID
    private Long warehouseId;
//经办人员ID
    private Long receivedBy;
//入库时间
    private Date createdAt;



}

