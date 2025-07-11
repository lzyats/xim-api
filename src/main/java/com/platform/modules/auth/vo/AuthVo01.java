package com.platform.modules.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 找回密码
 */
@Data
public class AuthVo01 {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String phone;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
