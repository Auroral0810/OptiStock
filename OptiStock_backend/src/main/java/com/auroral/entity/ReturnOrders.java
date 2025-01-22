package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 退货订单表(ReturnOrders)表实体类
 *
 * @author makejava
 * @since 2025-01-22 14:55:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("return_orders")
public class ReturnOrders  {
//退货订单唯一标识符@TableId
@TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//关联的订单ID
    private Long orderId;
//退货商品ID
    private Long productId;
//退货原因
    private String returnReason;
//退货处理状态
    private String returnStatus;
//拒绝申请原因（仅当退货被拒绝时填写）
    private String rejectReason;
//退货时间
    private Date returnDate;



}

