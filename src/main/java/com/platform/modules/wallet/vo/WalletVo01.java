package com.platform.modules.wallet.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import com.platform.modules.wallet.enums.TradePayEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo01 {

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private TradePayEnum payType;

    /**
     * 充值金额
     */
    @Digits(integer = 8, fraction = 2, message = "充值金额格式不正确")
    @DecimalMin(value = "0.01", message = "充值金额不能小于0.01元")
    @DecimalMax(value = "999999.99", message = "充值金额不能大于999999.99元")
    @NotNull(message = "充值金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal amount;

}
