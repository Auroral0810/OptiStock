package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 订单信息表(Order)表实体类
 *
 * @author makejava
 * @since 2025-01-22 14:54:35
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("`order`")  // 使用反引号包裹表名，防止关键字冲突
public class Order  {

//订单唯一标识符@TableId
@TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//订单编号
    private String orderNumber;
//客户姓名
    private String customerName;
//客户下单平台（如淘宝、拼多多等）
    private String platform;
//客户联系电话
    private String phone;
//客户收货地址
    private String address;
//订单总金额
    private Double totalAmount;
//订单状态
    private String status;
//订单创建时间
    private Date createdAt;



}

