package com.auroral.dto;


import lombok.Data;

@Data
public class AddPurchaseOrderDTO {
    //商品id
    private Long productId;
    //采购数量
    private Integer quantity;
    //采购成本单价
    private Double costPrice;
    //采购总价格
    private Double totalPrice;
    //供应商ID
    private Long supplierId;
}


