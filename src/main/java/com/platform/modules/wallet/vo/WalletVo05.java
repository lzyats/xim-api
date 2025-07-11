package com.platform.modules.wallet.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo05 {

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    @Size(max = 32, message = "支付密码长度不能大于32")
    private String password;

}
