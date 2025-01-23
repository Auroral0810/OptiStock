package com.auroral.vo;

import lombok.Data;

@Data
public class DefaultDateRangeVO {
    private String min_sa;     // stock_adjustments 最早日期
    private String max_sa;     // stock_adjustments 最迟日期
    private String min_si;     // stock_in 最早日期
    private String max_si;     // stock_in 最迟日期
    private String min_so;     // stock_out 最早日期
    private String max_so;     // stock_out 最迟日期
    private String min_order;  // order 最早日期
    private String max_order;  // order 最迟日期
}
