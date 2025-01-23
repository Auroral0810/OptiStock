package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StockFilterDTO {
    private String name;         // 商品名称
    private String sku;          // SKU 编号
}