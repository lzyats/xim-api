package com.platform.modules.wallet.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true) // 链式调用
@Deprecated
public class WalletVo04 {

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String phone;

}
