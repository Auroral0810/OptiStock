package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.util.List;

@Data
public class SalesStatisticsVO {
    private BasicStatsSaleVO basicStats;               // 基础统计
    private List<SalesTrendVO> salesTrend;         // 销售趋势
    private List<ProductSalesVO> topProducts;      // 热销商品TOP10
    private List<CategorySalesVO> categorySales;   // 销售分类占比
    private List<PlatformOrdersVO> platformOrders; // 平台订单占比
    private RefundStatsVO refundStats;             // 退款统计
}