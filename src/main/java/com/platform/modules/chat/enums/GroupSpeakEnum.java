package com.platform.modules.chat.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 群组禁言
 */
@Getter
public enum GroupSpeakEnum {

    /**
     * 取消
     */
    CLEAR("0", "取消禁言", 0),
    /**
     * 1小时
     */
    HOUR("1", "1小时", 1),
    /**
     * 1天
     */
    DAY("2", "1天", 24),
    /**
     * 1周
     */
    WEEK("3", "1周", 7 * 24),
    /**
     * 1月
     */
    MONTH("4", "1月", 30 * 24),
    /**
     * 1年
     */
    YEARS("5", "1年", 365 * 24),
    /**
     * 永久
     */
    FOREVER("6", "永久", 9999 * 24),
    ;

    @JsonValue
    private String code;
    private String name;
    private Integer value;

    GroupSpeakEnum(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

}
