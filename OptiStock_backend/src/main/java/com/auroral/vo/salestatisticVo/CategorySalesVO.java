package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售分类占比VO
 */
@Data
public class CategorySalesVO {
    private String name;           // 分类名称
    private int value;             // 占比（%）
    private BigDecimal amount;     // 销售金额
    private int orderCount;        // 订单数
}