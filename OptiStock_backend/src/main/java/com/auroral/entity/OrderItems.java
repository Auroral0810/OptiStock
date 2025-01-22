package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 订单商品详情表(OrderItems)表实体类
 *
 * @author makejava
 * @since 2025-01-22 14:54:57
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_items")
public class OrderItems  {
//订单商品唯一标识符@TableId
@TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//关联的订单ID
    private Long orderId;
//商品ID
    private Long productId;
//商品数量
    private Integer quantity;
//商品单价
    private Double price;
//商品总价
    private Double totalPrice;
//创建时间
    private Date createdAt;



}

