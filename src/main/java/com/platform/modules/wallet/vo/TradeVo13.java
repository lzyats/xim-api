package com.platform.modules.wallet.vo;

import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class TradeVo13 {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long tradeId;
    /**
     * 交易类型
     */
    private TradeTypeEnum tradeType;
    /**
     * 交易金额
     */
    private BigDecimal tradeAmount;
    /**
     * 交易数量
     */
    private Integer tradeCount;
    /**
     * 交易时间
     */
    private Date createTime;

    public TradeVo13(WalletTrade trade) {
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.tradeAmount = trade.getAbsolute();
        this.tradeCount = trade.getTradeCount();
        this.createTime = trade.getCreateTime();
    }

}
