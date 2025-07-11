package com.platform.modules.auth.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 根据账号+验证码登录
 */
@Data
public class AuthVo03 {

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

}
