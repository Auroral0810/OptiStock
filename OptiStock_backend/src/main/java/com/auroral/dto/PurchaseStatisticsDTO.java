package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PurchaseStatisticsDTO {
    private String startDate;
    private String endDate;
}