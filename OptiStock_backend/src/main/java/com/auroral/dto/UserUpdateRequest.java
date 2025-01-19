package com.auroral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^\\S{2,10}$", message = "昵称长度为2-10位，不能包含空格")
    private String nickname;

    @Pattern(regexp = "^[\\w.-]+@[a-zA-Z\\d.-]+\\.[a-zA-Z]{2,}$", message = "邮箱格式不正确")
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    private String avatar;
    private Long id;
}
