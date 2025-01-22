package com.auroral.service;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.vo.statisticsVo.InventoryStatisticsVO;

public interface StatisticsService {
    InventoryStatisticsVO getInventoryStatistics(InventoryStatisticsDTO dto);
}
