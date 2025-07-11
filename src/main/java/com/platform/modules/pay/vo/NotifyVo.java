package com.platform.modules.pay.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class NotifyVo {

    /**
     * 交易号码
     */
    private String tradeNo;
    /**
     * 交易号码
     */
    private String orderNo;
    /**
     * 支付金额
     */
    private BigDecimal amount;
    /**
     * 支付结果
     */
    private boolean result = false;
    /**
     * 支付结果
     */
    private String resultLabel;

    public NotifyVo(String resultLabel) {
        this.resultLabel = resultLabel;
    }
}
