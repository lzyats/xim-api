package com.platform.modules.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 短信类型枚举
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 注册
     */
    REGISTER("0", "chat:code:register:", 5),
    /**
     * 登录
     */
    LOGIN("1", "chat:code:login:", 5),
    /**
     * 忘记密码
     */
    FORGET("2", "chat:code:forget:", 5),
    /**
     * 钱包
     */
    WALLET("3", "chat:code:wallet:", 5),
    /**
     * 邮箱
     */
    EMAIL("4", "chat:code:email:", 5),
    /**
     * 找回密码
     */
    RETRIEVE("5", "chat:code:retrieve:", 5),
    /**
     * 绑定
     */
    BINDING("9", "chat:code:binding:", 5),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String prefix;
    private Integer timeout;

    MessageTypeEnum(String code, String prefix, Integer timeout) {
        this.code = code;
        this.prefix = prefix;
        this.timeout = timeout;
    }

}
