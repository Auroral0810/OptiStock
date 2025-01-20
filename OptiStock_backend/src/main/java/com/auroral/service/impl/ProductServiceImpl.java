package com.auroral.service.impl;

import com.auroral.dto.AddProductDTO;
import com.auroral.dto.ProductFilterDTO;
import com.auroral.dto.ProductListDTO;
import com.auroral.dto.UpdateProductDTO;
import com.auroral.entity.Product;
import com.auroral.entity.ProductCategory;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.ProductMapper;
import com.auroral.service.ProductService;
import com.auroral.utils.BeanCopyUtils;
import com.auroral.vo.PageVo;
import com.auroral.vo.ProductVo;
import com.auroral.vo.SkuAndCategoryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品信息表(Product)表服务实现类
 *
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
@Service("productService")
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    private ProductCategoryServiceImpl productCategoryService;
    //返回商品SKU和分类列表
    @Override
    public ResponseResult getSkuAndCategoryList() {
        /*
         * 1. 查询商品列表找到不同的Sku值
         */
        LambdaQueryWrapper<Product> skuQueryWrapper = new LambdaQueryWrapper<>();
        skuQueryWrapper.select(Product::getSku).groupBy(Product::getSku);
        List<String> skuList = list(skuQueryWrapper)
                .stream()
                .map(Product::getSku)
                .collect(Collectors.toList());

        /*
         * 2. 查询分类列表获得不同分类的值
         */
        LambdaQueryWrapper<ProductCategory> categoryQueryWrapper = new LambdaQueryWrapper<>();
        categoryQueryWrapper.select(ProductCategory::getName);
        List<String> categoryList = productCategoryService.list(categoryQueryWrapper)
                .stream()
                .map(ProductCategory::getName)
                .collect(Collectors.toList());

        /*
         * 3. 封装VO
         */
        SkuAndCategoryVo skuAndCategoryVo = new SkuAndCategoryVo();
        skuAndCategoryVo.setSkuList(skuList);
        skuAndCategoryVo.setCategoryNameList(categoryList);

        /*
         * 4. 返回结果
         */
        return ResponseResult.okResult(skuAndCategoryVo);
    }
    //返回商品信息
    @Override
    public ResponseResult getProductList(ProductListDTO requestDTO) {
        Integer currentPage = requestDTO.getPageNum() != null ? requestDTO.getPageNum() : 1;
        Integer pageSize = requestDTO.getPageSize() != null ? requestDTO.getPageSize() : 10;

        ProductFilterDTO filter = requestDTO.getFilterForm();
        Page<Product> page = new Page<>(currentPage, pageSize);

        // 构建查询条件
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();

        if (filter.getName() != null && !filter.getName().trim().isEmpty()) {
            queryWrapper.like(Product::getName, filter.getName().trim());
        }
        //todo 后续直接在SkuAndCategoryVo中返回分类id，前端直接使用，从而简化查询ID
        if (filter.getCategoryName() != null && !filter.getCategoryName().trim().isEmpty()) {
           // 先查询分类ID
            ProductCategory category = productCategoryService.getOne(new LambdaQueryWrapper<ProductCategory>()
                   .eq(ProductCategory::getName, filter.getCategoryName().trim()));
            if (category != null) {
                queryWrapper.eq(Product::getCategoryId, category.getId());
            }
        }
        if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty()) {
            queryWrapper.eq(Product::getStatus, filter.getStatus().trim());
        }
        if (filter.getSku() != null && !filter.getSku().trim().isEmpty()) {
            queryWrapper.like(Product::getSku, filter.getSku().trim());
        }

        queryWrapper.orderByAsc(Product::getId);

        // 执行分页查询
        page(page, queryWrapper);

        // 获取数据
        List<Product> productList = page.getRecords();

        // 封装VO
        List<ProductVo> productVoList = BeanCopyUtils.copyBeanList(productList, ProductVo.class);
        //设置CategoryName
        productVoList.stream()
                .map(product -> {
                    product.setCategoryName(productCategoryService.getById(product.getCategoryId()).getName());
                    return product;
                })
                .collect(Collectors.toList());
        // 返回结果
        PageVo pageVo = new PageVo(productVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //添加商品信息
    @Override
    public ResponseResult addProduct(AddProductDTO addProductDTO) {
        /*
         * 1.查询是否有相同的商品名称
         * 2.如果有相同的商品名称，返回错误信息
         * 3.如果没有相同的商品名称，就添加商品信息
         * 4.返回成功信息
         * */
        Product product = getOne(new LambdaQueryWrapper<Product>()
               .eq(Product::getName, addProductDTO.getName()));
        if (product != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR);
        }
        Product newProduct = BeanCopyUtils.copyBean(addProductDTO, Product.class);
        //查找分类ID
        ProductCategory category = productCategoryService.getOne(new LambdaQueryWrapper<ProductCategory>()
               .eq(ProductCategory::getName, addProductDTO.getCategoryName().trim()));
        if (category != null) {
            newProduct.setCategoryId(category.getId());
            }
        save(newProduct);
        return ResponseResult.okResult();
    }
    //删除商品信息
    @Override
    public ResponseResult deleteProduct(Long id) {
        /*1，判断商品的库存是否为0
        * 2.如果库存为0，则删除商品信息
        * 3.如果库存不为0，则返回错误信息
        * */
        Product product = getById(id);
        if (product == null) {   //商品不存在
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR);
             }
        if (product.getStockQuantity() == 0) {
            removeById(id);
            return ResponseResult.okResult();
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.REQUEST_NOT_MATCH);
        }
    }
    //修改商品信息

    @Override
    public ResponseResult updateProduct(UpdateProductDTO updateProductDTO) {
        /*1.

        * */
        Product product = BeanCopyUtils.copyBean(updateProductDTO, Product.class);
        updateById(product);
        return ResponseResult.okResult();
    }
}


