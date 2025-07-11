package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MineVo04 {

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

}
