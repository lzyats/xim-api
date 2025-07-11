package com.platform.modules.wallet.vo;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo06 {

    /**
     * 提现金额
     */
    @Digits(integer = 8, fraction = 2, message = "提现金额格式不正确")
    @DecimalMin(value = "0.01", message = "提现金额不能小于0.01")
    @DecimalMax(value = "999999.99", message = "提现金额不能大于999999.99元")
    @NotNull(message = "提现金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal amount;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能大于20")
    private String name;

    /**
     * 账户
     */
    @NotBlank(message = "账户不能为空")
    @Size(max = 200, message = "账户长度不能大于200")
    private String wallet;

    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    private String password;

}