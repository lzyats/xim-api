package com.platform.modules.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 根据账号+验证码注册
 */
@Data
public class AuthVo06 {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

}
