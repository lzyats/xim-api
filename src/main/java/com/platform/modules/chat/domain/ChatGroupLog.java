package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.GroupLogEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 群组日志实体类
 * </p>
 */
@Data
@TableName("chat_group_log")
@Accessors(chain = true) // 链式调用
public class ChatGroupLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 日志类型
     */
    private GroupLogEnum logType;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;

}
