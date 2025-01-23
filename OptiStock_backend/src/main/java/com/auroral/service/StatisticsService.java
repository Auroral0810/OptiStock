package com.auroral.service;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.dto.PurchaseStatisticsDTO;
import com.auroral.dto.SalesStatisticsDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.vo.OverviewStatisticsVO;
import com.auroral.vo.purchsaestatisticsVo.PurchaseStatisticsVO;
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

    PurchaseStatisticsVO getPurchaseStatistics(PurchaseStatisticsDTO purchaseStatisticsDTO);
    /**
     * 获取系统概览数据
     * @return OverviewStatisticsVO
     */
    ResponseResult getOverviewStatistics();


}
