package com.platform.modules.chat.domain;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.enums.GroupMemberEnum;
import com.platform.modules.chat.enums.GroupSourceEnum;
import com.platform.modules.push.dto.PushFrom;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <p>
 * 实体类
 * </p>
 */
@Data
@TableName("chat_group_member")
@Accessors(chain = true) // 链式调用
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ChatGroupMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long memberId;
    /**
     * 群组id
     */
    private Long groupId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 聊天号码
     */
    private String userNo;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String portrait;
    /**
     * 用户备注
     */
    private String remark;
    /**
     * 成员类型
     */
    private GroupMemberEnum memberType;
    /**
     * 是否置顶
     */
    private YesOrNoEnum top;
    /**
     * 是否免打扰
     */
    private YesOrNoEnum disturb;
    /**
     * 来源
     */
    private GroupSourceEnum memberSource;
    /**
     * 接收白名单，Y=白名单
     */
    private YesOrNoEnum packetWhite;
    /**
     * 禁言开关
     */
    private YesOrNoEnum speak;
    /**
     * 禁言时间
     */
    private Date speakTime;
    /**
     * 加入时间
     */
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

    public ChatGroupMember(Long memberId) {
        this.memberId = memberId;
    }

    public ChatGroupMember(Long groupId, ChatUser chatUser) {
        Date now = DateUtil.date();
        this.groupId = groupId;
        this.userId = chatUser.getUserId();
        this.userNo = chatUser.getUserNo();
        this.nickname = chatUser.getNickname();
        this.portrait = chatUser.getPortrait();
        this.memberType = GroupMemberEnum.NORMAL;
        this.memberSource = GroupSourceEnum.INVITE;
        this.packetWhite = YesOrNoEnum.NO;
        this.speak = YesOrNoEnum.NO;
        this.top = YesOrNoEnum.NO;
        this.disturb = YesOrNoEnum.NO;
        this.deleted = 0;
        this.speakTime = now;
        this.createTime = now;
    }

    public PushFrom getPushFrom(Long msgId, Long syncId) {
        String title = "";
        if (!GroupMemberEnum.NORMAL.equals(memberType)) {
            title = memberType.getInfo();
        }
        return new PushFrom()
                .setGroupId(groupId)
                .setUserId(userId)
                .setNickname(getDefaultRemark())
                .setPortrait(portrait)
                .setSign(ShiroUtils.getSign())
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setChatTalk(ChatTalkEnum.GROUP.getType())
                .setTitle(title);
    }

    /**
     * 字段
     */
    public static final String COLUMN_GROUP_ID = "group_id";
    public static final String COLUMN_PACKET_WHITE = "packet_white";

}
