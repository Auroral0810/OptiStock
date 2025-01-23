package com.auroral.vo.salestatisticVo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 退款趋势VO
 */
@Data
public class RefundTrendVO {
    private String date;            // 日期，格式：yyyy-MM
    private BigDecimal value;      // 退款金额
    private BigDecimal rate;       // 退款率（%）
    private int refundCount;       // 退款订单数
}
