package com.platform.modules.chat.domain;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务号实体类
 * </p>
 */
@Data
@TableName("chat_robot_sub")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatRobotSub extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long subId;
    /**
     * 机器人
     */
    private Long robotId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 置顶
     */
    private YesOrNoEnum top;
    /**
     * 静默
     */
    private YesOrNoEnum disturb;

    public ChatRobotSub(Long subId) {
        this.subId = subId;
    }

    public ChatRobotSub(Long robotId, Long userId) {
        this.robotId = robotId;
        this.userId = userId;
    }
}
