package com.platform.modules.pay.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class RefundVo {

    /**
     * 交易订单
     */
    private String tradeNo;

    /**
     * 退款金额
     */
    private BigDecimal amount;

}
