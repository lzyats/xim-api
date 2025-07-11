package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 聊天资源实体类
 * </p>
 */
@Data
@TableName("chat_resource")
@Accessors(chain = true) // 链式调用
public class ChatResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long resourceId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 图片地址
     */
    private String url;
    /**
     * 创建时间
     */
    private Date createTime;

}
