package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 采购商品详情表(PurchaseItems)表实体类
 *
 * @author makejava
 * @since 2025-01-21 23:03:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_items")
public class PurchaseItems  {
    //分类唯一标识符@TableId
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//采购订单ID
    private Long purchaseOrderId;
//商品ID
    private Long productId;
//采购数量
    private Integer quantity;
//采购成本单价
    private Double costPrice;
//采购总价格
    private Double totalPrice;
//创建时间
    private Date createdAt;



}

