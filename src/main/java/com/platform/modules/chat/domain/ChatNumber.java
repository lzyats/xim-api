package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 微聊号码实体类
 * </p>
 */
@Data
@TableName("chat_number")
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class ChatNumber extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    private String chatNo;
    /**
     * 是否使用
     */
    private YesOrNoEnum status = YesOrNoEnum.NO;

    public ChatNumber(String chatNo) {
        this.chatNo = chatNo;
        this.status = YesOrNoEnum.YES;
    }
}
