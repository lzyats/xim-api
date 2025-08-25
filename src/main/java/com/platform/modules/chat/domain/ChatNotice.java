package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.common.web.domain.JsonDateDeserializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 通知公告实体类
 * </p>
 */
@Data
@TableName("chat_notice")
@Accessors(chain = true) // 链式调用
public class ChatNotice extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @JsonIgnore
    private Long noticeId;
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
    @JsonIgnore
    private YesOrNoEnum status;
    /**
     * 发布时间
     */
    private Date createTime;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date updateTime;

    private int isindex;

}
