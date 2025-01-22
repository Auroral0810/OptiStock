package com.auroral.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class ProductNameVo {
    private List<ProductNameVo.ProductInfo> productNameList;     // 商品列表

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductInfo {
        private Long productId;    // 商品ID
        private String productName; // 商品名称
        //商品销售价
        private Double price;
        //商品库存
        private Integer stockQuantity;
    }

}
