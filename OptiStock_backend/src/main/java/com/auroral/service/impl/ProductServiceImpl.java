package com.auroral.service.impl;

import com.auroral.entity.Product;
import com.auroral.entity.ResponseResult;
import com.auroral.mapper.ProductMapper;
import com.auroral.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品信息表(Product)表服务实现类
 *
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


}

