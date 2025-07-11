package com.platform.modules.chat.vo;

import cn.hutool.core.date.DateUtil;
import com.platform.modules.chat.enums.ChatStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVo00 {

    /**
     * 消息id
     */
    private Long msgId;
    /**
     * 同步id
     */
    private Long syncId;
    /**
     * 发送状态
     */
    private ChatStatusEnum status;
    /**
     * 通话token
     */
    private String token;
    /**
     * 时间
     */
    private Long createTime;

    public String getStatusLabel() {
        if (status == null) {
            return null;
        }
        return status.getInfo();
    }

    public ChatVo00(Long msgId, Long syncId) {
        this.msgId = msgId;
        this.syncId = syncId;
        this.status = ChatStatusEnum.NORMAL;
        this.createTime = DateUtil.current();
    }

    public ChatVo00(Long msgId, Long syncId, ChatStatusEnum status) {
        this.msgId = msgId;
        this.syncId = syncId;
        this.status = status;
        this.createTime = DateUtil.current();
    }

}
