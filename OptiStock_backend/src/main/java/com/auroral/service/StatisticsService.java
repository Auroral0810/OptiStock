package com.auroral.service;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.dto.SalesStatisticsDTO;
import com.auroral.vo.salestatisticVo.SalesStatisticsVO;
import com.auroral.vo.statisticsVo.InventoryStatisticsVO;

public interface StatisticsService {
    InventoryStatisticsVO getInventoryStatistics(InventoryStatisticsDTO dto);

    /**
     * 获取销售统计数据
     *
     * @param salesStatisticsDTO 请求参数DTO
     * @return 销售统计响应VO
     */
    SalesStatisticsVO getSalesStatistics(SalesStatisticsDTO salesStatisticsDTO);
}
