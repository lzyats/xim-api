package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户详情实体类
 * </p>
 */
@Data
@NoArgsConstructor
@TableName("chat_user_info")
@Accessors(chain = true) // 链式调用
public class ChatUserInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 正面
     */
    private String identity1;
    /**
     * 反面
     */
    private String identity2;
    /**
     * 手持
     */
    private String holdCard;
    /**
     * 认证原因
     */
    private String authReason;
    /**
     * 认证时间
     */
    private Date authTime;

    public ChatUserInfo(Long userId) {
        this.userId = userId;
    }

}
