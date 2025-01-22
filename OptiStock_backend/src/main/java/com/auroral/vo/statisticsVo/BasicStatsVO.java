package com.auroral.vo.statisticsVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BasicStatsVO {
    private Integer totalInventory;
    private BigDecimal totalInventoryPercentage;
    private BigDecimal totalValue;
    private BigDecimal totalValuePercentage;
    private Integer lowStockCount;
    private BigDecimal lowStockPercentage;
    private BigDecimal turnoverRate;
    private BigDecimal turnoverPercentage;
}
