package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 群组成员类型
 */
@Getter
public enum GroupMemberEnum {

    /**
     * 群主
     */
    MASTER("master", "群主"),
    /**
     * 管理员
     */
    MANAGER("manager", "管理员"),
    /**
     * 成员
     */
    NORMAL("normal", "成员"),
    /**
     * 机器人
     */
    ROBOT("robot", "机器人"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    GroupMemberEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
