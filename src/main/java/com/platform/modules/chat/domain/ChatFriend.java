package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.enums.FriendSourceEnum;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSync;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 好友表实体类
 * </p>
 */
@Data
@TableName("chat_friend")
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class ChatFriend extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long friendId;
    /**
     * 当前id
     */
    private Long currentId;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 备注
     */
    private String remark;
    /**
     * 聊天号码
     */
    private String userNo;
    /**
     * 申请来源
     */
    private FriendSourceEnum source;
    /**
     * 是否黑名单
     */
    private YesOrNoEnum black;
    /**
     * 是否静默
     */
    private YesOrNoEnum disturb;
    /**
     * 是否置顶
     */
    private YesOrNoEnum top;
    /**
     * 创建时间
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    @TableLogic
    private Integer deleted;

    public String getDefaultRemark() {
        if (StringUtils.isEmpty(remark)) {
            return nickname;
        }
        return remark;
    }

    public PushFrom getPushFrom(Long msgId) {
        return new PushFrom()
                .setMsgId(msgId)
                .setSyncId(msgId)
                .setGroupId(groupId)
                .setUserId(userId)
                .setNickname(getDefaultRemark())
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.FRIEND.getType());
    }

    public PushSync getPushSync() {
        return new PushSync()
                .setUserId(userId)
                .setNickname(getDefaultRemark())
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.FRIEND.getType());
    }

    public static final String LABEL_CREATE = "create";
    public static final String LABEL_REMARK = "remark";
    public static final String LABEL_TOP = "top";
    public static final String LABEL_BLACK = "black";
    public static final String LABEL_DISTURB = "disturb";
    public static final String LABEL_DELETE = "delete";

}
