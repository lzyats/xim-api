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
 * 用户访问实体类
 * </p>
 */
@Data
@TableName("chat_visit")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatVisit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 访问id
     */
    @TableId
    private Long visitId;
    /**
     * 访问时间
     */
    private Date visitDate;
    /**
     * 访问次数
     */
    private Integer visitCount;

    public ChatVisit(Date visitDate, Integer visitCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
    }
}
