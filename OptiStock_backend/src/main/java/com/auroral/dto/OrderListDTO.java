package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private FilterForm filterForm;

    @Data
    @NoArgsConstructor
    public static class FilterForm {
        private String platform;
        private String status;
        private String keyword;//客户名称或者商品名称
        private List<String> dateRange;
    }
}
