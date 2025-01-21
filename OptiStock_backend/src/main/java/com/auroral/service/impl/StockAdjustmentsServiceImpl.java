package com.auroral.service.impl;

import com.auroral.entity.StockAdjustments;
import com.auroral.mapper.StockAdjustmentsMapper;
import com.auroral.service.StockAdjustmentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 库存调整记录表(StockAdjustments)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:07:13
 */
@Service("stockAdjustmentsService")
public class StockAdjustmentsServiceImpl extends ServiceImpl<StockAdjustmentsMapper, StockAdjustments> implements StockAdjustmentsService {

}

