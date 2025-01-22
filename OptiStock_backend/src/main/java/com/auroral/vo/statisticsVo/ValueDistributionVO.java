package com.auroral.vo.statisticsVo;

import lombok.Data;

@Data
public class ValueDistributionVO {
    private String priceRange; // 对应 SQL 中的 priceRange
    private int countValue;     // 对应 SQL 中的 countValue
}
