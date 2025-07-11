package com.platform.modules.chat.domain;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天帮助实体类
 * </p>
 */
@Data
@TableName("chat_help")
@Accessors(chain = true) // 链式调用
public class ChatHelp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long helpId;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private YesOrNoEnum status = YesOrNoEnum.YES;
    /**
     * 排序
     */
    private Integer sort;

}
