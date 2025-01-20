package com.auroral.mapper;

import com.auroral.entity.ProductCategory;
import com.auroral.vo.ProductCategoryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


/**
 * 商品分类表，存储商品的层级分类(ProductCategory)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-20 12:00:36
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    @Update({
            "<script>",
            "UPDATE product_category",
            "SET",
            "  <if test='name != null'> name = #{name}, </if>",
            "  parent_id = ",
            "  <choose>",
            "    <when test='parentId == null or parentId == \"\"'> NULL </when>",
            "    <otherwise> #{parentId} </otherwise>",
            "  </choose>",
            "WHERE id = #{id}",
            "</script>"
    })
    void updateCategoryNameAndParentId(@Param("id") Long id, @Param("name") String name, @Param("parentId") Long parentId);
}

