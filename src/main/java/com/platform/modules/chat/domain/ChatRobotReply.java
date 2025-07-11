package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatReplyEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务号实体类
 * </p>
 */
@Data
@TableName("chat_robot_reply")
@Accessors(chain = true) // 链式调用
public class ChatRobotReply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long replyId;
    /**
     * 机器人
     */
    private Long robotId;
    /**
     * 类型
     */
    private ChatReplyEnum replyType;
    /**
     * 关键字
     */
    private String replyKey;
    /**
     * 内容
     */
    private String content;

    /**
     * 字段
     */
    public static final String COLUMN_ROBOT_ID = "robot_id";
    public static final String COLUMN_REPLY_TYPE = "reply_type";
    public static final String COLUMN_REPLY_KEY = "reply_key";

}
