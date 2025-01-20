package com.auroral.service;

import com.auroral.dto.AddCategoryRequest;
import com.auroral.dto.UpdateCategoryRequest;
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
    ResponseResult getAllProductCategory(Integer pageNum, Integer sizeSize);

    ResponseResult getParentCategoty();

    ResponseResult addCategory(AddCategoryRequest addCategoryRequest);

    ResponseResult deleteCategory(Long id);

    ResponseResult updateCategory(UpdateCategoryRequest updateCategoryRequest);
}

