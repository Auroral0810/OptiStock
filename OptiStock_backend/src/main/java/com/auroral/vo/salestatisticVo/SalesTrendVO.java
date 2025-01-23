package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售趋势VO
 */
@Data
public class SalesTrendVO {
    private String date;            // 日期，格式：yyyy-MM
    private BigDecimal value;      // 销售额
    private int orderCount;        // 订单数
    private BigDecimal averageOrder; // 客单价
}