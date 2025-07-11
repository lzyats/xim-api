package com.platform.modules.chat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true) // 链式调用
public class MineVo19 {

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    private String email;

}
