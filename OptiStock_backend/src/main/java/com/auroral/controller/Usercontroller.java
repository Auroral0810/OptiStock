package com.auroral.controller;

import com.auroral.dto.UserChangePwdDTO;
import com.auroral.dto.UserLoginDTO;
import com.auroral.dto.UserRegisterDTO;
import com.auroral.dto.UserUpdateDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.UserService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
public class Usercontroller {
    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/register")
    public ResponseResult register(@RequestBody @Valid UserRegisterDTO request) {
        return userService.register(request.getUsername(), request.getNickname(), request.getPassword());
    }

    //登陆
    @PostMapping("/login")
    public ResponseResult login(@RequestBody @Valid UserLoginDTO request) {
        return userService.login(request);
    }

    //更新用户信息
    @PostMapping("/updateInfo")
    public ResponseResult update(@RequestHeader("Authorization") String authHeader,@RequestBody @Valid UserUpdateDTO request) {
        // 解析 Token，获取 UserId
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
            //合法就给service层处理
            request.setId(Long.parseLong(userId));
            return userService.updateInfo(request);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //修改密码
    @PostMapping("/changePassword")
    public ResponseResult changePassword(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid UserChangePwdDTO request) {
// 解析 Token，获取 UserId
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
            //合法就给service层处理
            request.setId(Long.parseLong(userId));
            return userService.changePassword(request);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

}

