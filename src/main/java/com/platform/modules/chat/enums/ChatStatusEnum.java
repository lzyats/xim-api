package com.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 消息状态
 */
@Getter
public enum ChatStatusEnum {

    /**
     * 成功
     */
    NORMAL("0", "成功"),
    /**
     * 对方不是自己朋友
     */
    FRIEND_NONE("1", "你们还不是好友哦，消息发送失败"),
    /**
     * 黑名单
     */
    FRIEND_BLACK("2", "消息已发出，但被对方拒收了"),
    /**
     * 群组不存在
     */
    GROUP_NONE("3", "当前群组不存在，消息发送失败"),
    /**
     * 群组成员不存在
     */
    GROUP_MEMBER_NONE("4", "你不在当前群组中，消息发送失败"),
    /**
     * 全员禁言
     */
    GROUP_SPEAK("5", "全员禁言，消息发送失败"),
    /**
     * 群组封禁
     */
    GROUP_BANNED("6", "群组已被封禁，消息发送失败"),
    /**
     * 禁止多媒体
     */
    GROUP_MEDIA("7", "成员禁止发送资源文件，消息发送失败"),
    /**
     * 禁止红包
     */
    GROUP_PACKET("8", "成员禁止发送红包，消息发送失败"),
    /**
     * 成员禁言
     */
    GROUP_MEMBER_SPEAK("9", "你已被禁言，消息发送失败"),
    /**
     * 服务不存在
     */
    ROBOT("10", "服务不存在，消息发送失败"),
    /**
     * 群组成员不存在
     */
    GROUP_FRIEND("11", "对方不在当前群组中，消息发送失败"),
    /**
     * 群组二维码
     */
    GROUP_SCAN("12", "成员禁止发送二维码，消息发送失败"),
    /**
     * 操作失败，请稍后再试
     */
    ERROR("99", "操作失败，请稍后再试"),
    ;

    @EnumValue
    @JsonValue
    private String code;
    private String info;

    ChatStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
