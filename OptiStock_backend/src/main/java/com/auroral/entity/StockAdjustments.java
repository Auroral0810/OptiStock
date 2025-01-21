package com.auroral.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 库存调整记录表(StockAdjustments)表实体类
 *
 * @author makejava
 * @since 2025-01-21 12:07:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stock_adjustments")
public class StockAdjustments  {
//库存调整唯一标识符@TableId
    private Long id;

//调整商品ID
    private Long productId;
//库存调整类型
    private String adjustmentType;
//调整数量
    private Integer quantity;
//调整备注
    private String remarks;
//调整时间
    private Date createdAt;



}

