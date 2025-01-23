package com.auroral.service.impl;

import com.auroral.dto.AddCategoryDTO;
import com.auroral.dto.PageRequestDTO;
import com.auroral.dto.UpdateCategoryDTO;
import com.auroral.entity.ProductCategory;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.ProductCategoryMapper;
import com.auroral.mapper.ProductMapper;
import com.auroral.service.ProductCategoryService;
import com.auroral.utils.BeanCopyUtils;
import com.auroral.vo.PageVo;
import com.auroral.vo.ParentCategotyVo;
import com.auroral.vo.ProductCategoryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品分类表，存储商品的层级分类(ProductCategory)表服务实现类
 *
 * @author makejava
 * @since 2025-01-20 12:00:38
 */
@Service("productCategoryService")
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    @Transactional
    public ResponseResult getAllProductCategory(PageRequestDTO pageRequestDTO) {
        // 获取参数
        Integer pageNum = pageRequestDTO.getPageNum();
        Integer pageSize = pageRequestDTO.getPageSize();
        String categoryName = pageRequestDTO.getCategoryName();
        Long parentId = pageRequestDTO.getParentId();
        // 构造分页条件，第几页，每页多少条数据
        Page<ProductCategory> page = new Page<>(pageNum, pageSize);

        // 查询条件构造器
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(ProductCategory::getId);//根据id升序排列
// 只有在 categoryName 不为空时加入条件
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            queryWrapper.like(ProductCategory::getName, categoryName.trim());
        }

// 只有在 parentId 不为空时加入条件
        if (parentId != null) {
            queryWrapper.eq(ProductCategory::getParentId, parentId);
        }
        // 执行分页查询
        page(page, queryWrapper);
        System.out.println("总条数：" + page.getTotal());
        // 获取查询的分类记录
        List<ProductCategory> categoryList = page.getRecords();

        // 查询父分类名称和商品数量
        List<ProductCategoryVo> categoryVOList = categoryList.stream()
                .map(category -> {
                    ProductCategoryVo vo = BeanCopyUtils.copyBean(category, ProductCategoryVo.class);
                    // 获取父分类名称
                    if (category.getParentId() != null && category.getParentId() > 0) {
                        ProductCategory parentCategory = getById(category.getParentId());
                        vo.setParentName(parentCategory != null ? parentCategory.getName() : "无");
                    } else {
                        vo.setParentName("无");
                    }

                    // 查询商品数量
                    Integer productCount = productMapper.countProductsByCategoryId(category.getId());
                    vo.setProductCount(productCount != null ? productCount : 0);

                    return vo;
                })
                .collect(Collectors.toList());

        // 封装查询结果
        PageVo pageVo = new PageVo(categoryVOList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //获取父类名称

    @Override
    @Transactional
    public ResponseResult getParentCategoty() {
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(ProductCategory::getParentId);
        List<ProductCategory> categoryList = list(queryWrapper);
        List<ParentCategotyVo> parentCategoryVoList = BeanCopyUtils.copyBeanList(categoryList, ParentCategotyVo.class);
        return ResponseResult.okResult(parentCategoryVoList);
    }


    //新增商品分类

    @Override
    @Transactional
    public ResponseResult addCategory(AddCategoryDTO addCategoryDTO) {
        //判断分类是否存在
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCategory::getName, addCategoryDTO.getName());
//        queryWrapper.eq(ProductCategory::getParentId, addCategoryRequest.getParentId());
        ProductCategory category = getOne(queryWrapper);
        if (category != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DUPLICATE_RECORD);
        }
        //转换成实体类
        ProductCategory productCategory = BeanCopyUtils.copyBean(addCategoryDTO, ProductCategory.class);
        //保存到数据库
        save(productCategory);
        return ResponseResult.okResult();
    }
    //删除商品分类

    @Override
    @Transactional
    public ResponseResult deleteCategory(Long id) {
        //判断分类是否存在
        ProductCategory category = getById(id);
        if (category == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR);
        }
        //判断是否有子分类
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductCategory::getParentId, id);
        category = getOne(queryWrapper);
        if (category != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR);
        }
        //删除分类
        removeById(id);
        //返回成功
        return ResponseResult.okResult();
    }

    //更新商品分类
    @Override
    @Transactional
    public ResponseResult updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        //判断分类是否存在
        LambdaQueryWrapper<ProductCategory> queryWrapper = new LambdaQueryWrapper<>();
        //判断数据库中是否存在id和要修改的记录不相同但是名称相同的记录
        queryWrapper.ne(ProductCategory::getId, updateCategoryDTO.getId());
        queryWrapper.eq(ProductCategory::getName, updateCategoryDTO.getName());
//        queryWrapper.eq(ProductCategory::getParentId, addCategoryRequest.getParentId());
        ProductCategory category = getOne(queryWrapper);
        if (category != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DUPLICATE_RECORD);
        }
        //调用重写的update方法更新
        productCategoryMapper.updateCategoryNameAndParentId(updateCategoryDTO.getId(), updateCategoryDTO.
                getName(), updateCategoryDTO.getParentId());
        return ResponseResult.okResult();
    }


}


