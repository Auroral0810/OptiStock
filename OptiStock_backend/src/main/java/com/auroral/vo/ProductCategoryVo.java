package com.auroral.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProductCategoryVo {
    private Long id;
    private String name;
    private String parentName;
    private Integer productCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
