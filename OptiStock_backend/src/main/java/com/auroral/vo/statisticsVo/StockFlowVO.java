package com.auroral.vo.statisticsVo;

import lombok.Data;

@Data
public class StockFlowVO {
    private String date;
    private Integer inbound;
    private Integer outbound;
}
