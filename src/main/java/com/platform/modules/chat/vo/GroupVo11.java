package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.GroupMemberEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class GroupVo11 {

    /**
     * 群组id
     */
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
     * 全员禁言
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
     * 成员类型
     */
    private GroupMemberEnum memberType;
    /**
     * 用户备注
     */
    private String memberRemark;
    /**
     * 是否置顶
     */
    private YesOrNoEnum memberTop;
    /**
     * 是否免打扰
     */
    private YesOrNoEnum memberDisturb;
    /**
     * 是否禁言
     */
    private YesOrNoEnum memberSpeak;
    /**
     * 是否白名单
     */
    private YesOrNoEnum memberWhite;
    /**
     * 群员数量
     */
    private String memberSize = "0";
    /**
     * 群员总数
     */
    private String memberTotal = "0";

}
