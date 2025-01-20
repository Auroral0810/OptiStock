package com.auroral.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageVo {
    private List<?> rows;
    private Long total;
}
