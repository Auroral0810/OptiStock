package com.auroral.service;

import com.auroral.dto.AddProductDTO;
import com.auroral.dto.ProductListDTO;
import com.auroral.dto.StockListDTO;
import com.auroral.dto.UpdateProductDTO;
import com.auroral.entity.Product;
import com.auroral.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;

import java.util.Map;


/**
 * 商品信息表(Product)表服务接口
 *
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
public interface ProductService extends IService<Product> {
    public ResponseResult getSkuAndCategoryList() ;

    ResponseResult getProductList(ProductListDTO requestDTO);

    ResponseResult addProduct(AddProductDTO addProductDTO);

    ResponseResult deleteProduct(Long id);

    ResponseResult updateProduct( UpdateProductDTO updateProductDTO);

    ResponseResult getStockList(StockListDTO stockListDTO);

    ResponseResult getThresholdDataList(StockListDTO stockListDTO);

    ResponseResult updateThreshold(Map<String, Object> requestData);
}

