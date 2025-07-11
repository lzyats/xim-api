package com.platform.modules.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 根据账号+密码登录
 */
@Data
public class AuthVo02 {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String phone;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
