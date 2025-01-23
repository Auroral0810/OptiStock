package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 平台订单占比VO
 */
@Data
public class PlatformOrdersVO {
    private String name;           // 平台名称
    private int value;             // 占比（%）
    private BigDecimal amount;     // 销售金额
    private int orderCount;        // 订单数
}