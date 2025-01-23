package com.auroral.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OverviewStatisticsVO {
    private int productCount;            // 商品总数
    private int warningProductCount;     // 预警商品数量
    private BigDecimal monthlyRevenue;   // 月度营收额
    private int orderCount;              // 订单数量
}