package com.platform.modules.wallet.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class TradeVo05 {

    /**
     * 交易ID
     */
    private Long tradeId;

    public TradeVo05(Long tradeId) {
        this.tradeId = tradeId;
    }
}
