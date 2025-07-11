package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 版本实体类
 * </p>
 */
@Data
@TableName("chat_version")
@Accessors(chain = true) // 链式调用
public class ChatVersion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 版本
     */
    private String version;
    /**
     * 设备
     */
    private String device;
    /**
     * 地址
     */
    private String url;
    /**
     * 内容
     */
    private String content;

}
