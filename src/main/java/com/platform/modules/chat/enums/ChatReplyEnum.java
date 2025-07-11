package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 自动回复类型
 */
@Getter
public enum ChatReplyEnum {

    /**
     * 关注回复
     */
    SUBSCRIBE("subscribe", "关注回复"),
    /**
     * 关键词回复
     */
    REPLY("reply", "关键词回复"),
    /**
     * 未匹配回复
     */
    ERROR("error", "未匹配回复"),
    /**
     * 事件回复
     */
    EVEN("even", "事件回复"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    ChatReplyEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
