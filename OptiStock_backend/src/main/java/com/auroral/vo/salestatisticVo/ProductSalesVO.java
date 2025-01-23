package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 热销商品TOP10 VO
 */
@Data
public class ProductSalesVO {
    private String name;           // 商品名称
    private int value;             // 销售数量
    private BigDecimal amount;     // 销售金额
    private int orderCount;        // 订单数
}