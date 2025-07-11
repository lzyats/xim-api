package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class GroupVo09 {

    @NotNull(message = "群组id不能为空")
    private Long groupId;

    @NotBlank(message = "头像不能为空")
    @Size(max = 2000, message = "头像长度不能大于2000")
    private String portrait;

}
