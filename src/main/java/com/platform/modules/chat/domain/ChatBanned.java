package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 封禁状态实体类
 * </p>
 */
@Data
@TableName("chat_banned")
@Accessors(chain = true) // 链式调用
public class ChatBanned extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 封禁id
     */
    @TableId
    private Long bannedId;
    /**
     * 封禁原因
     */
    private String bannedReason;
    /**
     * 封禁时间
     */
    private Date bannedTime;

    /**
     * 字段
     */
    public static final String COLUMN_BANNED_TIME = "banned_time";
}
