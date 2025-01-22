package com.auroral.service.impl;

import com.auroral.entity.PurchaseItems;
import com.auroral.mapper.PurchaseItemsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.auroral.service.PurchaseItemsService;

/**
 * 采购商品详情表(PurchaseItems)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 23:03:34
 */
@Service("purchaseItemsService")
public class PurchaseItemsServiceImpl extends ServiceImpl<PurchaseItemsMapper, PurchaseItems> implements PurchaseItemsService {

}

