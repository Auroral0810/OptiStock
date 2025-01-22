package com.auroral.mapper;

import com.auroral.entity.Product;
import com.auroral.vo.ProductNameVo;
import com.auroral.vo.SupplierAndProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * 商品信息表(Product)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT COUNT(*) FROM product WHERE category_id = #{categoryId}")
    Integer countProductsByCategoryId(@Param("categoryId") Long categoryId);

    @Select("SELECT id AS productId, name AS productName,cost_price AS costPrice FROM product")
    List<SupplierAndProductVo.ProductInfo> getAllProducts();

    @Select("SELECT id AS productId, name AS productName,price AS price, stock_quantity AS stockQuantity FROM product")
    List<ProductNameVo.ProductInfo> getProductName();
}

