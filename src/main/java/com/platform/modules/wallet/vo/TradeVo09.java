package com.platform.modules.wallet.vo;

import com.platform.common.enums.ApproveEnum;
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
public class TradeVo09 {

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
     * 交易状态
     */
    private ApproveEnum tradeStatus;
    /**
     * 交易状态
     */
    private String tradeLabel;
    /**
     * 交易金额
     */
    private String tradeAmount;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;

    public TradeVo09(WalletTrade trade) {
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.tradeStatus = trade.getTradeStatus();
        this.tradeLabel = format(trade.getTradeStatus());
        this.tradeAmount = format(trade.getTradeAmount());
        this.createTime = trade.getCreateTime();
        this.updateTime = trade.getUpdateTime();
    }

    private String format(BigDecimal tradeAmount) {
        String label = "-";
        if (BigDecimal.ZERO.compareTo(tradeAmount) == -1) {
            label = "+";
        }
        return label + tradeAmount.abs();
    }


    private String format(ApproveEnum tradeStatus) {
        switch (tradeStatus) {
            case APPLY:
                return "系统审核";
            case PASS:
                return "审核成功";
            case REJECT:
                return "审核失败";
            default:
                return "-";
        }
    }

}
