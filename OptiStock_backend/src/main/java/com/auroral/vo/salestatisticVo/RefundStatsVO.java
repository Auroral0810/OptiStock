package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.util.List;
/**
 * 退款统计VO
 */
@Data
public class RefundStatsVO {
    private List<RefundTrendVO> refundTrend;               // 退款趋势
    private List<RefundReasonVO> refundReasons;             // 退款原因分布
    private List<RefundCategoryVO> refundCategories;       // 退款商品类别分析
    private List<RefundTopProductVO> refundTopProducts;    // 退款TOP10商品
}