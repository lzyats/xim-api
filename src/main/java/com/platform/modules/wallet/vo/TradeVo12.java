package com.platform.modules.wallet.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.wallet.domain.WalletPacket;
import com.platform.modules.wallet.domain.WalletTrade;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class TradeVo12 {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户号码
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 最佳/最差
     */
    private YesOrNoEnum best = YesOrNoEnum.REFUND;
    /**
     * 交易时间
     */
    private Date createTime;

    public TradeVo12(WalletPacket walletPacket) {
        this.userId = walletPacket.getUserId();
        this.userNo = walletPacket.getUserNo();
        this.nickname = walletPacket.getNickname();
        this.portrait = walletPacket.getPortrait();
        this.amount = walletPacket.getAmount().abs();
        this.createTime = walletPacket.getCreateTime();
    }

    public TradeVo12(WalletTrade walletTrade) {
        this.userId = walletTrade.getReceiveId();
        this.userNo = walletTrade.getReceiveNo();
        this.nickname = walletTrade.getReceiveName();
        this.portrait = walletTrade.getReceivePortrait();
        this.amount = walletTrade.getAbsolute().abs();
        this.createTime = walletTrade.getCreateTime();
    }

}
