package com.auroral.dto;

import lombok.Data;

@Data
public class ProductFilterDTO {
    private String name;         // 商品名称
    private String categoryName; // 分类名称
    private String status;       // 状态
    private String sku;          // SKU 编号
}