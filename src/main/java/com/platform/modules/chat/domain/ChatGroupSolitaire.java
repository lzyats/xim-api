package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 成语接龙实体类
 * </p>
 */
@Data
@TableName("chat_group_solitaire")
@Accessors(chain = true) // 链式调用
public class ChatGroupSolitaire extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long solitaireId;
    /**
     * 发起人
     */
    private Long userId;
    /**
     * 群组
     */
    private Long groupId;
    /**
     * 主题
     */
    private String subject;
    /**
     * 例子
     */
    private String example;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
