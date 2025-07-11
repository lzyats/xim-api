package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class GroupVo28 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotNull(message = "套餐类型不能为空")
    private Integer groupLevel;

    @NotBlank(message = "支付密码不能为空")
    private String password;

}
