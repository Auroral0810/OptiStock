package com.auroral.service.impl;

import com.auroral.dto.AdjustStockDTO;
import com.auroral.entity.Product;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.StockAdjustments;
import com.auroral.mapper.ProductMapper;
import com.auroral.mapper.StockAdjustmentsMapper;
import com.auroral.service.StockAdjustmentsService;
import com.auroral.utils.BeanCopyUtils;
import com.auroral.vo.StockAdjustVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存调整记录表(StockAdjustments)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:07:13
 */
@Service("stockAdjustmentsService")
public class StockAdjustmentsServiceImpl extends ServiceImpl<StockAdjustmentsMapper, StockAdjustments> implements StockAdjustmentsService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    @Transactional
    public ResponseResult adjustStock(AdjustStockDTO adjustStockDTO) {
        /*思路：
        * 1.解析DTO中的数据，获取调整的id和数量
        * 2.将其转换成实体类对象
        * 3.插入adjustments表
        * 4.更新product表中对应商品的库存数量,要求是减少对应的库存值
        * 5.返回成功信息
        * */
        //1
        Long id = adjustStockDTO.getProductId();
        Integer quantity = adjustStockDTO.getQuantity();
        //2
        StockAdjustments adjustments = BeanCopyUtils.copyBean(adjustStockDTO, StockAdjustments.class);
        Product product = new Product();
        product.setId(id);
        product.setStockQuantity(adjustStockDTO.getCurrentStock() - quantity);
        //3
        save(adjustments);
        //4减少对应id产品的库存数量，减少数量等于quantity
        productMapper.updateById(product);
        //5
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult getStockAdjustRecord(Long id,String timeRange) {
        //1.根据id和时间范围查询adjustments表，timeRange的值为week、month、year，表示查询最近一周、一月、一年的调整记录

        QueryWrapper<StockAdjustments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", id);

        // 使用数据库函数计算时间
        if ("week".equals(timeRange)) {
            queryWrapper.apply("created_at >= DATE_SUB(NOW(), INTERVAL 1 WEEK)");
        } else if ("month".equals(timeRange)) {
            queryWrapper.apply("created_at >= DATE_SUB(NOW(), INTERVAL 1 MONTH)");
        } else if ("year".equals(timeRange)) {
            queryWrapper.apply("created_at >= DATE_SUB(NOW(), INTERVAL 1 YEAR)");
        }
        List<StockAdjustments> adjustmentsList = list(queryWrapper);
        //2.封装vo对象
        List<StockAdjustVo> stockAdjustVoList = BeanCopyUtils.copyBeanList(adjustmentsList, StockAdjustVo.class);
        //3.返回vo对象
        return ResponseResult.okResult(stockAdjustVoList);
    }


}

