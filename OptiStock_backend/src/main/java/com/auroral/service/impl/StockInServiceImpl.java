package com.auroral.service.impl;

import com.auroral.entity.StockIn;
import com.auroral.mapper.StockInMapper;
import com.auroral.service.StockInService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 库存入库记录表(StockIn)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:06:48
 */
@Service("stockInService")
public class StockInServiceImpl extends ServiceImpl<StockInMapper, StockIn> implements StockInService {

}

