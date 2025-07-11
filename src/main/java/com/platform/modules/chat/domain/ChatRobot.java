package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSync;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务号实体类
 * </p>
 */
@Data
@TableName("chat_robot")
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class ChatRobot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long robotId;
    /**
     * 秘钥
     */
    private String secret;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 菜单
     */
    private String menu;
    /**
     * 置顶
     */
    @TableField(exist = false)
    private YesOrNoEnum top;
    /**
     * 静默
     */
    @TableField(exist = false)
    private YesOrNoEnum disturb;

    public PushFrom getPushFrom() {
        Long msgId = IdWorker.getId();
        return new PushFrom()
                .setUserId(robotId)
                .setNickname(nickname)
                .setPortrait(portrait)
                .setMsgId(msgId)
                .setSyncId(msgId)
                .setChatTalk(ChatTalkEnum.ROBOT.getType());
    }

    public PushFrom getPushFrom(Long msgId) {
        return new PushFrom()
                .setUserId(robotId)
                .setNickname(nickname)
                .setPortrait(portrait)
                .setMsgId(msgId)
                .setSyncId(msgId)
                .setChatTalk(ChatTalkEnum.ROBOT.getType());
    }

    public PushSync getPushSync() {
        return new PushSync()
                .setUserId(robotId)
                .setNickname(nickname)
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.ROBOT.getType());
    }

    public static final String LABEL_TOP = "top";
    public static final String LABEL_DISTURB = "disturb";
}
