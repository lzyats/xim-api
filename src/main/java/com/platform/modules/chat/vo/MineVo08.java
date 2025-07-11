package com.platform.modules.chat.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class MineVo08 {

    @NotBlank(message = "省份不能为空")
    @Size(max = 20, message = "省份长度限1-20位")
    private String province;

    @NotBlank(message = "城市不能为空")
    @Size(max = 20, message = "城市长度限1-20位")
    private String city;

}
