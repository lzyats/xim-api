package com.platform.modules.chat.vo;

import com.platform.modules.chat.rtc.RtcStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatVo06 {

    @NotNull(message = "消息ID不能为空")
    private Long msgId;

    @NotNull(message = "消息状态不能为空")
    private RtcStatus status;

    private String second;

    public String getSecond() {
        if (second == null) {
            return "0";
        }
        return second;
    }
}
