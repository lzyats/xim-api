package com.platform.modules.wallet.vo;

import cn.hutool.core.util.NumberUtil;
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
public class TradeVo11 {

    private static final long serialVersionUID = 1L;

    /**
     * 交易ID
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
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 交易数量
     */
    private String total;
    /**
     * 交易备注
     */
    private String remark;
    /**
     * 发送人
     */
    private String nickname;
    /**
     * 发送人
     */
    private String portrait;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;

    public TradeVo11(WalletTrade trade) {
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.tradeStatus = trade.getTradeStatus();
        this.amount = trade.getTradeAmount().abs();
        this.remark = trade.getTradeRemark();
        this.portrait = trade.getPortrait();
        this.total = NumberUtil.toStr(trade.getTradeCount());
        this.createTime = trade.getCreateTime();
        this.updateTime = trade.getUpdateTime();
        this.nickname = trade.getNickname();
    }

}
