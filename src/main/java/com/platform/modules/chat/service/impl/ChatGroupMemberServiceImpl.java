package com.platform.modules.chat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupMemberDao;
import com.platform.modules.chat.domain.ChatGroupMember;
import com.platform.modules.chat.enums.GroupMemberEnum;
import com.platform.modules.chat.service.ChatGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务层实现
 * </p>
 */
@Service("chatGroupMemberService")
@CacheConfig(cacheNames = AppConstants.REDIS_CHAT_GROUP_MEMBER)
public class ChatGroupMemberServiceImpl extends BaseServiceImpl<ChatGroupMember> implements ChatGroupMemberService {

    @Resource
    private ChatGroupMemberDao chatGroupMemberDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupMemberDao);
    }

    @Override
    public List<ChatGroupMember> queryList(ChatGroupMember t) {
        List<ChatGroupMember> dataList = chatGroupMemberDao.queryList(t);
        return dataList;
    }

    @Override
    public Integer updateById(ChatGroupMember groupMember) {
        Integer result = 0;
        // 查询数据
        ChatGroupMember chatGroupMember = this.getById(groupMember.getMemberId());
        if (chatGroupMember == null) {
            return result;
        }
        // 执行更新
        result = super.updateById(groupMember);
        // 清除缓存
        this.clearCache(chatGroupMember.getGroupId(), chatGroupMember.getUserId());
        return result;
    }

    @Override
    @Cacheable(key = "#groupId + ':' + #userId", unless = "#result == null")
    public ChatGroupMember queryGroupMember(Long groupId, Long userId) {
        return this.queryOne(new ChatGroupMember().setGroupId(groupId).setUserId(userId));
    }

    @Override
    public ChatGroupMember verifyGroupMember(Long groupId, Long userId) {
        ChatGroupMember groupMember = this.queryGroupMember(groupId, userId);
        if (groupMember == null) {
            throw new BaseException("你不在当前群中");
        }
        return groupMember;
    }

    @Override
    public ChatGroupMember verifyGroupMaster(Long groupId, Long userId) {
        ChatGroupMember groupMember = this.verifyGroupMember(groupId, userId);
        if (!GroupMemberEnum.MASTER.equals(groupMember.getMemberType())) {
            throw new BaseException("你不是群主，不能操作");
        }
        return groupMember;
    }

    @Override
    public ChatGroupMember verifyGroupManager(Long groupId, Long userId) {
        ChatGroupMember groupMember = this.verifyGroupMember(groupId, userId);
        if (GroupMemberEnum.NORMAL.equals(groupMember.getMemberType())) {
            throw new BaseException("你不是管理员，不能操作");
        }
        return groupMember;
    }

    @Override
    public ChatGroupMember queryGroupMaster(Long groupId) {
        ChatGroupMember query = new ChatGroupMember()
                .setGroupId(groupId)
                .setMemberType(GroupMemberEnum.MASTER);
        return this.queryOne(query);
    }

    @Override
    public Long getGroupMaster(Long groupId) {
        ChatGroupMember groupMember = this.queryGroupMaster(groupId);
        return groupMember.getUserId();
    }

    @Override
    public List<Long> getGroupManager(Long groupId) {
        ChatGroupMember query = new ChatGroupMember()
                .setGroupId(groupId)
                .setMemberType(GroupMemberEnum.MANAGER);
        List<ChatGroupMember> dataList = this.queryList(query);
        // 集合取属性
        return dataList.stream().map(ChatGroupMember::getUserId).collect(Collectors.toList());
    }

    @Override
    public List<Long> getGroupAdmin(Long groupId) {
        // 查询接收人
        List<Long> managerList = this.getGroupManager(groupId);
        managerList.add(this.getGroupMaster(groupId));
        return managerList;
    }

    @Override
    public List<ChatGroupMember> queryMemberList(Long groupId) {
        ChatGroupMember query = new ChatGroupMember().setGroupId(groupId);
        return queryList(query);
    }

    @Override
    public List<Long> getMemberList(Long groupId) {
        // 查询缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        if (redisUtils.sSize(redisKey) > 0) {
            Set<String> dataList = redisUtils.sMembers(redisKey);
            return new ArrayList<>(dataList)
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        // 查询所有成员
        List<ChatGroupMember> dataList = this.queryList(new ChatGroupMember().setGroupId(groupId));
        List<Long> memberList = dataList.stream().map(ChatGroupMember::getUserId).collect(Collectors.toList());
        // 增加成员
        _addReceive(groupId, memberList);
        return memberList;
    }

    /**
     * 增加成员
     */
    private void _addReceive(Long groupId, List<Long> memberList) {
        // 查询缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        if (!CollectionUtils.isEmpty(memberList)) {
            List<String> value = memberList.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toList());
            redisUtils.sAdd(redisKey, value, 30, TimeUnit.DAYS);
        }
    }

    @Override
    public Integer getMemberSize(Long groupId) {
        // 查询缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        Long size = redisUtils.sSize(redisKey);
        if (size > 0) {
            return size.intValue();
        }
        List<Long> memberList = getMemberList(groupId);
        return memberList.size();
    }

    @Override
    public void addMember(Long groupId, List<Long> memberList) {
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        Long size = redisUtils.sSize(redisKey);
        if (size > 0) {
            // 增加成员
            _addReceive(groupId, memberList);
        }
    }

    @Override
    public void removeMember(Long groupId, List<Long> memberList) {
        // 删除数据
        this.delete(Wrappers.<ChatGroupMember>lambdaUpdate()
                .eq(ChatGroupMember::getGroupId, groupId)
                .in(ChatGroupMember::getUserId, memberList));
        // 组装数据
        List<String> cacheList = new ArrayList<>();
        List<String> userList = new ArrayList<>();
        memberList.forEach(userId -> {
            cacheList.add(StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_GROUP_MEMBER, groupId, userId));
            userList.add(NumberUtil.toStr(userId));
        });
        // 删除缓存
        redisUtils.delete(cacheList);
        // 删除缓存
        String redisKey = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        redisUtils.sRemove(redisKey, userList);
    }

    @Override
    public void editRemark(Long groupId, Long userId, String remark) {
        // 更新
        this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                .set(ChatGroupMember::getRemark, remark)
                .eq(ChatGroupMember::getGroupId, groupId)
                .eq(ChatGroupMember::getUserId, userId)
        );
        // 清除缓存
        this.clearCache(groupId, userId);
    }

    @Override
    public void clearCache(Long groupId) {
        String pattern1 = StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_GROUP_MEMBER, groupId, "*");
        redisUtils.delete(pattern1);
        // 查询缓存
        String pattern2 = StrUtil.format(AppConstants.REDIS_CHAT_GROUP_RECEIVE, groupId);
        redisUtils.delete(pattern2);
    }

    @Override
    public void clearCache(Long groupId, Long userId) {
        String pattern = StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_GROUP_MEMBER, groupId, userId);
        redisUtils.delete(pattern);
    }

    @Override
    public List<Long> setManager(Long groupId, List<Long> userList) {
        // 取消
        this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                .set(ChatGroupMember::getMemberType, GroupMemberEnum.NORMAL)
                .eq(ChatGroupMember::getMemberType, GroupMemberEnum.MANAGER)
                .eq(ChatGroupMember::getGroupId, groupId));
        // 成员
        List<Long> memberList = this.getMemberList(groupId);
        // 取交集
        List<Long> dataList = (List<Long>) CollUtil.intersection(userList, memberList);
        if (!CollectionUtils.isEmpty(dataList)) {
            // 更新
            this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                    .set(ChatGroupMember::getMemberType, GroupMemberEnum.MANAGER)
                    .eq(ChatGroupMember::getGroupId, groupId)
                    .in(ChatGroupMember::getUserId, dataList));
            // 缓存
            this.clearCache(groupId);
        }
        return dataList;
    }

    @Override
    public List<Long> queryPacketWhite(Long groupId) {
        QueryWrapper<ChatGroupMember> wrapper = new QueryWrapper();
        wrapper.eq(ChatGroupMember.COLUMN_GROUP_ID, groupId);
        wrapper.eq(ChatGroupMember.COLUMN_PACKET_WHITE, YesOrNoEnum.YES);
        List<ChatGroupMember> dataList = this.queryList(wrapper);
        return dataList.stream().map(ChatGroupMember::getUserId).collect(Collectors.toList());
    }

    @Override
    public void editPacketWhite(Long groupId, List<Long> memberList) {
        // 取消
        this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                .set(ChatGroupMember::getPacketWhite, YesOrNoEnum.NO)
                .eq(ChatGroupMember::getGroupId, groupId));
        // 设置
        if (!CollectionUtils.isEmpty(memberList)) {
            this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                    .set(ChatGroupMember::getPacketWhite, YesOrNoEnum.YES)
                    .eq(ChatGroupMember::getGroupId, groupId)
                    .in(ChatGroupMember::getUserId, memberList));
        }
        // 缓存
        this.clearCache(groupId);
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                .set(ChatGroupMember::getNickname, nickname)
                .eq(ChatGroupMember::getUserId, current));
        // 清空缓存
        this.clearMemberCache(current);
    }

    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        // 修改数据
        this.update(Wrappers.<ChatGroupMember>lambdaUpdate()
                .set(ChatGroupMember::getPortrait, portrait)
                .eq(ChatGroupMember::getUserId, current));
        // 清空缓存
        this.clearMemberCache(current);
    }

    @Override
    public void delGroup(Long groupId) {
        // 删除缓存
        this.delete(Wrappers.<ChatGroupMember>lambdaUpdate()
                .eq(ChatGroupMember::getGroupId, groupId));
        // 删除缓存
        this.clearCache(groupId);
    }

    /**
     * 删除成员缓存
     */
    private void clearMemberCache(Long userId) {
        String pattern = StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_GROUP_MEMBER, "*", userId);
        redisUtils.delete(pattern);
    }

}
