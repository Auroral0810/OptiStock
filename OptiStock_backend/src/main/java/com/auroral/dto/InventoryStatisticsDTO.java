package com.auroral.dto;

import lombok.Data;

@Data
public class InventoryStatisticsDTO {
    // 例如，可以添加时间范围、分类筛选等参数
    private String startDate;
    private String endDate;
}
