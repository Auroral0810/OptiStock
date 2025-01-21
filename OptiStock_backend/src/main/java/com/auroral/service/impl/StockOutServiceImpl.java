package com.auroral.service.impl;

import com.auroral.entity.StockOut;
import com.auroral.mapper.StockOutMapper;
import com.auroral.service.StockOutService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 库存出库记录表(StockOut)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:06:59
 */
@Service("stockOutService")
public class StockOutServiceImpl extends ServiceImpl<StockOutMapper, StockOut> implements StockOutService {

}

