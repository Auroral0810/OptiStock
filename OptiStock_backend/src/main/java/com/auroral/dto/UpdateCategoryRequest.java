package com.auroral.dto;

import lombok.Data;

@Data
public class UpdateCategoryRequest {

    private Long id;
    //分类名称
    private String name;
    //父分类ID，顶级分类为NULL
    private Long parentId;
}
