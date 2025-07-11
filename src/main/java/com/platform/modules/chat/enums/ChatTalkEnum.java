package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 消息聊天枚举
 */
@Getter
public enum ChatTalkEnum {

    /**
     * 单聊
     */
    FRIEND("1", "单聊", "friend"),
    /**
     * 群聊
     */
    GROUP("2", "群聊", "group"),
    /**
     * 服务号
     */
    ROBOT("3", "服务号", "robot"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;
    private String type;

    ChatTalkEnum(String code, String info, String type) {
        this.code = code;
        this.info = info;
        this.type = type;
    }

}
