package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 收藏表实体类
 * </p>
 */
@Data
@TableName("chat_user_collect")
@Accessors(chain = true) // 链式调用
public class ChatUserCollect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long collectId;
    /**
     * 用户id
     */
    private Long userId;
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

}
