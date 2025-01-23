package com.auroral.vo.purchsaestatisticsVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyTrendVO {
    private String month;
    private BigDecimal amount;
}