package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 群组来源
 */
@Getter
public enum GroupSourceEnum {

    /**
     * 搜索
     */
    GROUP_NO("1", "搜索"),
    /**
     * 邀请
     */
    INVITE("2", "邀请"),
    /**
     * 扫码
     */
    SCAN("3", "扫码"),
    /**
     * 群名
     */
    GROUP_NAME("4", "群名"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    GroupSourceEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
