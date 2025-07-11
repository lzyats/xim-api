package com.platform.modules.chat.domain;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * 注销表实体类
 * </p>
 */
@Data
@TableName("chat_user_deleted")
@NoArgsConstructor
public class ChatUserDeleted extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long userId;
    /**
     * 账号
     */
    private String phone;
    /**
     * 操作时间
     */
    private Date createTime;

    public ChatUserDeleted(String phone) {
        this.phone = phone;
    }

    public ChatUserDeleted(Long userId, String phone) {
        this.userId = userId;
        this.phone = phone;
        this.createTime = DateUtil.date();
    }
}
