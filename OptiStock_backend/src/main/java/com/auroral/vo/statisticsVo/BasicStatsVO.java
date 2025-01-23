package com.auroral.vo.statisticsVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasicStatsVO {
    private int totalInventory;
    private BigDecimal totalInventoryPercentage;
    private BigDecimal totalValue;
    private BigDecimal totalValuePercentage;
    private int lowStockCount;
    private BigDecimal lowStockPercentage;
    private BigDecimal turnoverRate;
    private BigDecimal turnoverPercentage;
}
