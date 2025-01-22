package com.auroral.service.impl;

import com.auroral.entity.ReturnOrders;
import com.auroral.mapper.ReturnOrdersMapper;
import com.auroral.service.ReturnOrdersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 退货订单表(ReturnOrders)表服务实现类
 *
 * @author makejava
 * @since 2025-01-22 14:55:08
 */
@Service("returnOrdersService")
public class ReturnOrdersServiceImpl extends ServiceImpl<ReturnOrdersMapper, ReturnOrders> implements ReturnOrdersService {

}

