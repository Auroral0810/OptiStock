package com.auroral.service;

import com.auroral.dto.AdjustStockDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.StockAdjustments;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 库存调整记录表(StockAdjustments)表服务接口
 *
 * @author makejava
 * @since 2025-01-21 12:07:13
 */
public interface StockAdjustmentsService extends IService<StockAdjustments> {

    ResponseResult adjustStock(AdjustStockDTO adjustStockDTO);

    ResponseResult getStockAdjustRecord(Long id,String timeRange);
}

