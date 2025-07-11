package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 聊天消息实体类
 * </p>
 */
@Data
@TableName("chat_msg")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatMsg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息主键
     */
    @TableId
    private Long msgId;
    /**
     * 同步id
     */
    private Long syncId;
    /**
     * 发送人
     */
    private Long userId;
    /**
     * 接收人
     */
    private Long receiveId;
    /**
     * 群id
     */
    private Long groupId;
    /**
     * 消息类型
     */
    private PushMsgTypeEnum msgType;
    /**
     * 消息类型
     */
    private ChatTalkEnum talkType;
    /**
     * 消息内容
     */
    private String content;
    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;

    public ChatMsg(Long msgId) {
        this.msgId = msgId;
    }
}
