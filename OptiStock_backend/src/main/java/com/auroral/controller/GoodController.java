package com.auroral.controller;

import com.auroral.dto.*;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.ProductCategoryService;
import com.auroral.service.ProductService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class GoodController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private ProductService productService;

    @PostMapping("/getAllProductCategory")
    public ResponseResult getAllProductCategory(@RequestHeader("Authorization") String authHeader, @RequestBody PageRequestDTO pageRequestDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productCategoryService.getAllProductCategory(pageRequestDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    @GetMapping("/getParentCategoty")
    public ResponseResult getParentCategoty(@RequestHeader("Authorization") String authHeader) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productCategoryService.getParentCategoty();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //添加商品分类
    @PostMapping("/addCategory")
    public ResponseResult addCategory(@RequestHeader("Authorization") String authHeader, @RequestBody AddCategoryDTO addCategoryDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productCategoryService.addCategory(addCategoryDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //删除分类
    @DeleteMapping("/deleteCategory")
    public ResponseResult deleteCategory(@RequestHeader("Authorization") String authHeader, @RequestParam Long id) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productCategoryService.deleteCategory(id);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }

    }

    //更新分类
    @PatchMapping("/updateCategoryInfo")
    public ResponseResult updateCategoryInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productCategoryService.updateCategory(updateCategoryDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //获取SKU列表和分类列表
    @GetMapping("/getSkuAndCategoryList")
    public ResponseResult getSkuAndCategoryList(@RequestHeader("Authorization") String authHeader) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productService.getSkuAndCategoryList();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //获取商品列表
    @PostMapping("/getProductList")
    public ResponseResult getProductList(@RequestHeader("Authorization") String authHeader, @RequestBody ProductListDTO requestDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productService.getProductList(requestDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //添加商品
    @PostMapping("/addProduct")
    public ResponseResult addProduct(@RequestHeader("Authorization") String authHeader, @RequestBody AddProductDTO addProductDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productService.addProduct(addProductDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //更新商品
    //格式：http://localhost:9092/product/updateProduct
    @PatchMapping("/updateProduct")
    public ResponseResult updateProduct(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateProductDTO updateProductDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productService.updateProduct(updateProductDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //删除商品
    //格式：http://localhost:9092`/product/deleteProduct?id=1
    @DeleteMapping("/deleteProduct")
    public ResponseResult deleteProduct(@RequestHeader("Authorization") String authHeader, @RequestParam Long id) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return productService.deleteProduct(id);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
}