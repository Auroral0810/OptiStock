package com.auroral.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddProductDTO {
    //商品名称
    private String name;
    //库存单位编号（SKU）
    private String sku;
    //商品售价
    private Double price;
    //商品成本价
    private Double costPrice;

    //计量单位，如件、盒、瓶等
    private String unit;
    //商品状态
    private String status;
    private Long categoryId;// 商品分类id
    private String categoryName;
}
