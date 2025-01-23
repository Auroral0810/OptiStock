package com.auroral.vo.salestatisticVo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 退款商品类别分析VO
 */
@Data
public class RefundCategoryVO {
    private String name;            // 商品类别
    private int refundCount;        // 退款数量
    private BigDecimal refundRate;  // 退款率（%）
    private BigDecimal refundAmount;// 退款金额
}
