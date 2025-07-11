package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.GroupSourceEnum;
import com.platform.modules.chat.vo.*;

import java.util.List;

/**
 * <p>
 * 群组 服务层
 * </p>
 */
public interface ChatGroupService extends BaseService<ChatGroup> {

    /**
     * 搜索
     */
    PageInfo searchGroup(String param);

    /**
     * 扫码
     */
    GroupVo39 scan(Long groupId);

    /**
     * 建组
     */
    void create(GroupVo16 groupVo);

    /**
     * 查询列表
     */
    List<GroupVo11> groupList();

    /**
     * 详情
     */
    GroupVo11 getInfo(Long groupId);

    /**
     * 加群
     */
    void join(GroupVo23 groupVo);

    /**
     * 加群
     */
    void addGroup(ChatGroup chatGroup, ChatUser chatUser, GroupSourceEnum memberSource);

    /**
     * 成员
     */
    List<GroupVo08> getMemberList(Long groupId);

    /**
     * 邀请
     */
    void invite(GroupVo01 groupVo);

    /**
     * 设置置顶
     */
    void setTop(GroupVo04 groupVo);

    /**
     * 设置免打扰
     */
    void setDisturb(GroupVo05 groupVo);

    /**
     * 设置群昵称
     */
    void setRemark(GroupVo10 groupVo);

    /**
     * 修改成员昵称
     */
    void setNickname(GroupVo21 groupVo);

    /**
     * 移出群组
     */
    void kicked(GroupVo24 groupVo);

    /**
     * 修改成员保护
     */
    void editConfigMember(GroupVo06 groupVo);

    /**
     * 修改成员邀请
     */
    void editConfigInvite(GroupVo12 groupVo);

    /**
     * 修改群组头衔
     */
    void editConfigTitle(GroupVo17 groupVo);

    /**
     * 红包开关
     */
    void editConfigPacket(GroupVo18 groupVo);

    /**
     * 显示金额
     */
    void editConfigAmount(GroupVo44 groupVo);

    /**
     * 接收红包
     */
    void editConfigReceive(GroupVo36 groupVo);

    /**
     * 二维码开关
     */
    void editConfigScan(GroupVo46 groupVo);

    /**
     * 专属可见
     */
    void editConfigAssign(GroupVo19 groupVo);

    /**
     * 允许资源
     */
    void editConfigMedia(GroupVo20 groupVo);

    /**
     * 全员禁言
     */
    void editConfigSpeak(GroupVo13 groupVo);

    /**
     * 审核开关
     */
    void editConfigAudit(GroupVo22 groupVo);

    /**
     * 转让
     */
    void transfer(GroupVo14 groupVo);

    /**
     * 设置管理员
     */
    void setManager(GroupVo15 groupVo);

    /**
     * 公告置顶
     */
    void editNoticeTop(GroupVo07 groupVo);

    /**
     * 退出
     */
    void logout(Long groupId);

    /**
     * 解散群组
     */
    void dissolve(Long groupId);

    /**
     * 修改群名
     */
    void editGroupName(GroupVo02 groupVo);

    /**
     * 修改群头像
     */
    void editPortrait(GroupVo09 groupVo);

    /**
     * 修改群公告
     */
    void editNotice(GroupVo03 groupVo);

    /**
     * 指定禁言
     */
    void speak(GroupVo26 groupVo);

    /**
     * 扩容价格
     */
    List<GroupVo27> groupLevelPrice(Long groupId);

    /**
     * 扩容支付
     */
    void groupLevelPay(GroupVo28 groupVo);

    /**
     * 修改隐私开关
     */
    void editPrivacyNo(GroupVo34 groupVo);

    /**
     * 修改隐私开关
     */
    void editPrivacyScan(GroupVo38 groupVo);

    /**
     * 修改隐私开关
     */
    void editPrivacyName(GroupVo45 groupVo);

    /**
     * 群内昵称
     */
    void editConfigNickname(GroupVo35 groupVo);

    /**
     * 查询红包白名单
     */
    List<Long> queryPacketWhite(Long groupId);

    /**
     * 修改红包白名单
     */
    void editPacketWhite(GroupVo37 groupVo);

}
