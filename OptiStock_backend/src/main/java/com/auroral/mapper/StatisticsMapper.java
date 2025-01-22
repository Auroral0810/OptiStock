package com.auroral.mapper;

import com.auroral.vo.ProductVo;
import com.auroral.vo.statisticsVo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    // 获取总库存数量和总价值
    @Select("SELECT SUM(stock_quantity) AS totalInventory, " +
            "SUM(stock_quantity * price) AS totalValue " +
            "FROM product")
    BasicStatsVO getBasicStats();

    // 获取损耗率趋势数据
    @Select("SELECT DATE_FORMAT(sa.created_at, '%Y-%m') AS date, " +
            "SUM(sa.quantity * p.cost_price) / SUM(p.stock_quantity * p.price) * 100 AS value " +
            "FROM stock_adjustments sa " +
            "JOIN product p ON sa.product_id = p.id " +
            "WHERE sa.created_at BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(sa.created_at, '%Y-%m') " +
            "ORDER BY date")
    List<LossDataVO> getLossData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取出入库趋势数据（修正后的查询）
    @Select("SELECT " +
            "s.date, " +
            "SUM(s.inbound) AS inbound, " +
            "SUM(s.outbound) AS outbound " +
            "FROM (" +
            "    SELECT DATE_FORMAT(created_at, '%Y-%m') AS date, quantity AS inbound, 0 AS outbound " +
            "    FROM stock_in " +
            "    WHERE created_at BETWEEN #{startDate} AND #{endDate} " +
            "    UNION ALL " +
            "    SELECT DATE_FORMAT(created_at, '%Y-%m') AS date, 0 AS inbound, quantity AS outbound " +
            "    FROM stock_out " +
            "    WHERE created_at BETWEEN #{startDate} AND #{endDate} " +
            ") s " +
            "GROUP BY s.date " +
            "ORDER BY s.date")
    List<StockFlowVO> getStockFlow(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取商品价值分布（更改别名以避免保留关键字）
    @Select("SELECT " +
            "CASE " +
            "  WHEN p.price < 1000 THEN '0-1000' " +
            "  WHEN p.price BETWEEN 1000 AND 5000 THEN '1000-5000' " +
            "  WHEN p.price BETWEEN 5000 AND 10000 THEN '5000-10000' " +
            "  WHEN p.price BETWEEN 10000 AND 50000 THEN '10000-50000' " +
            "  ELSE '50000以上' " +
            "END AS priceRange, " +
            "COUNT(*) AS countValue " +
            "FROM product p " +
            "GROUP BY priceRange")
    List<ValueDistributionVO> getValueDistribution();

    // 获取库存最多的十大商品
    @Select("SELECT p.name, p.stock_quantity AS value " +
            "FROM product p " +
            "ORDER BY p.stock_quantity DESC " +
            "LIMIT 10")
    List<ProductVO> getTopTenProducts();

    // 获取库存最少的十大商品
    @Select("SELECT p.name, p.stock_quantity AS value " +
            "FROM product p " +
            "ORDER BY p.stock_quantity ASC " +
            "LIMIT 10")
    List<ProductVO> getBottomTenProducts();

    // 获取库存预警分布（调整 GROUP BY 子句）
    @Select("SELECT " +
            "CASE " +
            "  WHEN p.stock_quantity <= p.warning_threshold * 0.5 THEN '严重不足' " +
            "  WHEN p.stock_quantity <= p.warning_threshold THEN '库存偏低' " +
            "  ELSE '即将不足' " +
            "END AS name, " +
            "COUNT(*) AS value " +
            "FROM product p " +
            "GROUP BY " +
            "CASE " +
            "  WHEN p.stock_quantity <= p.warning_threshold * 0.5 THEN '严重不足' " +
            "  WHEN p.stock_quantity <= p.warning_threshold THEN '库存偏低' " +
            "  ELSE '即将不足' " +
            "END")
    List<WarningDistributionVO> getWarningDistribution();

    // 获取库存分类占比
    @Select("SELECT pc.name, COUNT(*) AS value " +
            "FROM product p " +
            "JOIN product_category pc ON p.category_id = pc.id " +
            "GROUP BY pc.name")
    List<CategoryDistributionVO> getCategoryDistribution();

    // 获取总库存价值
    @Select("SELECT SUM(p.price * p.stock_quantity) FROM product p")
    BigDecimal getTotalInventoryValue();

    // 获取库存调整总价值
    @Select("SELECT SUM(sa.quantity * p.cost_price) FROM stock_adjustments sa " +
            "JOIN product p ON sa.product_id = p.id " +
            "WHERE sa.created_at BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getStockAdjustmentTotalValue(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取已发货订单商品总价值
    @Select("SELECT SUM(oi.quantity * p.price) FROM order_items oi " +
            "JOIN `order` o ON oi.order_id = o.id " +
            "JOIN product p ON oi.product_id = p.id " +
            "WHERE o.status IN ('已发货', '已完成') " +
            "AND o.created_at BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getShippedOrdersTotalValue(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
