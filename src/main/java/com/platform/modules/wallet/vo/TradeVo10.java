package com.platform.modules.wallet.vo;

import com.platform.common.enums.ApproveEnum;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.domain.WalletRecharge;
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
public class TradeVo10 {

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
    private String tradeAmount;
    /**
     * 交易备注
     */
    private String remark;
    /**
     * 交易状态
     */
    private ApproveEnum tradeStatus;
    /**
     * 交易时间
     */
    private Date createTime;
    /**
     * 交易时间
     */
    private Date updateTime;

    public TradeVo10(WalletTrade trade) {
        String label = "-";
        if (BigDecimal.ZERO.compareTo(trade.getTradeAmount()) == -1) {
            label = "+";
        }
        label += trade.getTradeAmount().abs() + " 元";
        this.tradeId = trade.getTradeId();
        this.tradeType = trade.getTradeType();
        this.tradeAmount = label;
        this.remark = trade.getTradeRemark();
        this.tradeStatus = trade.getTradeStatus();
        this.createTime = trade.getCreateTime();
        this.updateTime = trade.getUpdateTime();
    }

    public TradeVo10 setRecharge(WalletTrade trade, WalletRecharge recharge) {
        TradeRechargeVo tradeVo = new TradeRechargeVo(trade);
        tradeVo.payType = recharge.getPayType().getInfo();
        tradeVo.payType = recharge.getPayType().getInfo();
        tradeVo.tradeNo = recharge.getTradeNo();
        tradeVo.orderNo = recharge.getOrderNo();
        return tradeVo;
    }

    // 提现
    public TradeVo10 setCash(WalletTrade trade, WalletCash cash) {
        TradeCashVo tradeVo = new TradeCashVo(trade);
        String tradeLabel;
        String remark;
        switch (trade.getTradeStatus()) {
            case APPLY:
                tradeLabel = "审核中";
                remark = "后台审核中，请耐心等待";
                break;
            case PASS:
                tradeLabel = "已完成";
                remark = "已完成";
                break;
            case REJECT:
                tradeLabel = "已驳回";
                remark = cash.getReason();
                break;
            default:
                tradeLabel = "-";
                remark = "-";
                break;
        }
        tradeVo.name = cash.getName();
        tradeVo.wallet = cash.getWallet();
        tradeVo.tradeLabel = tradeLabel;
        tradeVo.setRemark(remark);
        return tradeVo;
    }

    // 红包/转账/扫码转账
    public TradeVo10 setFriend(WalletTrade trade) {
        TradeFriendVo tradeVo = new TradeFriendVo(trade);
        String nickname = trade.getNickname();
        String userNo = trade.getUserNo();
        String receiveName = trade.getReceiveName();
        String receiveNo = trade.getReceiveNo();
        if (BigDecimal.ZERO.compareTo(trade.getTradeAmount()) == -1) {
            nickname = trade.getReceiveName();
            userNo = trade.getReceiveNo();
            receiveName = trade.getNickname();
            receiveNo = trade.getUserNo();
        }
        String tradeLabel;
        switch (trade.getTradeStatus()) {
            case APPLY:
                tradeLabel = "未领取";
                break;
            case PASS:
                tradeLabel = "已领取";
                break;
            case REJECT:
                tradeLabel = "已退回";
                break;
            default:
                tradeLabel = "-";
                break;
        }
        tradeVo.nickname = nickname;
        tradeVo.userNo = userNo;
        tradeVo.receiveName = receiveName;
        tradeVo.receiveNo = receiveNo;
        tradeVo.tradeLabel = tradeLabel;
        return tradeVo;
    }

    // 普通红包/手气红包/专属红包
    public TradeVo10 setGroup(WalletTrade trade) {
        TradeGroupVo tradeVo = new TradeGroupVo(trade);
        String nickname = trade.getNickname();
        String userNo = trade.getUserNo();
        String receiveName = trade.getReceiveName();
        String receiveNo = trade.getReceiveNo();
        if (BigDecimal.ZERO.compareTo(trade.getTradeAmount()) == -1) {
            userNo = trade.getReceiveNo();
            nickname = trade.getReceiveName();
            receiveName = trade.getNickname();
            receiveNo = trade.getUserNo();
        }
        String tradeLabel;
        switch (trade.getTradeStatus()) {
            case APPLY:
                tradeLabel = "未领取";
                break;
            case PASS:
                tradeLabel = "已领取";
                break;
            case REJECT:
                tradeLabel = "有退款";
                break;
            default:
                tradeLabel = "-";
                break;
        }
        tradeVo.nickname = nickname;
        tradeVo.userNo = userNo;
        tradeVo.receiveName = receiveName;
        tradeVo.receiveNo = receiveNo;
        tradeVo.groupName = trade.getGroupName();
        tradeVo.groupNo = trade.getGroupNo();
        tradeVo.tradeLabel = tradeLabel;
        tradeVo.count = trade.getTradeCount();
        return tradeVo;
    }

    // 退款
    public TradeVo10 setRefund(WalletTrade trade) {
        TradeRefundVo tradeVo = new TradeRefundVo(trade);
        tradeVo.source = trade.getSourceType().getInfo();
        return tradeVo;
    }

    @Data
    @NoArgsConstructor
    class TradeRechargeVo extends TradeVo10 {
        private String payType;
        private String tradeNo;
        private String orderNo;

        public TradeRechargeVo(WalletTrade trade) {
            super(trade);
        }
    }

    @Data
    @NoArgsConstructor
    class TradeCashVo extends TradeVo10 {
        private String name;
        private String wallet;
        private String tradeLabel;

        public TradeCashVo(WalletTrade trade) {
            super(trade);
        }
    }

    @Data
    @NoArgsConstructor
    class TradeFriendVo extends TradeVo10 {
        private String nickname;
        private String userNo;
        private String receiveName;
        private String receiveNo;
        private String tradeLabel;

        public TradeFriendVo(WalletTrade trade) {
            super(trade);
        }
    }

    @Data
    @NoArgsConstructor
    class TradeGroupVo extends TradeVo10 {
        private String nickname;
        private String userNo;
        private String receiveName;
        private String receiveNo;
        private String groupName;
        private String groupNo;
        private String tradeLabel;
        private Integer count;

        public TradeGroupVo(WalletTrade trade) {
            super(trade);
        }
    }

    @Data
    @NoArgsConstructor
    class TradeRefundVo extends TradeVo10 {
        private String source;

        public TradeRefundVo(WalletTrade trade) {
            super(trade);
        }
    }

}
