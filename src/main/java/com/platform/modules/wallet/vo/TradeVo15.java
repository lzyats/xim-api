package com.platform.modules.wallet.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.web.domain.JsonDecimalDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class TradeVo15 {

    /**
     * 应用id
     */
    @NotBlank(message = "应用id不能为空")
    private String appId;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String goodsName;
    /**
     * 订单编号
     */
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;
    /**
     * 支付金额
     */
    @Digits(integer = 8, fraction = 2, message = "支付金额格式不正确")
    @DecimalMin(value = "0.01", message = "支付金额不能小于0.01元")
    @DecimalMax(value = "999999.99", message = "支付金额不能大于999999.99元")
    @NotNull(message = "支付金额不能为空")
    @JsonDeserialize(using = JsonDecimalDeserializer.class)
    private BigDecimal goodsPrice;
    /**
     * 支付密码
     */
    @NotBlank(message = "支付密码不能为空")
    private String password;

}
