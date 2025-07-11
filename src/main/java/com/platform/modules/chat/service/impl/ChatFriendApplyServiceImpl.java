package com.platform.modules.chat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.ApproveEnum;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFriendApplyDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.domain.ChatFriendApply;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.FriendSourceEnum;
import com.platform.modules.chat.service.ChatConfigService;
import com.platform.modules.chat.service.ChatFriendApplyService;
import com.platform.modules.chat.service.ChatFriendService;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.chat.vo.FriendVo02;
import com.platform.modules.chat.vo.FriendVo10;
import com.platform.modules.chat.vo.FriendVo11;
import com.platform.modules.push.enums.PushBadgerEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 好友申请表 服务层实现
 * </p>
 */
@Service("chatFriendApplyService")
public class ChatFriendApplyServiceImpl extends BaseServiceImpl<ChatFriendApply> implements ChatFriendApplyService {

    @Resource
    private ChatFriendApplyDao chatFriendApplyDao;

    @Lazy
    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatFriendService chatFriendService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private PushService pushService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFriendApplyDao);
    }

    @Override
    public List<ChatFriendApply> queryList(ChatFriendApply t) {
        List<ChatFriendApply> dataList = chatFriendApplyDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public void applyFriend(FriendVo02 friendVo) {
        // 参数
        Long userId = friendVo.getUserId();
        FriendSourceEnum source = friendVo.getSource();
        Long current = ShiroUtils.getUserId();
        // 设置计数
        Long increment = getIncrement();
        Integer applyCount = chatConfigService.queryConfig(ChatConfigEnum.APPLY_FRIEND).getInt();
        if (increment > applyCount) {
            throw new BaseException("已超过好友申请上限，请明日再试");
        }
        // 验证是否是自己
        if (current.equals(userId)) {
            throw new BaseException("你不能添加自己为好友");
        }
        // 检查好友
        if (chatFriendService.getFriend(current, userId) != null) {
            return;
        }
        // 查询好友
        ChatUser chatUser = chatUserService.getById(userId);
        if (chatUser == null) {
            return;
        }
        // 错误
        String error = "暂无结果，对方可能开启了隐私保护";
        // 隐私
        switch (source) {
            case SCAN:
                if (!YesOrNoEnum.transform(chatUser.getPrivacyScan())) {
                    throw new BaseException(error);
                }
                break;
            case CARD:
                if (!YesOrNoEnum.transform(chatUser.getPrivacyCard())) {
                    throw new BaseException(error);
                }
                break;
            case GROUP:
                if (!YesOrNoEnum.transform(chatUser.getPrivacyGroup())) {
                    throw new BaseException(error);
                }
                break;
            case NO:
                if (!YesOrNoEnum.transform(chatUser.getPrivacyNo())) {
                    throw new BaseException(error);
                }
                break;
            case PHONE:
                if (!YesOrNoEnum.transform(chatUser.getPrivacyPhone())) {
                    throw new BaseException(error);
                }
                break;
        }
        // 去重
        ChatFriendApply result = this.queryOne(new ChatFriendApply(current, userId));
        if (result != null) {
            this.deleteById(result.getApplyId());
        }
        ChatFriendApply apply = new ChatFriendApply()
                .setUserId(current)
                .setNickname(ShiroUtils.getNickname())
                .setPortrait(ShiroUtils.getPortrait())
                .setUserNo(ShiroUtils.getUserNo())
                .setReason(friendVo.getReason())
                .setReceiveId(userId)
                .setReceiveRemark(friendVo.getRemark())
                .setStatus(ApproveEnum.APPLY)
                .setSource(friendVo.getSource())
                .setCreateTime(DateUtil.date());
        this.add(apply);
        // 发送通知
        pushService.pushBadger(userId, PushBadgerEnum.FRIEND, Arrays.asList(userId));
    }

    /**
     * 计数器
     */
    private Long getIncrement() {
        Date now = DateUtil.date();
        Long current = ShiroUtils.getUserId();
        String time = DateUtil.format(now, DatePattern.PURE_DATE_PATTERN);
        String redisKey = StrUtil.format(AppConstants.REDIS_APPLY_FRIEND, time, current);
        return redisUtils.increment(redisKey, 1, 1, TimeUnit.DAYS);
    }

    @Override
    public PageInfo queryDataList() {
        Long current = ShiroUtils.getUserId();
        // 清空角标
        redisUtils.delete(StrUtil.format(PushBadgerEnum.FRIEND.getType(), current));
        pushService.pushBadger(current, PushBadgerEnum.FRIEND, null);
        // 查询数据
        QueryWrapper<ChatFriendApply> wrapper = new QueryWrapper();
        wrapper.eq(ChatFriendApply.COLUMN_RECEIVE_ID, current);
        wrapper.gt(ChatFriendApply.COLUMN_CREATE_TIME, DateUtil.lastMonth());
        List<ChatFriendApply> dataList = super.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return new PageInfo();
        }
        // 转换数据
        List<FriendVo10> dictList = dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(BeanUtil.toBean(y, FriendVo10.class).setRemark(y.getSource().getInfo()));
        }, ArrayList::addAll);
        return getPageInfo(dictList, dataList);
    }

    @Transactional
    @Override
    public void agree(FriendVo11 friendVo) {
        // 参数
        Long applyId = friendVo.getApplyId();
        String remark = friendVo.getRemark();
        // 校验申请
        ChatFriendApply apply = verifyApply(applyId);
        if (apply == null) {
            return;
        }
        // 更新申请
        this.dealApply(applyId, ApproveEnum.PASS);
        Long current = ShiroUtils.getUserId();
        Long userId = apply.getUserId();
        // 查询好友
        ChatFriend friend1 = chatFriendService.getFriend(current, userId);
        ChatFriend friend2 = chatFriendService.getFriend(userId, current);
        if (friend1 != null && friend2 != null) {
            return;
        }
        Long groupId;
        if (friend1 != null) {
            groupId = friend1.getGroupId();
        } else if (friend2 != null) {
            groupId = friend2.getGroupId();
        } else {
            groupId = IdWorker.getId();
        }
        // 新增好友
        if (friend1 == null) {
            friend1 = new ChatFriend()
                    .setCurrentId(current)
                    .setGroupId(groupId)
                    .setUserId(userId)
                    .setNickname(apply.getNickname())
                    .setPortrait(apply.getPortrait())
                    .setUserNo(apply.getUserNo())
                    .setRemark(remark)
                    .setSource(apply.getSource())
                    .setBlack(YesOrNoEnum.NO)
                    .setDisturb(YesOrNoEnum.NO)
                    .setTop(YesOrNoEnum.NO)
                    .setCreateTime(DateUtil.date());
            chatFriendService.add(friend1);
        } else {
            friend1.setNickname(apply.getNickname())
                    .setPortrait(apply.getPortrait())
                    .setUserNo(apply.getUserNo())
                    .setRemark(remark)
                    .setSource(apply.getSource())
                    .setBlack(YesOrNoEnum.NO)
                    .setDisturb(YesOrNoEnum.NO)
                    .setTop(YesOrNoEnum.NO);
            chatFriendService.updateById(friend1);
        }
        // 新增好友
        if (friend2 == null) {
            friend2 = new ChatFriend()
                    .setCurrentId(userId)
                    .setGroupId(groupId)
                    .setUserId(current)
                    .setNickname(ShiroUtils.getNickname())
                    .setPortrait(ShiroUtils.getPortrait())
                    .setRemark(apply.getReceiveRemark())
                    .setUserNo(ShiroUtils.getUserNo())
                    .setSource(apply.getSource())
                    .setBlack(YesOrNoEnum.NO)
                    .setDisturb(YesOrNoEnum.NO)
                    .setTop(YesOrNoEnum.NO)
                    .setCreateTime(DateUtil.date());
            chatFriendService.add(friend2);
        } else {
            friend2.setNickname(ShiroUtils.getNickname())
                    .setPortrait(ShiroUtils.getPortrait())
                    .setRemark(apply.getReceiveRemark())
                    .setUserNo(ShiroUtils.getUserNo())
                    .setSource(apply.getSource())
                    .setBlack(YesOrNoEnum.NO)
                    .setDisturb(YesOrNoEnum.NO)
                    .setTop(YesOrNoEnum.NO);
            chatFriendService.updateById(friend2);
        }
        // 内容
        String content = AppConstants.TIPS_FRIEND_NEW;
        // 发送通知1
        pushService.pushSingle(friend1.getPushFrom(IdWorker.getId()), Arrays.asList(current), content, PushMsgTypeEnum.TIPS);
        // 发送通知2
        pushService.pushSingle(friend2.getPushFrom(IdWorker.getId()), Arrays.asList(userId), content, PushMsgTypeEnum.TIPS);
        // 通知推送
        chatFriendService.pushSetting(current, userId, ChatFriend.LABEL_CREATE, "");
        // 通知推送
        chatFriendService.pushSetting(userId, current, ChatFriend.LABEL_CREATE, "");
    }

    /**
     * 验证申请
     */
    private ChatFriendApply verifyApply(Long applyId) {
        ChatFriendApply apply = getById(applyId);
        if (apply == null) {
            return null;
        }
        if (!ApproveEnum.APPLY.equals(apply.getStatus())) {
            return null;
        }
        Long receiveId = apply.getReceiveId();
        // 好友判断
        if (!ShiroUtils.getUserId().equals(receiveId)) {
            return null;
        }
        return apply;
    }

    @Override
    public void reject(Long applyId) {
        // 校验申请
        ChatFriendApply apply = verifyApply(applyId);
        if (apply == null) {
            return;
        }
        // 更新申请
        this.dealApply(applyId, ApproveEnum.REJECT);
    }

    @Override
    public void applyDelete(Long applyId) {
        // 校验申请
        ChatFriendApply apply = this.getById(applyId);
        if (apply == null) {
            return;
        }
        Long current = ShiroUtils.getUserId();
        if (!apply.getReceiveId().equals(current)) {
            return;
        }
        // 更新申请
        this.deleteById(applyId);
    }

    @Override
    public void editNickname(String nickname) {
        Long current = ShiroUtils.getUserId();
        this.update(Wrappers.<ChatFriendApply>lambdaUpdate()
                .set(ChatFriendApply::getNickname, nickname)
                .eq(ChatFriendApply::getUserId, current));
    }

    @Override
    public void editPortrait(String portrait) {
        Long current = ShiroUtils.getUserId();
        this.update(Wrappers.<ChatFriendApply>lambdaUpdate()
                .set(ChatFriendApply::getPortrait, portrait)
                .eq(ChatFriendApply::getUserId, current));
    }


    /**
     * 处理申请
     */
    private void dealApply(Long applyId, ApproveEnum status) {
        updateById(new ChatFriendApply().setApplyId(applyId).setStatus(status));
    }

}
