package com.auroral.dto;

import lombok.Data;

@Data
public class StockFilterDTO {
    private String name;         // 商品名称
    private String sku;          // SKU 编号
}