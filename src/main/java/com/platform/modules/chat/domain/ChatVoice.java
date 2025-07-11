package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 声音表实体类
 * </p>
 */
@Data
@TableName("chat_voice")
@Accessors(chain = true) // 链式调用
public class ChatVoice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long msgId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 地址
     */
    private String voiceUrl;
    /**
     * 文本
     */
    private String voiceText;
    /**
     * 时间
     */
    private Date createTime;

}
