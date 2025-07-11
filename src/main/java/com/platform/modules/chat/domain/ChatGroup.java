package com.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushGroup;
import com.platform.modules.push.dto.PushSync;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 群组实体类
 * </p>
 */
@Data
@TableName("chat_group")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Accessors(chain = true) // 链式调用
public class ChatGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long groupId;
    /**
     * 群组名称
     */
    private String groupName;
    /**
     * 群组编号
     */
    private String groupNo;
    /**
     * 群组封禁
     */
    private YesOrNoEnum banned;
    /**
     * 群组头像
     */
    private String portrait;
    /**
     * 公告
     */
    private String notice;
    /**
     * 公告置顶
     */
    private YesOrNoEnum noticeTop;
    /**
     * 成员保护
     */
    private YesOrNoEnum configMember;
    /**
     * 允许邀请
     */
    private YesOrNoEnum configInvite;
    /**
     * 全员禁言Y=禁言N=不禁言
     */
    private YesOrNoEnum configSpeak;
    /**
     * 群组头衔
     */
    private YesOrNoEnum configTitle;
    /**
     * 审核开关
     */
    private YesOrNoEnum configAudit;
    /**
     * 发送资源
     */
    private YesOrNoEnum configMedia;
    /**
     * 专属可见
     */
    private YesOrNoEnum configAssign;
    /**
     * 昵称开关
     */
    private YesOrNoEnum configNickname;
    /**
     * 红包开关
     */
    private YesOrNoEnum configPacket;
    /**
     * 显示金额
     */
    private YesOrNoEnum configAmount;
    /**
     * 二维码
     */
    private YesOrNoEnum configScan;
    /**
     * 红包禁抢
     */
    private YesOrNoEnum configReceive;
    /**
     * 群组等级
     */
    private Integer groupLevel;
    /**
     * 群组容量
     */
    private Integer groupLevelCount;
    /**
     * 群组价格
     */
    private BigDecimal groupLevelPrice;
    /**
     * 群组容量时间
     */
    private Date groupLevelTime;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyNo;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyScan;
    /**
     * 隐私开关
     */
    private YesOrNoEnum privacyName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 注销0正常null注销
     */
    @TableLogic
    private Integer deleted;

    public PushFrom getPushFrom() {
        return new PushFrom()
                .setUserId(groupId)
                .setNickname(groupName)
                .setPortrait(portrait)
                .setMsgId(IdWorker.getId())
                .setSyncId(IdWorker.getId())
                .setChatTalk(ChatTalkEnum.GROUP.getType());
    }

    public PushGroup getPushGroup() {
        return new PushGroup()
                .setGroupId(groupId)
                .setGroupName(groupName)
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.GROUP.getType());
    }

    public PushSync getPushSync() {
        return new PushSync()
                .setUserId(groupId)
                .setNickname(groupName)
                .setPortrait(portrait)
                .setChatTalk(ChatTalkEnum.GROUP.getType());
    }

    public ChatGroup(Long groupId) {
        this.groupId = groupId;
    }

    public static final String LABEL_CREATE = "create";
    public static final String LABEL_GROUP_NAME = "groupName";
    public static final String LABEL_GROUP_PORTRAIT = "portrait";
    public static final String LABEL_GROUP_NOTICE = "notice";
    public static final String LABEL_GROUP_NOTICE_TOP = "noticeTop";
    public static final String LABEL_MEMBER_TOP = "memberTop";
    public static final String LABEL_MEMBER_DISTURB = "memberDisturb";
    public static final String LABEL_MEMBER_REMARK = "memberRemark";
    public static final String LABEL_MEMBER_TYPE = "memberType";
    public static final String LABEL_MEMBER_SPEAK = "memberSpeak";
    public static final String LABEL_MEMBER_WHITE = "memberWhite";
    public static final String LABEL_MEMBER_TOTAL = "memberTotal";
    public static final String LABEL_CONFIG_MEMBER = "configMember";
    public static final String LABEL_CONFIG_INVITE = "configInvite";
    public static final String LABEL_CONFIG_TITLE = "configTitle";
    public static final String LABEL_CONFIG_NICKNAME = "configNickname";
    public static final String LABEL_CONFIG_PACKET = "configPacket";
    public static final String LABEL_CONFIG_AMOUNT = "configAmount";
    public static final String LABEL_CONFIG_RECEIVE = "configReceive";
    public static final String LABEL_CONFIG_SCAN = "configScan";
    public static final String LABEL_CONFIG_ASSIGN = "configAssign";
    public static final String LABEL_CONFIG_MEDIA = "configMedia";
    public static final String LABEL_CONFIG_SPEAK = "configSpeak";
    public static final String LABEL_CONFIG_AUDIT = "configAudit";
    public static final String LABEL_PRIVACY_SCAN = "privacyScan";
    public static final String LABEL_PRIVACY_NO = "privacyNo";
    public static final String LABEL_PRIVACY_NAME = "privacyName";
    public static final String LABEL_DELETE = "delete";

    /**
     * 字段
     */
    public static final String COLUMN_GROUP_LEVEL = "group_level";
    public static final String COLUMN_GROUP_LEVEL_TIME = "group_level_time";
}
