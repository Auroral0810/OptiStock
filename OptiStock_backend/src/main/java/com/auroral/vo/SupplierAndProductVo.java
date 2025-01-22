package com.auroral.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAndProductVo {
    private List<ProductInfo> products;     // 商品列表
    private List<SupplierInfo> suppliers;   // 供应商列表

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private Long productId;    // 商品ID
        private String productName; // 商品名称
        //商品成本价
        private Double costPrice;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SupplierInfo {
        private Long supplierId;   // 供应商ID
        private String supplierName; // 供应商名称
    }
}
