package com.auroral.vo;

import lombok.Data;

import java.util.List;

@Data
public class SkuAndCategoryVo {
    //sku值
    private List<String> skuList;
    //分类名
    private List<String> categoryNameList;
}
