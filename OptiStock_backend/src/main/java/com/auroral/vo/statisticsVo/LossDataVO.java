package com.auroral.vo.statisticsVo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LossDataVO {
    private String date;
    private BigDecimal value;
}
