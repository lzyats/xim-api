package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupMember;

import java.util.List;

/**
 * <p>
 * 服务层
 * </p>
 */
public interface ChatGroupMemberService extends BaseService<ChatGroupMember> {

    /**
     * 查询详情
     */
    ChatGroupMember queryGroupMember(Long groupId, Long userId);

    /**
     * 验证成员
     */
    ChatGroupMember verifyGroupMember(Long groupId, Long userId);

    /**
     * 验证群主
     */
    ChatGroupMember verifyGroupMaster(Long groupId, Long userId);

    /**
     * 验证管理员
     */
    ChatGroupMember verifyGroupManager(Long groupId, Long userId);

    /**
     * 查询群主
     */
    ChatGroupMember queryGroupMaster(Long groupId);

    /**
     * 查询群主
     */
    Long getGroupMaster(Long groupId);

    /**
     * 查询管理员
     */
    List<Long> getGroupManager(Long groupId);

    /**
     * 查询管理员
     */
    List<Long> getGroupAdmin(Long groupId);

    /**
     * 查询成员
     */
    List<ChatGroupMember> queryMemberList(Long groupId);

    /**
     * 成员列表
     */
    List<Long> getMemberList(Long groupId);

    /**
     * 成员数量
     */
    Integer getMemberSize(Long groupId);

    /**
     * 增加成员
     */
    void addMember(Long groupId, List<Long> memberList);

    /**
     * 移除成员
     */
    void removeMember(Long groupId, List<Long> memberList);

    /**
     * 修改成员
     */
    void editRemark(Long groupId, Long userId, String remark);

    /**
     * 删除缓存
     */
    void clearCache(Long groupId);

    /**
     * 删除缓存
     */
    void clearCache(Long groupId, Long userId);

    /**
     * 设置管理员
     */
    List<Long> setManager(Long groupId, List<Long> userList);

    /**
     * 查询红包白名单
     */
    List<Long> queryPacketWhite(Long groupId);

    /**
     * 修改红包白名单
     */
    void editPacketWhite(Long groupId, List<Long> memberList);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 删除
     */
    void delGroup(Long groupId);

}
