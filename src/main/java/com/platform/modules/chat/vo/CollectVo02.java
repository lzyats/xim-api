package com.platform.modules.chat.vo;

import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true) // 链式调用
public class CollectVo02 {

    /**
     * 主键
     */
    private Long collectId;
    /**
     * 消息类型
     */
    private PushMsgTypeEnum msgType;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;

    public String getMsgTypeLabel() {
        if (msgType == null) {
            return null;
        }
        return msgType.getInfo();
    }
}
