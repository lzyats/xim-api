package com.platform.modules.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class PayVo {

    /**
     * 商品名称
     */
    private String body;
    /**
     * 交易号码
     */
    private String tradeNo;
    /**
     * 支付金额
     */
    private BigDecimal amount;

}
