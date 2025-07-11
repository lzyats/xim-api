package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天头像实体类
 * </p>
 */
@Data
@TableName("chat_portrait")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatPortrait extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 类型
     */
    private ChatTalkEnum chatType;
    /**
     * 状态
     */
    private YesOrNoEnum status;

    public ChatPortrait(ChatTalkEnum chatType) {
        this.chatType = chatType;
        this.status = YesOrNoEnum.YES;
    }
}
