package com.auroral.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserChangePwdDTO {
    private Long id;
    @NotBlank(message = "原始密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度为5-16位，不能包含空格")
    private String oldPassword;
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = "^\\S{5,16}$", message = "密码长度为5-16位，不能包含空格")
    private String newPassword;
}
