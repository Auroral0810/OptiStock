package com.auroral.dto;

import lombok.Data;

@Data
public class InventoryStatisticsDTO {
    // 例如，可以添加时间范围、分类筛选等参数
    //对时间进行判断，如果为空就设置默认值
    private String startDate;
    private String endDate;
}
