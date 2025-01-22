package com.auroral.service.impl;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.mapper.StatisticsMapper;
import com.auroral.service.StatisticsService;
import com.auroral.vo.*;
import com.auroral.vo.statisticsVo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public InventoryStatisticsVO getInventoryStatistics(InventoryStatisticsDTO dto) {
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();

        // 获取基本统计数据
        BasicStatsVO basicStats = statisticsMapper.getBasicStats();

        // 获取损耗率趋势数据
        List<LossDataVO> lossData = statisticsMapper.getLossData(startDate, endDate);

        // 获取出入库趋势数据
        List<StockFlowVO> stockFlow = statisticsMapper.getStockFlow(startDate, endDate);

        // 获取商品价值分布
        List<ValueDistributionVO> valueDistribution = statisticsMapper.getValueDistribution();

        // 获取库存最多的十大商品
        List<ProductVO> topTenProducts = statisticsMapper.getTopTenProducts();

        // 获取库存最少的十大商品
        List<ProductVO> bottomTenProducts = statisticsMapper.getBottomTenProducts();

        // 获取库存预警分布
        List<WarningDistributionVO> warningDistribution = statisticsMapper.getWarningDistribution();

        // 获取库存分类占比
        List<CategoryDistributionVO> categoryDistribution = statisticsMapper.getCategoryDistribution();

        // 计算总库存价值
        BigDecimal totalInventoryValue = statisticsMapper.getTotalInventoryValue();

        // 计算库存调整总价值
        BigDecimal stockAdjustmentTotalValue = statisticsMapper.getStockAdjustmentTotalValue(startDate, endDate);

        // 计算已发货订单商品总价值
        BigDecimal shippedOrdersTotalValue = statisticsMapper.getShippedOrdersTotalValue(startDate, endDate);

        // 计算损耗率
        BigDecimal denominator = totalInventoryValue.add(stockAdjustmentTotalValue).add(shippedOrdersTotalValue);
        BigDecimal lossRate = denominator.compareTo(BigDecimal.ZERO) > 0 ?
                stockAdjustmentTotalValue.divide(denominator, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) :
                BigDecimal.ZERO;

        // 计算周转率
        BigDecimal turnoverRate = denominator.compareTo(BigDecimal.ZERO) > 0 ?
                shippedOrdersTotalValue.divide(denominator, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)) :
                BigDecimal.ZERO;

        // 设置基本统计数据
        basicStats.setTotalValuePercentage(lossRate);
        basicStats.setTurnoverRate(turnoverRate);

        // 构建返回对象
        InventoryStatisticsVO vo = new InventoryStatisticsVO();
        vo.setBasicStats(basicStats);
        vo.setLossData(lossData);
        vo.setStockFlow(stockFlow);
        vo.setValueDistribution(valueDistribution);
        vo.setTopTenProducts(topTenProducts);
        vo.setBottomTenProducts(bottomTenProducts);
        vo.setWarningDistribution(warningDistribution);
        vo.setCategoryDistribution(categoryDistribution);

        return vo;
    }
}
