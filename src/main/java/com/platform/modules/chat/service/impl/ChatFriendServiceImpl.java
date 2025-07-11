package com.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFriendDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.FriendSourceEnum;
import com.platform.modules.chat.enums.FriendTypeEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.*;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 好友表 服务层实现
 * </p>
 */
@Slf4j
@Service("chatFriendService")
@CacheConfig(cacheNames = AppConstants.REDIS_CHAT_FRIEND)
public class ChatFriendServiceImpl extends BaseServiceImpl<ChatFriend> implements ChatFriendService {

    @Resource
    private ChatFriendDao chatFriendDao;

    @Resource
    @Lazy
    private ChatUserService chatUserService;

    @Resource
    private ChatFriendService chatFriendService;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private PushService pushService;

    @Resource
    private ChatConfigService chatConfigService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFriendDao);
    }

    @Override
    public List<ChatFriend> queryList(ChatFriend t) {
        List<ChatFriend> dataList = chatFriendDao.queryList(t);
        return dataList;
    }

    @Override
    public void setBlack(FriendVo03 friendVo) {
        Long current = ShiroUtils.getUserId();
        Long userId = friendVo.getUserId();
        // 校验好友
        ChatFriend friend = chatFriendService.getFriend(current, userId);
        if (friend == null) {
            throw new BaseException(AppConstants.TIPS_FRIEND_NONE);
        }
        // 更新数据
        this.updateById(new ChatFriend().setFriendId(friend.getFriendId()).setBlack(friendVo.getBlack()));
        // 移除缓存
        this.clearCache(current, userId);
        // 推送通知
        pushSetting(current, userId, ChatFriend.LABEL_BLACK, friendVo.getBlack().getCode());
    }

    @Transactional
    @Override
    public void delFriend(Long userId) {
        Long current = ShiroUtils.getUserId();
        // 查询好友
        ChatFriend chatFriend = chatFriendService.getFriend(current, userId);
        // 删除好友
        chatFriendDao.delete(Wrappers.<ChatFriend>lambdaUpdate()
                .eq(ChatFriend::getCurrentId, current)
                .eq(ChatFriend::getUserId, userId));
        // 移除缓存
        this.clearCache(current, userId);
        // 清空消息
        if (chatFriend != null) {
            pushService.clearMsg(current, chatFriend.getGroupId());
        }
        // 通知推送
        pushSetting(current, userId, ChatFriend.LABEL_DELETE, "");
    }

    @Override
    public void setRemark(FriendVo05 friendVo) {
        Long current = ShiroUtils.getUserId();
        Long userId = friendVo.getUserId();
        String remark = friendVo.getRemark();
        // 校验好友
        ChatFriend friend = chatFriendService.getFriend(current, userId);
        if (friend == null) {
            throw new BaseException(AppConstants.TIPS_FRIEND_NONE);
        }
        // 更新数据
        this.updateById(new ChatFriend().setFriendId(friend.getFriendId()).setRemark(remark));
        // 移除缓存
        this.clearCache(current, userId);
        // 通知推送
        if (StringUtils.isEmpty(remark)) {
            pushSetting(current, userId, ChatFriend.LABEL_REMARK, friend.getNickname());
        } else {
            pushSetting(current, userId, ChatFriend.LABEL_REMARK, remark);
        }
    }

    @Override
    public void setTop(FriendVo06 friendVo) {
        Long current = ShiroUtils.getUserId();
        Long userId = friendVo.getUserId();
        // 如果是自己，则不提示
        if (current.equals(userId)) {
            return;
        }
        // 校验好友
        ChatFriend friend = chatFriendService.getFriend(current, userId);
        if (friend == null) {
            throw new BaseException(AppConstants.TIPS_FRIEND_NONE);
        }
        // 更新数据
        this.updateById(new ChatFriend().setFriendId(friend.getFriendId()).setTop(friendVo.getTop()));
        // 移除缓存
        this.clearCache(current, userId);
        // 推送通知
        pushSetting(current, userId, ChatFriend.LABEL_TOP, friendVo.getTop().getCode());
    }

    @Override
    public void setDisturb(FriendVo08 friendVo) {
        Long current = ShiroUtils.getUserId();
        Long userId = friendVo.getUserId();
        // 校验好友
        ChatFriend friend = chatFriendService.getFriend(current, userId);
        if (friend == null) {
            throw new BaseException(AppConstants.TIPS_FRIEND_NONE);
        }
        // 更新数据
        this.updateById(new ChatFriend().setFriendId(friend.getFriendId()).setDisturb(friendVo.getDisturb()));
        // 移除缓存
        this.clearCache(current, userId);
        // 推送通知
        pushSetting(current, userId, ChatFriend.LABEL_DISTURB, friendVo.getDisturb().getCode());
    }

    @Override
    public List<FriendVo09> getFriendList() {
        Long current = ShiroUtils.getUserId();
        // 好友列表
        List<FriendVo09> dataList = chatFriendDao.getFriendList(current);
        // 审核
        String phoneConfig = chatConfigService.queryConfig(ChatConfigEnum.SYS_PHONE).getStr();
        if (!ShiroUtils.getPhone().equals(phoneConfig)) {
            // 自己
            dataList.add(self(current));
        }
        return dataList;
    }

    /**
     * 自己
     */
    private FriendVo09 self(Long current) {
        ChatUser chatUser = chatUserService.getById(current);
        FriendVo09 friendVo = new FriendVo09(chatUser);
        return friendVo.setFriendType(FriendTypeEnum.SELF)
                .setGroupId(current)
                .setFriendSource(FriendSourceEnum.SELF);
    }

    @Override
    public FriendVo09 getInfo(Long userId) {
        Long current = ShiroUtils.getUserId();
        // 自己
        if (current.equals(userId)) {
            return self(current);
        }
        // 不存在
        ChatUser chatUser = chatUserService.getById(userId);
        if (chatUser == null) {
            return null;
        }
        // 格式化
        FriendVo09 friendVo = new FriendVo09(chatUser);
        // 校验好友
        ChatFriend friend = chatFriendService.getFriend(current, userId);
        if (friend != null) {
            friendVo.setRemark(friend.getRemark())
                    .setTop(friend.getTop())
                    .setDisturb(friend.getDisturb())
                    .setBlack(friend.getBlack())
                    .setGroupId(friend.getGroupId())
                    .setFriendType(FriendTypeEnum.FRIEND)
                    .setFriendSource(friend.getSource());
        }
        return friendVo;
    }

    @Override
    @Cacheable(key = "#current + ':' + #userId", unless = "#result == null")
    public ChatFriend getFriend(Long current, Long userId) {
        return queryOne(new ChatFriend().setCurrentId(current).setUserId(userId));
    }

    @Override
    public List<Long> verifyFriend(List<Long> friendList) {
        Long current = ShiroUtils.getUserId();
        // 查询列表
        List<Long> dataList = this.queryFriendList(current);
        // 取交集
        List<Long> memberList = (List<Long>) CollUtil.intersection(friendList, dataList);
        if (CollectionUtils.isEmpty(memberList)) {
            throw new BaseException("邀请的人不是你的好友");
        }
        return memberList;
    }

    @Override
    public FriendVo01 searchFriend(String param) {
        // 用户详情
        ChatUser result = null;
        // 来源
        FriendSourceEnum source = null;
        // 按账号搜索
        if (source == null) {
            ChatUser chatUser = chatUserService.queryOne(new ChatUser().setPhone(param));
            if (chatUser != null) {
                source = FriendSourceEnum.PHONE;
                if (YesOrNoEnum.YES.equals(chatUser.getPrivacyPhone())) {
                    result = chatUser;
                }
            }
        }
        // 按微聊号搜索
        if (source == null) {
            ChatUser chatUser = chatUserService.queryOne(new ChatUser().setUserNo(param));
            if (chatUser != null) {
                source = FriendSourceEnum.NO;
                if (YesOrNoEnum.YES.equals(chatUser.getPrivacyNo())) {
                    result = chatUser;
                }
            }
        }
        if (result == null) {
            throw new BaseException("暂无结果，对方可能开启了隐私保护");
        }
        return BeanUtil.toBean(result, FriendVo01.class).setSource(source);
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        // 更新数据
        this.update(Wrappers.<ChatFriend>lambdaUpdate()
                .set(ChatFriend::getNickname, nickname)
                .eq(ChatFriend::getUserId, current));
        // 清空缓存
        this.clearCache(current);
    }

    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        // 更新数据
        this.update(Wrappers.<ChatFriend>lambdaUpdate()
                .set(ChatFriend::getPortrait, portrait)
                .eq(ChatFriend::getUserId, current));
        // 清空缓存
        this.clearCache(current);
    }

    @Override
    public void pushSetting(Long userId, Long object, String label, String value) {
        PushSetting setting = new PushSetting(PushSettingEnum.FRIEND, object, label, value);
        pushService.pushSetting(setting, Arrays.asList(userId));
    }

    /**
     * 查询好友列表
     */
    private List<Long> queryFriendList(Long userId) {
        List<ChatFriend> dataList = this.queryList(new ChatFriend().setUserId(userId));
        return dataList.stream().map(ChatFriend::getUserId).collect(Collectors.toList());
    }

    /**
     * 删除好友缓存
     */
    private void clearCache(Long current, Long userId) {
        redisUtils.delete(StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_FRIEND, current, userId));
    }

    /**
     * 删除好友缓存
     */
    private void clearCache(Long userId) {
        String pattern = StrUtil.format("{}::{}:{}", AppConstants.REDIS_CHAT_FRIEND, "*", userId);
        redisUtils.delete(pattern);
    }

}
