package com.platform.modules.chat.rtc;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RtcStatus {
    AWAIT("await"),
    CANCEL("cancel"),
    REJECT("reject"),
    CONNECT("connect"),
    FINISH("finish"),
    ;

    @EnumValue
    @JsonValue
    public String value;

    RtcStatus(String value) {
        this.value = value;
    }
}
