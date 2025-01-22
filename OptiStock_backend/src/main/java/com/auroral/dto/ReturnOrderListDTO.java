package com.auroral.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;
import java.util.List;

@Data
public class ReturnOrderListDTO {
    private Integer pageNum;
    private Integer pageSize;
    private FilterForm filterForm;

    @Data
    public static class FilterForm {
        private String orderNumber;
        private String productName;
        private String returnStatus;
        @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
        private List<String> dateRange;
    }
}
