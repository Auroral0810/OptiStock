package com.auroral.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 库存出库记录表(StockOut)表实体类
 *
 * @author makejava
 * @since 2025-01-21 12:06:59
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stock_out")
public class StockOut  {
//出库记录唯一标识符@TableId
    private Long id;

//关联的订单ID
    private Long orderId;
//出库商品ID
    private Long productId;
//出库数量
    private Integer quantity;
//仓库ID
    private Long warehouseId;
//经办人员ID
    private Long shippedBy;
//出库时间
    private Date createdAt;



}

