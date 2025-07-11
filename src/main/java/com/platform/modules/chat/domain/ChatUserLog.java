package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.UserLogEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户日志实体类
 * </p>
 */
@Data
@TableName("chat_user_log")
@Accessors(chain = true) // 链式调用
public class ChatUserLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 类型
     */
    private UserLogEnum logType;
    /**
     * 操作内容
     */
    private String content;
    /**
     * ip
     */
    private String ip;
    /**
     * ip地址
     */
    private String ipAddr;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 设备版本
     */
    private String deviceVersion;
    /**
     * 创建时间
     */
    private Date createTime;

}
