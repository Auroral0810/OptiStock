package com.auroral.vo.salestatisticVo;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 退款原因分布VO
 */
@Data
public class RefundReasonVO {
    private String name;           // 退款原因
    private BigDecimal value;      // 占比（%）
    private int count;             // 退款数量
    private BigDecimal amount;     // 退款金额
}
