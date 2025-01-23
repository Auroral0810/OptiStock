package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PurchaseOrderListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private FilterForm filterForm;

    @Data
    @NoArgsConstructor
    public static class FilterForm {
        private String supplierId;
        private String status;
        private List<String> dateRange;
    }
}
