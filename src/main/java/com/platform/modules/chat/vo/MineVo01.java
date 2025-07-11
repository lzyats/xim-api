package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MineVo01 {

    /**
     * 密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPwd;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPwd;

}
