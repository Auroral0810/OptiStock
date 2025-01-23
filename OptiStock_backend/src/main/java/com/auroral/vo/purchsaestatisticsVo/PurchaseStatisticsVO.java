package com.auroral.vo.purchsaestatisticsVo;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseStatisticsVO {
    private BigDecimal totalAmount;
    private Integer orderCount;
    private Integer supplierCount;
    private Double averageDeliveryTime;
    private Integer deliveryTimePercentage; // 新增字段
    private List<MonthlyTrendVO> monthlyTrend;
    private List<CategoryDataVO> categoryData;
    private List<SupplierRankingVO> supplierRanking;

    // Getters and Setters

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getSupplierCount() {
        return supplierCount;
    }

    public void setSupplierCount(Integer supplierCount) {
        this.supplierCount = supplierCount;
    }

    public Double getAverageDeliveryTime() {
        return averageDeliveryTime;
    }

    public void setAverageDeliveryTime(Double averageDeliveryTime) {
        this.averageDeliveryTime = averageDeliveryTime;
    }

    public Integer getDeliveryTimePercentage() {
        return deliveryTimePercentage;
    }

    public void setDeliveryTimePercentage(Integer deliveryTimePercentage) {
        this.deliveryTimePercentage = deliveryTimePercentage;
    }

    public List<MonthlyTrendVO> getMonthlyTrend() {
        return monthlyTrend;
    }

    public void setMonthlyTrend(List<MonthlyTrendVO> monthlyTrend) {
        this.monthlyTrend = monthlyTrend;
    }

    public List<CategoryDataVO> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(List<CategoryDataVO> categoryData) {
        this.categoryData = categoryData;
    }

    public List<SupplierRankingVO> getSupplierRanking() {
        return supplierRanking;
    }

    public void setSupplierRanking(List<SupplierRankingVO> supplierRanking) {
        this.supplierRanking = supplierRanking;
    }
}
