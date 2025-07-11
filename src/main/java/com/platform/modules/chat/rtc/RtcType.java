package com.platform.modules.chat.rtc;

public enum RtcType {
    JOIN(1),
    AUDIO(2),
    VIDEO(3),
    DATA(4),
    ;

    public short intValue;

    RtcType(int value) {
        intValue = (short) value;
    }
}
