package com.auroral.vo.salestatisticVo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 退款TOP10商品VO
 */
@Data
public class RefundTopProductVO {
    private String name;            // 商品名称
    private int refundCount;        // 退款数量
    private BigDecimal refundRate;  // 退款率（%）
    private BigDecimal refundAmount;// 退款金额
}
