package com.platform.modules.wallet.vo;

import com.platform.common.enums.YesOrNoEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true) // 链式调用
public class WalletVo02 {

    /**
     * 提现加成
     */
    private BigDecimal cost;
    /**
     * 提现最大金额
     */
    private BigDecimal max;
    /**
     * 提现最小金额
     */
    private BigDecimal min;
    /**
     * 提现说明
     */
    private String remark;
    /**
     * 提现利率
     */
    private BigDecimal rate;
    /**
     * 提现次数
     */
    private Integer count;
    /**
     * 提现实名
     */
    //private YesOrNoEnum auth;
    private String auth;

    /**
     * 提现汇率
     */
    private BigDecimal rates;

}
