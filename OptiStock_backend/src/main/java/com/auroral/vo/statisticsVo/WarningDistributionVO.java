package com.auroral.vo.statisticsVo;

import lombok.Data;

@Data
public class WarningDistributionVO {
    private String name; // '严重不足', '库存偏低', '即将不足'
    private int value;    // 对应 SQL 中的 value
}
