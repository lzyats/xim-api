package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.BannedTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 骚扰举报实体类
 * </p>
 */
@Data
@TableName("chat_group_inform")
@Accessors(chain = true) // 链式调用
public class ChatGroupInform extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long informId;
    /**
     * 类型
     */
    private BannedTypeEnum informType;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 目标id
     */
    private Long groupId;
    /**
     * 图片
     */
    private String images;
    /**
     * 内容
     */
    private String content;
    /**
     * 处理状态
     */
    private YesOrNoEnum status;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 字段
     */
    public static final String COLUMN_STATUS = "status";

}
