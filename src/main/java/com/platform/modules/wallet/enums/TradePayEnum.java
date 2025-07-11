package com.platform.modules.wallet.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 支付类型
 */
@Getter
public enum TradePayEnum {

    /**
     * 系统
     */
    SYS_PAY("0", "系统"),
    /**
     * 支付宝支付
     */
    ALI_PAY("1", "支付宝"),
    /**
     * 微信支付
     */
    WX_PAY("2", "微信"),
    /**
     * 钱包支付
     */
    WALLET_PAY("3", "钱包"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    TradePayEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
