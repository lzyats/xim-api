package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 找回密码
 */
@Data
public class MineVo21 {

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
