// com/platform/modules/chat/domain/mongo/ChatMsgMongo.java
package com.platform.modules.chat.domain.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import java.util.Date;

/**
 * MongoDB 聊天消息备份实体（对应集合：chat_msg_backup）
 */
@Data
@Document(collection = "chat_msg")
public class ChatMsgMongo {
    @Id
    private String id; // MongoDB 自动生成的唯一标识
    private Long msgId; // 原消息ID（关联MySQL的chat_msg表）
    private Long syncId; // 同步ID
    private Long userId; // 发送人ID
    private Long receiveId; // 接收人ID
    private Long groupId; // 群组ID
    private PushMsgTypeEnum msgType; // 消息类型
    private ChatTalkEnum talkType; // 聊天类型（单聊/群聊）
    private String content; // 消息内容（JSON字符串）
    private Date createTime; // 创建时间

}