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
     * 邮箱
     */
    @NotBlank(message = "密码不能为空")
    private String pass;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 验证码
     */
    @NotBlank(message = "系统安全码不能为空")
    private String safestr;


    /**
     *  邀请码
     */
    private String incode;

}
