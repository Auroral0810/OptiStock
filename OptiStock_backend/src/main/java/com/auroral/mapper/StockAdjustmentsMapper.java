package com.auroral.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
/**
 * 库存调整记录表(StockAdjustments)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-21 12:07:13
 */
import com.auroral.entity.StockAdjustments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockAdjustmentsMapper extends BaseMapper<StockAdjustments> {
    // 自定义查询方法（如果有）
}

