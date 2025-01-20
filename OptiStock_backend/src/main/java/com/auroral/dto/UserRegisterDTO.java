package com.auroral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "用户名长度为5-16位，不能包含空格")
    private String username;

    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^\\S{2,10}$", message = "昵称长度为2-10位，不能包含空格")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度为5-16位，不能包含空格")
    private String password;
}
