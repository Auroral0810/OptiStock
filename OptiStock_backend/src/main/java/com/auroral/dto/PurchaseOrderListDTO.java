package com.auroral.dto;

import lombok.Data;
import java.util.List;

@Data
public class PurchaseOrderListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private FilterForm filterForm;

    @Data
    public static class FilterForm {
        private String supplierId;
        private String status;
        private List<String> dateRange;
    }
}
