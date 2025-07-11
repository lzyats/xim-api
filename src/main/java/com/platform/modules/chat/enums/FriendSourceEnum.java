package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 好友来源
 */
@Getter
public enum FriendSourceEnum {

    /**
     * 自己
     */
    SELF("0", "自己"),
    /**
     * 扫码
     */
    SCAN("1", "扫码"),
    /**
     * 名片
     */
    CARD("2", "名片"),
    /**
     * ID
     */
    NO("3", "ID查询"),
    /**
     * 账号
     */
    PHONE("4", "账号"),
    /**
     * 群聊
     */
    GROUP("7", "群聊"),
    /**
     * 未知
     */
    OTHER("9", "未知"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    FriendSourceEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
