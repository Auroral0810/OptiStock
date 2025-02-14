package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 采购订单表(PurchaseOrder)表实体类
 *
 * @author makejava
 * @since 2025-01-21 20:19:54
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("purchase_order")
public class PurchaseOrder  {
    //分类唯一标识符@TableId
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//供应商ID
    private Long supplierId;
//采购总成本
    private Double totalCost;
//采购订单状态
    private String status;
//采购订单创建时间
    private Date createdAt;



}

