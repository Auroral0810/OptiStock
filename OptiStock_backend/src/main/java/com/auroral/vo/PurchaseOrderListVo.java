package com.auroral.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderListVo  {
    //采购订单唯一标识符@TableId
    private Long orderId;
    //商品名称
    private String productName;
    //供应商名称
    private String supplierName;
    //采购数量
    private Integer quantity;
    //采购成本单价
    private Double costPrice;
    //采购总成本
    private Double totalCost;
    //采购订单状态
    private String status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    //供应商联系人姓名
    private String contact;
    //供应商联系电话
    private String phone;
    //供应商电子邮件
    private String email;
    //供应商地址
    private String address;
}

/*
*     WITH filtered_orders AS (
        SELECT id, supplier_id, total_cost, status, created_at
        FROM purchase_order
        WHERE status = #{status}
          AND created_at BETWEEN #{startDate} AND #{endDate}
    )
    SELECT
        fo.id AS Id,
        p.name AS productName,
        s.name AS supplierName,
        pi.quantity,
        pi.cost_price AS costPrice,
        fo.total_cost AS totalCost,
        fo.status,
        fo.created_at AS createdAt*/