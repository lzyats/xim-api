package com.platform.modules.common.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class CommonVo02 {

    @NotBlank(message = "声音地址不能为空")
    @Size(max = 2000, message = "声音地址不能大于2000")
    private String voicePath;

}
