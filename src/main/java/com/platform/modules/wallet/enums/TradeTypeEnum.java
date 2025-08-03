package com.platform.modules.wallet.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 交易类型
 */
@Getter
public enum TradeTypeEnum {

    /**
     * 充值
     */
    RECHARGE("1001", "充值"),
    /**
     * 提现
     */
    CASH("1002", "提现"),
    /**
     * 个人转账
     */
    TRANSFER("1003", "个人转账"),
    /**
     * 红包-个人红包
     */
    PACKET("1004", "个人红包"),
    /**
     * 红包-专属红包
     */
    PACKET_ASSIGN("1005", "专属红包"),
    /**
     * 红包-手气红包
     */
    PACKET_LUCK("1006", "手气红包"),
    /**
     * 红包-普通红包
     */
    PACKET_NORMAL("1007", "普通红包"),
    /**
     * 扫码转账
     */
    SCAN("1008", "扫码转账"),
    /**
     * 退款
     */
    REFUND("1009", "退款"),
    /**
     * 消费
     */
    SHOPPING("1010", "消费"),
    /**
     * 群内转账
     */
    GROUP_TRANSFER("1011", "群内转账"),
    /**
     * 群内转账
     */
    SIGN("1019", "每日签到"),
    /**
     * 群内转账
     */
    INVO("1020", "邀请奖励"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    TradeTypeEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
