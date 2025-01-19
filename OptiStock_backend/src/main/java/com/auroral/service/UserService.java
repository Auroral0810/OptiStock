package com.auroral.service;

import com.auroral.dto.UserChangePwdRequest;
import com.auroral.dto.UserLoginRequest;
import com.auroral.dto.UserUpdateRequest;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;


/**
 * 用户表(Users)表服务接口
 *
 * @author makejava
 * @since 2025-01-19 13:58:19
 */
public interface UserService extends IService<User> {

    ResponseResult register(String username, String nickname, String password);

    ResponseResult login(UserLoginRequest request);

    ResponseResult updateInfo(UserUpdateRequest request);

    ResponseResult changePassword( UserChangePwdRequest request);
}

