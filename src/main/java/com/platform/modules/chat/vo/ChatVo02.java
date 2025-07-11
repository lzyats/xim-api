package com.platform.modules.chat.vo;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatVo02 {

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 同步ID
     */
    private Long syncId;

    @NotNull(message = "群组不能为空")
    private Long groupId;

    @NotNull(message = "消息类型不能为空")
    private PushMsgTypeEnum msgType;

    @NotNull(message = "消息内容不能为空")
    private JSONObject content;

    public Long getMsgId() {
        if (msgId == null) {
            msgId = IdWorker.getId();
        }
        return msgId;
    }

    public Long getSyncId() {
        if (syncId == null) {
            syncId = IdWorker.getId();
        }
        return syncId;
    }

}
