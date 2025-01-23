package com.auroral.vo.statisticsVo;

import com.auroral.vo.ProductVo;
import lombok.Data;

import java.util.List;

@Data
public class InventoryStatisticsVO {
    private BasicStatsVO basicStats;
    private List<LossDataVO> lossData;
    private List<StockFlowVO> stockFlow;
    private List<ValueDistributionVO> valueDistribution;
    private List<ProductVO> topTenProducts;
    private List<ProductVO> bottomTenProducts;
    private List<WarningDistributionVO> warningDistribution;
    private List<CategoryDistributionVO> categoryDistribution;
}

