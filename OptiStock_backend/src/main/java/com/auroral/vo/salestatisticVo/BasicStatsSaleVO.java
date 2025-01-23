package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 基础统计VO
 */
@Data
public class BasicStatsSaleVO {
    private BigDecimal totalSales;             // 总销售额
    private BigDecimal totalSalesPercentage;   // 销售额完成率
    private int orderCount;                     // 订单总数
    private BigDecimal orderCountPercentage;    // 订单数完成率
    private BigDecimal averageOrder;            // 客单价
    private BigDecimal averageOrderPercentage;  // 客单价环比
    private BigDecimal growthRate;              // 销售增长率
    private BigDecimal growthRatePercentage;    // 增长率环比
}