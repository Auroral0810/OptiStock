package com.auroral.service.impl;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.dto.PurchaseStatisticsDTO;
import com.auroral.dto.SalesStatisticsDTO;
import com.auroral.mapper.StatisticsMapper;
import com.auroral.service.StatisticsService;
import com.auroral.vo.*;
import com.auroral.vo.purchsaestatisticsVo.CategoryDataVO;
import com.auroral.vo.purchsaestatisticsVo.MonthlyTrendVO;
import com.auroral.vo.purchsaestatisticsVo.PurchaseStatisticsVO;
import com.auroral.vo.purchsaestatisticsVo.SupplierRankingVO;
import com.auroral.vo.salestatisticVo.*;
import com.auroral.vo.statisticsVo.*;
import com.auroral.vo.statisticsVo.BasicStatsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public InventoryStatisticsVO getInventoryStatistics(InventoryStatisticsDTO dto) {
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();

        // 处理时间范围参数
        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = statisticsMapper.getMinCreatedAtForStockAdjustments();
            if (startDate == null) {
                startDate = "1970-01-01"; // 设置一个默认的最早日期
            }
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = statisticsMapper.getMaxCreatedAtForStockAdjustments();
            if (endDate == null) {
                endDate = "9999-12-31"; // 设置一个默认的最迟日期
            }
        }

        // 获取基本统计数据
        BasicStatsVO basicStats = statisticsMapper.getBasicStats();
        if (basicStats == null) {
            basicStats = new BasicStatsVO();
            basicStats.setTotalInventory(0);
            basicStats.setTotalInventoryPercentage(BigDecimal.ZERO);
            basicStats.setTotalValue(BigDecimal.ZERO);
            basicStats.setTotalValuePercentage(BigDecimal.ZERO);
            basicStats.setLowStockCount(0);
            basicStats.setLowStockPercentage(BigDecimal.ZERO);
            basicStats.setTurnoverRate(BigDecimal.ZERO);
            basicStats.setTurnoverPercentage(BigDecimal.ZERO);
        }

        // 获取损耗率趋势数据
        List<LossDataVO> lossData = statisticsMapper.getLossData(startDate, endDate);
        if (lossData == null) {
            lossData = Collections.emptyList();
        }

        // 获取出入库趋势数据
        List<StockFlowVO> stockFlow = statisticsMapper.getStockFlow(startDate, endDate);
        if (stockFlow == null) {
            stockFlow = Collections.emptyList();
        }

        // 获取商品价值分布
        List<ValueDistributionVO> valueDistribution = statisticsMapper.getValueDistribution();
        if (valueDistribution == null) {
            valueDistribution = Collections.emptyList();
        }

        // 获取库存最多的十大商品
        List<ProductVO> topTenProducts = statisticsMapper.getTopTenProducts();
        if (topTenProducts == null) {
            topTenProducts = Collections.emptyList();
        }

        // 获取库存最少的十大商品
        List<ProductVO> bottomTenProducts = statisticsMapper.getBottomTenProducts();
        if (bottomTenProducts == null) {
            bottomTenProducts = Collections.emptyList();
        }

        // 获取库存预警分布
        List<WarningDistributionVO> warningDistribution = statisticsMapper.getWarningDistribution();
        if (warningDistribution == null) {
            warningDistribution = Collections.emptyList();
        }

        // 获取库存分类占比
        List<CategoryDistributionVO> categoryDistribution = statisticsMapper.getCategoryDistribution();
        if (categoryDistribution == null) {
            categoryDistribution = Collections.emptyList();
        }

        // 计算总库存价值
        BigDecimal totalInventoryValue = statisticsMapper.getTotalInventoryValue();
        if (totalInventoryValue == null) {
            totalInventoryValue = BigDecimal.ZERO;
        }

        // 计算库存调整总价值
        BigDecimal stockAdjustmentTotalValue = statisticsMapper.getStockAdjustmentTotalValue(startDate, endDate);
        if (stockAdjustmentTotalValue == null) {
            stockAdjustmentTotalValue = BigDecimal.ZERO;
        }

        // 计算已发货订单商品总价值
        BigDecimal shippedOrdersTotalValue = statisticsMapper.getShippedOrdersTotalValue(startDate, endDate);
        if (shippedOrdersTotalValue == null) {
            shippedOrdersTotalValue = BigDecimal.ZERO;
        }

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

    @Override
    public SalesStatisticsVO getSalesStatistics(SalesStatisticsDTO salesStatisticsDTO) {
        String startDate = salesStatisticsDTO.getStartDate();
        String endDate = salesStatisticsDTO.getEndDate();
        // 处理时间范围参数
        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = statisticsMapper.getMinCreatedAtForStockAdjustments();
            if (startDate == null) {
                startDate = "1970-01-01"; // 设置一个默认的最早日期
            }
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = statisticsMapper.getMaxCreatedAtForStockAdjustments();
            if (endDate == null) {
                endDate = "9999-12-31"; // 设置一个默认的最迟日期
            }
        }
        SalesStatisticsVO salesStatisticsVO = new SalesStatisticsVO();

        // 基础统计
        BasicSalesStatsVO basicSalesStats = statisticsMapper.getBasicSalesStats(startDate, endDate);
        BasicStatsSaleVO basicStatsSaleVO = new BasicStatsSaleVO();
        basicStatsSaleVO.setTotalSales(basicSalesStats.getTotalSales());
        basicStatsSaleVO.setTotalSalesPercentage(basicSalesStats.getTotalSalesPercentage());
        basicStatsSaleVO.setOrderCount(basicSalesStats.getOrderCount());
        basicStatsSaleVO.setOrderCountPercentage(basicSalesStats.getOrderCountPercentage());
        basicStatsSaleVO.setAverageOrder(basicSalesStats.getAverageOrder());
        basicStatsSaleVO.setAverageOrderPercentage(basicSalesStats.getAverageOrderPercentage());
        basicStatsSaleVO.setGrowthRate(basicSalesStats.getGrowthRate());
        basicStatsSaleVO.setGrowthRatePercentage(basicSalesStats.getGrowthRatePercentage());
        salesStatisticsVO.setBasicStats(basicStatsSaleVO);

        // 销售趋势
        List<SalesTrendVO> salesTrend = statisticsMapper.getSalesTrend(startDate, endDate);
        salesStatisticsVO.setSalesTrend(salesTrend);

        // 热销商品TOP10
        List<ProductSalesVO> topProducts = statisticsMapper.getTopProducts(startDate, endDate);
        salesStatisticsVO.setTopProducts(topProducts);

        // 销售分类占比
        List<CategorySalesVO> categorySales = statisticsMapper.getCategorySales(startDate, endDate);
        salesStatisticsVO.setCategorySales(categorySales);

        // 平台订单占比
        List<PlatformOrdersVO> platformOrders = statisticsMapper.getPlatformOrders(startDate, endDate);
        salesStatisticsVO.setPlatformOrders(platformOrders);

        // 退款统计
        RefundStatsVO refundStatsVO = new RefundStatsVO();
        List<RefundTrendVO> refundTrend = statisticsMapper.getRefundTrend(startDate, endDate);
        List<RefundReasonVO> refundReasons = statisticsMapper.getRefundReasons(startDate, endDate);
        List<RefundCategoryVO> refundCategories = statisticsMapper.getRefundCategories(startDate, endDate);
        List<RefundTopProductVO> refundTopProducts = statisticsMapper.getRefundTopProducts(startDate, endDate);
        refundStatsVO.setRefundTrend(refundTrend);
        refundStatsVO.setRefundReasons(refundReasons);
        refundStatsVO.setRefundCategories(refundCategories);
        refundStatsVO.setRefundTopProducts(refundTopProducts);
        salesStatisticsVO.setRefundStats(refundStatsVO);

        return salesStatisticsVO;
    }

    private Random random = new Random();

    @Override
    public PurchaseStatisticsVO getPurchaseStatistics(PurchaseStatisticsDTO dto) {
        // 获取传入的 startDate 和 endDate
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();

        // 处理时间范围参数
        if (startDate == null || startDate.trim().isEmpty()) {
            startDate = statisticsMapper.getMinCreatedAt();
            if (startDate == null || startDate.trim().isEmpty()) {
                startDate = "1970-01-01"; // 设置一个默认的最早日期
            }
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            endDate = statisticsMapper.getMaxCreatedAt();
            if (endDate == null || endDate.trim().isEmpty()) {
                endDate = "9999-12-31"; // 设置一个默认的最迟日期
            }
        }

        PurchaseStatisticsVO vo = new PurchaseStatisticsVO();

        // 获取总采购金额
        BigDecimal totalAmount = statisticsMapper.getTotalPurchaseAmount(startDate, endDate);
        vo.setTotalAmount(totalAmount != null ? totalAmount : BigDecimal.ZERO);

        // 获取采购订单数
        Integer orderCount = statisticsMapper.getPurchaseOrderCount(startDate, endDate);
        vo.setOrderCount(orderCount != null ? orderCount : 0);

        // 获取供应商数量
        Integer supplierCount = statisticsMapper.getSupplierCount(startDate, endDate);
        vo.setSupplierCount(supplierCount != null ? supplierCount : 0);

        // 设置模拟的平均交付时间和交付时间百分比
        Double averageDeliveryTime = 3.0 + (7.0 - 3.0) * random.nextDouble(); // 模拟值在3到7天之间
        averageDeliveryTime = Math.round(averageDeliveryTime * 10.0) / 10.0; // 保留1位小数
        vo.setAverageDeliveryTime(averageDeliveryTime);

        Integer deliveryTimePercentage = 30 + random.nextInt(21); // 模拟值在30到50之间
        vo.setDeliveryTimePercentage(deliveryTimePercentage);

        // 获取月度采购趋势
        List<MonthlyTrendVO> monthlyTrend = statisticsMapper.getMonthlyPurchaseTrend(startDate, endDate);
        vo.setMonthlyTrend(monthlyTrend);

        // 获取采购品类分布
        List<CategoryDataVO> categoryData = statisticsMapper.getPurchaseCategoryDistribution(startDate, endDate);
        vo.setCategoryData(categoryData);

        // 获取供应商采购金额TOP10
        List<SupplierRankingVO> supplierRanking = statisticsMapper.getTop10SuppliersByPurchaseAmount(startDate, endDate);
        vo.setSupplierRanking(supplierRanking);

        return vo;
    }

}
