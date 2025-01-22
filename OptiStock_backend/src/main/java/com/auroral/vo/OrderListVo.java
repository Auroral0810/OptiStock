package com.auroral.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OrderListVo {
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
    //商品ID
    private String  productName;
    //商品数量
    private Integer quantity;
    //商品单价
    private Double price;
    //商品总价
    private Double totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //创建时间
    private Date createdAt;
}
