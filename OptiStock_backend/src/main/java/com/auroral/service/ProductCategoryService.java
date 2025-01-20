package com.auroral.service;

import com.auroral.dto.AddCategoryDTO;
import com.auroral.dto.PageRequestDTO;
import com.auroral.dto.UpdateCategoryDTO;
import com.auroral.entity.ProductCategory;
import com.auroral.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 商品分类表，存储商品的层级分类(ProductCategory)表服务接口
 *
 * @author makejava
 * @since 2025-01-20 12:00:38
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    ResponseResult getAllProductCategory(PageRequestDTO pageRequestDTO);

    ResponseResult getParentCategoty();

    ResponseResult addCategory(AddCategoryDTO addCategoryDTO);

    ResponseResult deleteCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryDTO updateCategoryDTO);

}

