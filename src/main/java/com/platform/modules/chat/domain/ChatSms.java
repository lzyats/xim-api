package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 短信记录实体类
 * </p>
 */
@Data
@TableName("chat_sms")
@Accessors(chain = true) // 链式调用
public class ChatSms extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 内容
     */
    private String content;
    /**
     * 账号
     */
    private String mobile;
    /**
     * 状态
     */
    private YesOrNoEnum status;
    /**
     * 结果
     */
    private String body;
    /**
     * 时间
     */
    private Date createTime;

}
