package com.auroral.vo.salestatisticVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopProductVO {
    private String name;
    private int value; // 销售数量
    private BigDecimal amount; // 销售金额
    private int orderCount; // 订单数
}
