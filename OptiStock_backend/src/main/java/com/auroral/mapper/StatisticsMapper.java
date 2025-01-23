package com.auroral.mapper;

import com.auroral.vo.purchsaestatisticsVo.CategoryDataVO;
import com.auroral.vo.purchsaestatisticsVo.MonthlyTrendVO;
import com.auroral.vo.purchsaestatisticsVo.SupplierRankingVO;
import com.auroral.vo.salestatisticVo.*;
import com.auroral.vo.statisticsVo.BasicStatsVO;
import com.auroral.vo.statisticsVo.ProductVO;
import com.auroral.vo.statisticsVo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    // 获取总库存数量和总价值
    @Select("SELECT " +
            "COALESCE(SUM(stock_quantity), 0) AS totalInventory, " +
            "COALESCE(ROUND(SUM(stock_quantity) * 100 / NULLIF((SELECT SUM(stock_quantity) FROM product), 0), 2), 0) AS totalInventoryPercentage, " +
            "COALESCE(SUM(price * stock_quantity), 0) AS totalValue, " +
            "COALESCE(ROUND(SUM(price * stock_quantity) * 100 / NULLIF((SELECT SUM(price * stock_quantity) FROM product), 0), 2), 0) AS totalValuePercentage, " +
            "COALESCE(COUNT(CASE WHEN stock_quantity < warning_threshold THEN 1 END), 0) AS lowStockCount, " +
            "COALESCE(ROUND(COUNT(CASE WHEN stock_quantity < warning_threshold THEN 1 END) * 100 / NULLIF(COUNT(*), 0), 2), 0) AS lowStockPercentage, " +
            "COALESCE(3.5, 0) AS turnoverRate, " + // 模拟数据
            "COALESCE(75, 0) AS turnoverPercentage " + // 模拟数据
            "FROM product")
    BasicStatsVO getBasicStats();

    // 获取损耗率趋势数据
    @Select("SELECT DATE_FORMAT(sa.created_at, '%Y-%m') AS date, " +
            "COALESCE(SUM(sa.quantity * p.cost_price) / NULLIF(SUM(p.stock_quantity * p.price), 0) * 100, 0) AS value " +
            "FROM stock_adjustments sa " +
            "JOIN product p ON sa.product_id = p.id " +
            "WHERE sa.created_at BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE_FORMAT(sa.created_at, '%Y-%m') " +
            "ORDER BY date")
    List<LossDataVO> getLossData(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取出入库趋势数据
    @Select("SELECT " +
            "COALESCE(s.date, 'N/A') AS date, " +
            "COALESCE(SUM(s.inbound), 0) AS inbound, " +
            "COALESCE(SUM(s.outbound), 0) AS outbound " +
            "FROM (" +
            "    SELECT DATE_FORMAT(created_at, '%Y-%m') AS date, COALESCE(quantity, 0) AS inbound, 0 AS outbound " +
            "    FROM stock_in " +
            "    WHERE created_at BETWEEN #{startDate} AND #{endDate} " +
            "    UNION ALL " +
            "    SELECT DATE_FORMAT(created_at, '%Y-%m') AS date, 0 AS inbound, COALESCE(quantity, 0) AS outbound " +
            "    FROM stock_out " +
            "    WHERE created_at BETWEEN #{startDate} AND #{endDate} " +
            ") s " +
            "GROUP BY s.date " +
            "ORDER BY s.date")
    List<StockFlowVO> getStockFlow(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取商品价值分布
    @Select("SELECT " +
            "CASE " +
            "  WHEN p.price < 1000 THEN '0-1000' " +
            "  WHEN p.price BETWEEN 1000 AND 5000 THEN '1000-5000' " +
            "  WHEN p.price BETWEEN 5000 AND 10000 THEN '5000-10000' " +
            "  WHEN p.price BETWEEN 10000 AND 50000 THEN '10000-50000' " +
            "  ELSE '50000以上' " +
            "END AS priceRange, " +
            "COALESCE(COUNT(*), 0) AS countValue " +
            "FROM product p " +
            "GROUP BY priceRange")
    List<ValueDistributionVO> getValueDistribution();

    // 获取库存最多的十大商品
    @Select("SELECT p.name, COALESCE(p.stock_quantity, 0) AS value " +
            "FROM product p " +
            "ORDER BY p.stock_quantity DESC " +
            "LIMIT 10")
    List<ProductVO> getTopTenProducts();

    // 获取库存最少的十大商品
    @Select("SELECT p.name, COALESCE(p.stock_quantity, 0) AS value " +
            "FROM product p " +
            "ORDER BY p.stock_quantity ASC " +
            "LIMIT 10")
    List<ProductVO> getBottomTenProducts();

    // 获取库存预警分布
    @Select("SELECT " +
            "CASE " +
            "  WHEN p.stock_quantity <= p.warning_threshold * 0.5 THEN '严重不足' " +
            "  WHEN p.stock_quantity <= p.warning_threshold THEN '库存偏低' " +
            "  ELSE '即将不足' " +
            "END AS name, " +
            "COALESCE(COUNT(*), 0) AS value " +
            "FROM product p " +
            "GROUP BY " +
            "CASE " +
            "  WHEN p.stock_quantity <= p.warning_threshold * 0.5 THEN '严重不足' " +
            "  WHEN p.stock_quantity <= p.warning_threshold THEN '库存偏低' " +
            "  ELSE '即将不足' " +
            "END")
    List<WarningDistributionVO> getWarningDistribution();

    // 获取库存分类占比
    @Select("SELECT pc.name, COALESCE(COUNT(*), 0) AS value " +
            "FROM product p " +
            "JOIN product_category pc ON p.category_id = pc.id " +
            "GROUP BY pc.name")
    List<CategoryDistributionVO> getCategoryDistribution();

    // 获取总库存价值
    @Select("SELECT COALESCE(SUM(p.price * p.stock_quantity), 0) FROM product p")
    BigDecimal getTotalInventoryValue();

    // 获取库存调整总价值
    @Select("SELECT COALESCE(SUM(sa.quantity * p.cost_price), 0) FROM stock_adjustments sa " +
            "JOIN product p ON sa.product_id = p.id " +
            "WHERE sa.created_at BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getStockAdjustmentTotalValue(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取已发货订单商品总价值
    @Select("SELECT COALESCE(SUM(oi.quantity * p.price), 0) FROM order_items oi " +
            "JOIN `order` o ON oi.order_id = o.id " +
            "JOIN product p ON oi.product_id = p.id " +
            "WHERE o.status IN ('已发货', '已完成') " +
            "AND o.created_at BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getShippedOrdersTotalValue(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取 stock_adjustments 表中的最早 created_at 日期
    @Select("SELECT COALESCE(MIN(created_at), '1970-01-01') FROM stock_adjustments")
    String getMinCreatedAtForStockAdjustments();

    // 获取 stock_adjustments 表中的最迟 created_at 日期
    @Select("SELECT COALESCE(MAX(created_at), '9999-12-31') FROM stock_adjustments")
    String getMaxCreatedAtForStockAdjustments();

    /**
     * 获取基础销售统计数据
     */
    @Select({
            "<script>",
            "SELECT",
            "    COALESCE(SUM(o.total_amount), 0) AS totalSales,",
            "    COALESCE(ROUND(SUM(o.total_amount) * 100 / NULLIF((SELECT SUM(o2.total_amount) FROM `order` o2 WHERE o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o2.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o2.created_at &lt;= #{endDate}",
            "</if>",
            "), 0), 2), 0) AS totalSalesPercentage,",
            "    COUNT(DISTINCT o.id) AS orderCount,",
            "    COALESCE(ROUND(COUNT(DISTINCT o.id) * 100 / NULLIF((SELECT COUNT(*) FROM `order` o3 WHERE o3.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o3.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o3.created_at &lt;= #{endDate}",
            "</if>",
            "), 0), 2), 0) AS orderCountPercentage,",
            "    COALESCE(AVG(o.total_amount), 0) AS averageOrder,",
            "    COALESCE(ROUND(AVG(o.total_amount) * 100 / NULLIF((SELECT AVG(o4.total_amount) FROM `order` o4 WHERE o4.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o4.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o4.created_at &lt;= #{endDate}",
            "</if>",
            "), 0), 2), 0) AS averageOrderPercentage,",
            "    COALESCE(ROUND((SUM(o.total_amount) - NULLIF((SELECT SUM(o5.total_amount) FROM `order` o5 WHERE o5.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o5.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o5.created_at &lt;= #{endDate}",
            "</if>",
            "), 0)) / NULLIF((SELECT SUM(o6.total_amount) FROM `order` o6 WHERE o6.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o6.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o6.created_at &lt;= #{endDate}",
            "</if>",
            "), 0), 2), 0) AS growthRate,",
            "    COALESCE(ROUND(((SUM(o.total_amount) / NULLIF((SELECT SUM(o7.total_amount) FROM `order` o7 WHERE o7.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o7.created_at &gt;= DATE_SUB(#{startDate}, INTERVAL 60 DAY)",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o7.created_at &lt;= DATE_SUB(#{startDate}, INTERVAL 30 DAY)",
            "</if>",
            "), 0)) - 1) * 100, 2), 0) AS growthRatePercentage",
            "FROM `order` o",
            "WHERE 1=1",
            " AND o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o.created_at &lt;= #{endDate}",
            "</if>",
            "</script>"
    })
    BasicSalesStatsVO getBasicSalesStats(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取销售趋势数据
     */
    @Select({
            "<script>",
            "SELECT",
            "    DATE_FORMAT(o.created_at, '%Y-%m') AS date,",
            "    COALESCE(SUM(o.total_amount), 0) AS value,",
            "    COUNT(DISTINCT o.id) AS orderCount,",
            "    COALESCE(AVG(o.total_amount), 0) AS averageOrder",
            "FROM `order` o",
            "WHERE o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o.created_at &lt;= #{endDate}",
            "</if>",
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')",
            "ORDER BY date",
            "</script>"
    })
    List<SalesTrendVO> getSalesTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取热销商品TOP10
     */
    @Select({
            "<script>",
            "SELECT p.name, SUM(oi.quantity) AS value, SUM(oi.quantity * oi.price) AS amount, COUNT(DISTINCT o.id) AS orderCount",
            "FROM order_items oi",
            "JOIN `order` o ON oi.order_id = o.id",
            "JOIN product p ON oi.product_id = p.id",
            "WHERE o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o.created_at &lt;= #{endDate}",
            "</if>",
            "GROUP BY p.name",
            "ORDER BY value DESC",
            "LIMIT 10",
            "</script>"
    })
    List<ProductSalesVO> getTopProducts(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取销售分类占比
     */
    @Select({
            "<script>",
            "SELECT pc.name, ROUND(SUM(o.total_amount) / NULLIF((SELECT SUM(o2.total_amount) FROM `order` o2 WHERE o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o2.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o2.created_at &lt;= #{endDate}",
            "</if>",
            "), 0) * 100, 2) AS value,",
            "    SUM(o.total_amount) AS amount,",
            "    COUNT(DISTINCT o.id) AS orderCount",
            "FROM `order` o",
            "JOIN order_items oi ON o.id = oi.order_id",
            "JOIN product p ON oi.product_id = p.id",
            "JOIN product_category pc ON p.category_id = pc.id",
            "WHERE o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o.created_at &lt;= #{endDate}",
            "</if>",
            "GROUP BY pc.name",
            "</script>"
    })
    List<CategorySalesVO> getCategorySales(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取平台订单占比
     */
    @Select({
            "<script>",
            "SELECT o.platform AS name,",
            "       ROUND(COUNT(*) / NULLIF((SELECT COUNT(*) FROM `order` o2 WHERE o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o2.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o2.created_at &lt;= #{endDate}",
            "</if>",
            "), 0) * 100, 2) AS value,",
            "       SUM(o.total_amount) AS amount,",
            "       COUNT(*) AS orderCount",
            "FROM `order` o",
            "WHERE o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o.created_at &lt;= #{endDate}",
            "</if>",
            "GROUP BY o.platform",
            "</script>"
    })
    List<PlatformOrdersVO> getPlatformOrders(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取退款趋势数据，仅统计return_status为'同意'且订单状态符合条件的记录
     */
    @Select({
            "<script>",
            "SELECT",
            "    DATE_FORMAT(ro.return_date, '%Y-%m') AS date,",
            "    COALESCE(SUM(oi.total_price), 0) AS value,",
            "    COALESCE(ROUND((SUM(oi.total_price) / NULLIF((SELECT SUM(o2.total_amount) FROM `order` o2 WHERE o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o2.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o2.created_at &lt;= #{endDate}",
            "</if>",
            "), 0)) * 100, 2), 0) AS rate,",
            "    COUNT(DISTINCT ro.id) AS refundCount",
            "FROM return_orders ro",
            "JOIN order_items oi ON ro.order_id = oi.order_id AND ro.product_id = oi.product_id",
            "JOIN `order` o ON ro.order_id = o.id",
            "WHERE ro.return_status = '同意'",
            " AND o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro.return_date &lt;= #{endDate}",
            "</if>",
            "GROUP BY DATE_FORMAT(ro.return_date, '%Y-%m')",
            "ORDER BY date",
            "</script>"
    })
    List<RefundTrendVO> getRefundTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取退款原因分布，仅统计return_status为'同意'且订单状态符合条件的记录
     */
    @Select({
            "<script>",
            "SELECT",
            "    ro.return_reason AS name,",
            "    ROUND(COUNT(*) / NULLIF((SELECT COUNT(*) FROM return_orders ro2",
            " JOIN `order` o2 ON ro2.order_id = o2.id",
            " WHERE ro2.return_status = '同意'",
            " AND o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro2.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro2.return_date &lt;= #{endDate}",
            "</if>",
            "), 0) * 100, 2) AS value,",
            "    COUNT(*) AS count,",
            "    COALESCE(SUM(oi.total_price), 0) AS amount",
            "FROM return_orders ro",
            "JOIN order_items oi ON ro.order_id = oi.order_id AND ro.product_id = oi.product_id",
            "JOIN `order` o ON ro.order_id = o.id",
            "WHERE ro.return_status = '同意'",
            " AND o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro.return_date &lt;= #{endDate}",
            "</if>",
            "GROUP BY ro.return_reason",
            "</script>"
    })
    List<RefundReasonVO> getRefundReasons(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取退款商品类别分析，仅统计return_status为'同意'且订单状态符合条件的记录
     */
    @Select({
            "<script>",
            "SELECT",
            "    pc.name AS name,",
            "    COUNT(ro.id) AS refundCount,",
            "    ROUND(COUNT(ro.id) / NULLIF((SELECT COUNT(*) FROM return_orders ro2",
            " JOIN `order` o2 ON ro2.order_id = o2.id",
            " WHERE ro2.return_status = '同意'",
            " AND o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro2.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro2.return_date &lt;= #{endDate}",
            "</if>",
            "), 0) * 100, 2) AS refundRate,",
            "    COALESCE(SUM(oi.total_price), 0) AS refundAmount",
            "FROM return_orders ro",
            "JOIN order_items oi ON ro.order_id = oi.order_id AND ro.product_id = oi.product_id",
            "JOIN product p ON oi.product_id = p.id",
            "JOIN product_category pc ON p.category_id = pc.id",
            "JOIN `order` o ON ro.order_id = o.id",
            "WHERE ro.return_status = '同意'",
            " AND o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro.return_date &lt;= #{endDate}",
            "</if>",
            "GROUP BY pc.name",
            "</script>"
    })
    List<RefundCategoryVO> getRefundCategories(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 获取退款TOP10商品，仅统计return_status为'同意'且订单状态符合条件的记录
     */
    @Select({
            "<script>",
            "SELECT",
            "    p.name AS name,",
            "    COUNT(ro.id) AS refundCount,",
            "    ROUND(COUNT(ro.id) / NULLIF((SELECT COUNT(*) FROM order_items oi2",
            " JOIN `order` o2 ON oi2.order_id = o2.id",
            " WHERE oi2.product_id = p.id",
            " AND o2.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND o2.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND o2.created_at &lt;= #{endDate}",
            "</if>",
            "), 0) * 100, 2) AS refundRate,",
            "    COALESCE(SUM(oi.total_price), 0) AS refundAmount",
            "FROM return_orders ro",
            "JOIN order_items oi ON ro.order_id = oi.order_id AND ro.product_id = oi.product_id",
            "JOIN product p ON oi.product_id = p.id",
            "JOIN `order` o ON ro.order_id = o.id",
            "WHERE ro.return_status = '同意'",
            " AND o.status IN ('待发货', '已发货', '已完成', '已付款')",
            "<if test='startDate != null and startDate != \"\"'>",
            " AND ro.return_date &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            " AND ro.return_date &lt;= #{endDate}",
            "</if>",
            "GROUP BY p.id, p.name", // 添加 p.id 到 GROUP BY
            "ORDER BY refundCount DESC",
            "LIMIT 10",
            "</script>"
    })
    List<RefundTopProductVO> getRefundTopProducts(@Param("startDate") String startDate, @Param("endDate") String endDate);
    // 获取总采购金额
    @Select({
            "<script>",
            "SELECT SUM(total_cost) FROM purchase_order",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "</script>"
    })
    BigDecimal getTotalPurchaseAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取采购订单数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM purchase_order",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "</script>"
    })
    Integer getPurchaseOrderCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取供应商数量
    @Select({
            "<script>",
            "SELECT COUNT(DISTINCT supplier_id) FROM purchase_order",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "</script>"
    })
    Integer getSupplierCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取月度采购趋势
    @Select({
            "<script>",
            "SELECT DATE_FORMAT(created_at, '%m月') AS month, SUM(total_cost) AS amount",
            "FROM purchase_order",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "GROUP BY DATE_FORMAT(created_at, '%m月')",
            "ORDER BY DATE_FORMAT(created_at, '%m月')",
            "</script>"
    })
    List<MonthlyTrendVO> getMonthlyPurchaseTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取采购品类分布
    @Select({
            "<script>",
            "SELECT pc.name, SUM(pi.total_price) AS value",
            "FROM purchase_order po",
            "JOIN purchase_items pi ON po.id = pi.purchase_order_id",
            "JOIN product p ON pi.product_id = p.id",
            "JOIN product_category pc ON p.category_id = pc.id",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "po.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND po.created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "GROUP BY pc.name",
            "</script>"
    })
    List<CategoryDataVO> getPurchaseCategoryDistribution(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取供应商采购金额TOP10
    @Select({
            "<script>",
            "SELECT s.name, SUM(po.total_cost) AS value",
            "FROM purchase_order po",
            "JOIN supplier s ON po.supplier_id = s.id",
            "<where>",
            "<if test='startDate != null and startDate != \"\"'>",
            "po.created_at &gt;= #{startDate}",
            "</if>",
            "<if test='endDate != null and endDate != \"\"'>",
            "AND po.created_at &lt;= #{endDate}",
            "</if>",
            "</where>",
            "GROUP BY s.name",
            "ORDER BY SUM(po.total_cost) DESC",
            "LIMIT 10",
            "</script>"
    })
    List<SupplierRankingVO> getTop10SuppliersByPurchaseAmount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // 获取最早的 created_at 日期
    @Select("SELECT DATE_FORMAT(MIN(created_at), '%Y-%m-%d') FROM purchase_order")
    String getMinCreatedAt();

    // 获取最晚的 created_at 日期
    @Select("SELECT DATE_FORMAT(MAX(created_at), '%Y-%m-%d') FROM purchase_order")
    String getMaxCreatedAt();
    /**
     * 获取状态为“上架”的商品总数
     */
    @Select("SELECT COUNT(*) FROM `product` WHERE `status` = '上架'")
    int getProductCount();

    /**
     * 获取状态为“上架”且库存数量小于预警阈值的商品数量
     */
    @Select("SELECT COUNT(*) FROM `product` WHERE `status` = '上架' AND `stock_quantity` < `warning_threshold`")
    int getWarningProductCount();

    /**
     * 获取状态不为“已取消”和“待付款”的订单数量
     */
    @Select("SELECT COUNT(*) FROM `order` WHERE `status` NOT IN ('已取消', '待付款')")
    int getOrderCount();

    /**
     * 获取过去一个月内，状态不为“已取消”和“待付款”的订单的总金额
     * @param startDate 起始日期（格式：yyyy-MM-dd）
     * @param endDate 结束日期（格式：yyyy-MM-dd）
     */
    @Select("SELECT IFNULL(SUM(`total_amount`), 0) FROM `order` " +
            "WHERE `status` NOT IN ('已取消', '待付款') " +
            "AND `created_at` BETWEEN #{startDate} AND #{endDate}")
    BigDecimal getMonthlyRevenue(@Param("startDate") String startDate, @Param("endDate") String endDate);
}