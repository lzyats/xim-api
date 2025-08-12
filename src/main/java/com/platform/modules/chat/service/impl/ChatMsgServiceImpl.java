package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.chat.dao.ChatMsgDao;
import com.platform.modules.chat.domain.*;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.enums.ChatStatusEnum;
import com.platform.modules.chat.enums.ChatTalkEnum;
import com.platform.modules.chat.enums.GroupMemberEnum;
import com.platform.modules.chat.rtc.RtcBuilder;
import com.platform.modules.chat.rtc.RtcStatus;
import com.platform.modules.chat.service.*;
import com.platform.modules.chat.vo.*;
import com.platform.modules.common.service.FileService;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushGroup;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.dto.PushSync;
import com.platform.modules.push.enums.PushBadgerEnum;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.wallet.service.WalletTradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 聊天消息 服务层实现
 * </p>
 */
@Slf4j
@Service("chatMsgService")
public class ChatMsgServiceImpl extends BaseServiceImpl<ChatMsg> implements ChatMsgService {

    @Resource
    private ChatMsgDao chatMsgDao;

    @Resource
    private ChatFriendService friendService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatGroupMemberService chatGroupMemberService;

    @Resource
    private PushService pushService;

    @Resource
    private ChatRobotReplyService chatRobotReplyService;

    @Resource
    private FileService fileService;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private WalletTradeService walletTradeService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private TokenService tokenService;

    @Resource
    private RtcBuilder rtcBuilder;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatMsgDao);
    }

    @Override
    public List<ChatMsg> queryList(ChatMsg t) {
        List<ChatMsg> dataList = chatMsgDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public ChatVo00 sendSelfMsg(ChatVo01 chatVo) {
        Long msgId = chatVo.getMsgId();
        Long syncId = chatVo.getSyncId();
        PushMsgTypeEnum msgType = chatVo.getMsgType();
        JSONObject jsonObject = chatVo.getContent();
        Long current = ShiroUtils.getUserId();
        // 校验
        if (!verify(msgId, msgType, jsonObject)) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.ERROR);
        }
        // 撤回消息
        if (PushMsgTypeEnum.RECALL.equals(msgType)) {
            this.recallSelfMsg(jsonObject);
        }
        // 消息内容
        String content = JSONUtil.toJsonStr(jsonObject);
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setUserId(current)
                .setReceiveId(current)
                .setGroupId(current)
                .setMsgType(msgType)
                .setTalkType(ChatTalkEnum.FRIEND)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        PushFrom pushFrom = ShiroUtils.getPushFrom(msgId, syncId);
        PushSync pushSync = ShiroUtils.getPushSync();
        pushService.pushSync(pushFrom, pushSync, content, msgType);
        return new ChatVo00(msgId, syncId);
    }

    /**
     * 消息回撤
     */
    private void recallSelfMsg(JSONObject jsonObject) {
        // 查询撤回消息
        ChatMsg chatMsg = this.queryRecallMsg(jsonObject, true);
        if (chatMsg == null) {
            return;
        }
        // 执行撤回
        pushService.recallMsg(Arrays.asList(NumberUtil.toStr(chatMsg.getMsgId()), NumberUtil.toStr(chatMsg.getSyncId())));
        // 推送
        String tips = StrUtil.format("[你]撤回了一条消息");
        PushFrom pushFrom = ShiroUtils.getPushFrom(IdWorker.getId(), IdWorker.getId()).setSign("");
        PushSync pushSync = ShiroUtils.getPushSync();
        pushService.pushSync(pushFrom, pushSync, tips, PushMsgTypeEnum.TIPS);
    }

    @Transactional
    @Override
    public ChatVo00 sendFriendMsg(ChatVo01 chatVo) {
        Long msgId = chatVo.getMsgId();
        Long syncId = chatVo.getSyncId();
        Long userId = chatVo.getUserId();
        PushMsgTypeEnum msgType = chatVo.getMsgType();
        JSONObject jsonObject = chatVo.getContent();
        Long current = ShiroUtils.getUserId();
        // 校验
        if (!verify(msgId, msgType, jsonObject)) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.ERROR);
        }
        // 校验好友
        ChatFriend friend1 = friendService.getFriend(current, userId);
        if (friend1 == null) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.FRIEND_NONE);
        }
        // 校验好友
        ChatFriend friend2 = friendService.getFriend(userId, current);
        if (friend2 == null) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.FRIEND_NONE);
        }
        if (YesOrNoEnum.YES.equals(friend2.getBlack())) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.FRIEND_BLACK);
        }
        // 转账消息
        if (PushMsgTypeEnum.TRANSFER.equals(msgType)) {
            walletTradeService.sendTransfer(msgId, userId, jsonObject);
        }
        // 红包消息
        if (PushMsgTypeEnum.PACKET.equals(msgType)) {
            walletTradeService.sendPacket(msgId, userId, jsonObject);
        }
        // 撤回消息
        if (PushMsgTypeEnum.RECALL.equals(msgType)) {
            this.recallFriendMsg(jsonObject, friend1, friend2);
        }
        // 语音视频
        String token = null;
        if (PushMsgTypeEnum.CALL.equals(msgType)) {
            token = rtcBuilder.buildToken(msgId, ShiroUtils.getUserNo());
        }
        // 消息内容
        String content = JSONUtil.toJsonStr(jsonObject);
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setMsgId(msgId)
                .setUserId(current)
                .setSyncId(syncId)
                .setReceiveId(userId)
                .setGroupId(friend2.getGroupId())
                .setMsgType(msgType)
                .setTalkType(ChatTalkEnum.FRIEND)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        PushFrom pushFrom = friend2.getPushFrom(msgId)
                .setSyncId(syncId)
                .setSign(ShiroUtils.getSign());
        // 发送消息
        pushService.pushSingle(pushFrom, userId, content, msgType);
        // 同步消息
        pushService.pushSync(pushFrom, friend1.getPushSync(), content, msgType);
        // 返回消息
        return new ChatVo00(msgId, syncId).setToken(token);
    }

    @Override
    public String callKit(ChatVo06 chatVo) {
        Long msgId = chatVo.getMsgId();
        RtcStatus status = chatVo.getStatus();
        String second = chatVo.getSecond();
        // 等待
        if (RtcStatus.AWAIT.equals(status)) {
            return null;
        }
        // 查询
        ChatMsg chatMsg = this.getById(msgId);
        // 校验
        if (chatMsg == null) {
            return null;
        }
        // 校验
        Long current = ShiroUtils.getUserId();
        Long userId = chatMsg.getUserId();
        Long receiveId = chatMsg.getReceiveId();
        if (!(current.equals(userId) || current.equals(receiveId))) {
            return null;
        }
        // 更新数据
        JSONObject jsonObject = JSONUtil.parseObj(chatMsg.getContent());
        jsonObject.set("callStatus", status.value);
        jsonObject.set("callTime", second);
        String content = JSONUtil.toJsonStr(jsonObject);
        this.updateById(new ChatMsg(msgId).setContent(content));
        // 更新消息
        pushService.editMsg(chatMsg.getMsgId(), chatMsg.getSyncId(), jsonObject);
        // 返回
        String token = null;
        if (RtcStatus.CONNECT.equals(status)) {
            token = rtcBuilder.buildToken(msgId, ShiroUtils.getUserNo());
        }
        // 下发消息
        PushSetting setting = new PushSetting(PushSettingEnum.SYS, msgId, "call", content);
        pushService.pushSetting(setting, Arrays.asList(userId, receiveId));
        return token;
    }

    /**
     * 消息回撤
     */
    private void recallFriendMsg(JSONObject jsonObject, ChatFriend friend1, ChatFriend friend2) {
        // 查询撤回消息
        ChatMsg chatMsg = this.queryRecallMsg(jsonObject, true);
        if (chatMsg == null) {
            return;
        }
        // 执行撤回
        pushService.recallMsg(Arrays.asList(NumberUtil.toStr(chatMsg.getMsgId()), NumberUtil.toStr(chatMsg.getSyncId())));
        // 推送
        PushFrom pushFrom = friend2.getPushFrom(IdWorker.getId()).setSyncId(IdWorker.getId());
        // 正常消息
        pushService.pushSingle(pushFrom, chatMsg.getReceiveId(), StrUtil.format("[{}]撤回了一条消息", friend2.getDefaultRemark()), PushMsgTypeEnum.TIPS);
        // 同步消息
        pushService.pushSync(pushFrom, friend1.getPushSync(), StrUtil.format("[你]撤回了一条消息"), PushMsgTypeEnum.TIPS);
    }

    @Transactional
    @Override
    public ChatVo00 sendGroupMsg(ChatVo02 chatVo) {
        Long msgId = chatVo.getMsgId();
        Long syncId = chatVo.getSyncId();
        Long groupId = chatVo.getGroupId();
        Long current = ShiroUtils.getUserId();
        PushMsgTypeEnum msgType = chatVo.getMsgType();
        JSONObject jsonObject = chatVo.getContent();
        // 校验
        if (!verify(msgId, msgType, jsonObject)) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.ERROR);
        }
        // 查询群组
        ChatGroup chatGroup = chatGroupService.getById(groupId);
        if (chatGroup == null) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_NONE);
        }
        // 群组封禁
        if (YesOrNoEnum.YES.equals(chatGroup.getBanned())) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_BANNED);
        }
        // 验证成员
        ChatGroupMember member = chatGroupMemberService.queryGroupMember(groupId, current);
        if (member == null) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_MEMBER_NONE);
        }
        // 全员禁言
        if (YesOrNoEnum.YES.equals(chatGroup.getConfigSpeak())) {
            if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_SPEAK);
            }
        }
        // 成员禁言
        if (YesOrNoEnum.YES.equals(member.getSpeak())) {
            if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_MEMBER_SPEAK);
            }
        }
        // 禁止多媒体
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigMedia())) {
            if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                switch (msgType) {
                    case IMAGE:
                    case VIDEO:
                    case FILE:
                        return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_MEDIA);
                    default:
                        break;
                }
            }
        }
        // 禁止二维码
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigScan()) && PushMsgTypeEnum.IMAGE.equals(msgType)) {
            // 成员
            if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                if (jsonObject.containsKey("scan")) {
                    return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_SCAN);
                }
            }
        }
        // 禁止红包
        if (YesOrNoEnum.NO.equals(chatGroup.getConfigPacket())) {
            if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
                switch (msgType) {
                    case GROUP_ASSIGN:
                    case GROUP_LUCK:
                    case GROUP_PACKET:
                        return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_PACKET);
                    default:
                        break;
                }
            }
        }
        // 查询成员
        List<Long> memberList = chatGroupMemberService.getMemberList(groupId);
        // 撤回消息
        if (PushMsgTypeEnum.RECALL.equals(msgType)) {
            String tips = this.recallGroupMsg(jsonObject, member);
            if (!StringUtils.isEmpty(tips)) {
                // 正常消息
                PushFrom pushFrom = member.getPushFrom(IdWorker.getId(), IdWorker.getId())
                        .setSign("");
                PushGroup pushGroup = chatGroup.getPushGroup();
                pushService.pushGroup(pushFrom, pushGroup, memberList, tips, PushMsgTypeEnum.TIPS);
                // 同步消息
                PushSync pushSync = chatGroup.getPushSync();
                pushService.pushSync(pushFrom, pushSync, tips, PushMsgTypeEnum.TIPS);
            }
        }
        // 专属红包
        if (PushMsgTypeEnum.GROUP_ASSIGN.equals(msgType)) {
            Long receiveId = jsonObject.getLong("receiveId", 0L);
            if (chatGroupMemberService.queryGroupMember(groupId, receiveId) == null) {
                return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_FRIEND);
            }
            // 专属可见
            if (YesOrNoEnum.YES.equals(chatGroup.getConfigAssign())) {
                memberList = Arrays.asList(receiveId);
            }
            walletTradeService.sendPacketAssign(msgId, chatGroup, jsonObject);
        }
        // 普通红包
        if (PushMsgTypeEnum.GROUP_PACKET.equals(msgType)) {
            walletTradeService.sendPacketGroup(msgId, chatGroup, jsonObject, YesOrNoEnum.YES);
        }
        // 手气红包
        if (PushMsgTypeEnum.GROUP_LUCK.equals(msgType)) {
            walletTradeService.sendPacketGroup(msgId, chatGroup, jsonObject, YesOrNoEnum.NO);
        }
        // 群组转账
        if (PushMsgTypeEnum.GROUP_TRANSFER.equals(msgType)) {
            Long receiveId = jsonObject.getLong("receiveId", 0L);
            if (chatGroupMemberService.queryGroupMember(groupId, receiveId) == null) {
                return new ChatVo00(msgId, syncId, ChatStatusEnum.GROUP_FRIEND);
            }
            memberList = Arrays.asList(receiveId);
            walletTradeService.sendGroupTransfer(msgId, chatGroup, jsonObject);
        }
        // 消息内容
        String content = JSONUtil.toJsonStr(jsonObject);
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setUserId(current)
                .setReceiveId(groupId)
                .setGroupId(groupId)
                .setMsgType(msgType)
                .setTalkType(ChatTalkEnum.GROUP)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        // 消息推送
        PushFrom paramFrom = member.getPushFrom(msgId, syncId);
        PushGroup pushGroup = chatGroup.getPushGroup();
        pushService.pushGroup(paramFrom, pushGroup, memberList, content, chatVo.getMsgType());
        // 同步消息
        PushSync pushSync = chatGroup.getPushSync();
        pushService.pushSync(paramFrom, pushSync, content, msgType);
        // 返回结果
        return new ChatVo00(msgId, syncId);
    }

    /**
     * 消息回撤
     */
    private String recallGroupMsg(JSONObject jsonObject, ChatGroupMember member) {
        ChatMsg chatMsg = this.queryRecallMsg(jsonObject, GroupMemberEnum.NORMAL.equals(member.getMemberType()));
        if (chatMsg == null) {
            return null;
        }
        String tips;
        // 普通成员
        if (GroupMemberEnum.NORMAL.equals(member.getMemberType())) {
            tips = StrUtil.format("[{}]撤回了一条消息", member.getDefaultRemark());
        }
        // 管理员自己
        else if (member.getUserId().equals(chatMsg.getUserId())) {
            tips = StrUtil.format("[{}]撤回了一条消息", member.getDefaultRemark());
        }
        // 撤回成员
        else {
            String nickname = "成员";
            // 验证成员
            ChatGroupMember groupMember = chatGroupMemberService.queryGroupMember(member.getGroupId(), chatMsg.getUserId());
            if (groupMember != null) {
                nickname = groupMember.getDefaultRemark();
            }
            tips = StrUtil.format("[{}]撤回了[{}]的消息", member.getMemberType().getInfo(), nickname);
        }
        // 执行撤回
        pushService.recallMsg(Arrays.asList(NumberUtil.toStr(chatMsg.getMsgId()), NumberUtil.toStr(chatMsg.getSyncId())));
        return tips;
    }

    @Override
    public ChatVo00 sendRobotMsg(ChatVo03 chatVo) {
        Long msgId = chatVo.getMsgId();
        Long syncId = chatVo.getSyncId();
        Long robotId = chatVo.getRobotId();
        Long current = ShiroUtils.getUserId();
        PushMsgTypeEnum msgType = chatVo.getMsgType();
        JSONObject jsonObject = chatVo.getContent();
        // 校验
        if (!verify(msgId, msgType, jsonObject)) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.ERROR);
        }
        // 检查服务号
        ChatRobot robot = chatRobotService.getById(robotId);
        if (robot == null) {
            return new ChatVo00(msgId, syncId, ChatStatusEnum.ROBOT);
        }
        // 撤回消息
        if (PushMsgTypeEnum.RECALL.equals(msgType)) {
            this.recallRobotMsg(jsonObject, robot);
        }
        // 事件消息
        if (PushMsgTypeEnum.EVEN.equals(msgType)) {
            chatRobotReplyService.even(robotId, current, jsonObject);
        }
        // 消息内容
        String content = JSONUtil.toJsonStr(jsonObject);
        // 保存数据
        ChatMsg chatMsg = new ChatMsg()
                .setMsgId(msgId)
                .setSyncId(syncId)
                .setUserId(current)
                .setReceiveId(robotId)
                .setGroupId(robotId)
                .setMsgType(msgType)
                .setTalkType(ChatTalkEnum.ROBOT)
                .setContent(content)
                .setCreateTime(DateUtil.date());
        this.add(chatMsg);
        // 在线客服
        if (AppConstants.ROBOT_ID.equals(robotId)) {
            PushFrom pushFrom = ShiroUtils.getPushFrom(msgId, syncId)
                    .setGroupId(robotId, current);
            PushSync pushSync = robot.getPushSync();
            pushService.pushSingle(pushFrom, robotId, content, msgType);
            pushService.pushSync(pushFrom, pushSync, content, msgType);
        }
        // 返回结果
        return new ChatVo00(msgId, syncId);
    }

    @Override
    public ChatVo05 getMsgId() {
        return new ChatVo05();
    }

    /**
     * 校验
     */
    private boolean verify(Long msgId, PushMsgTypeEnum msgType, JSONObject jsonObject) {
        // 比大小
        if (msgId > IdWorker.getId()) {
            return false;
        }
        // 声音消息
        if (PushMsgTypeEnum.VOICE.equals(msgType)) {
            fileService.uploadVoice(msgId, jsonObject.getStr("data"));
        }
        return true;
    }

    /**
     * 消息回撤
     */
    private void recallRobotMsg(JSONObject jsonObject, ChatRobot robot) {
        ChatMsg chatMsg = this.queryRecallMsg(jsonObject, true);
        if (chatMsg == null) {
            return;
        }
        String tips = StrUtil.format("[{}]撤回了一条消息", ShiroUtils.getNickname());
        // 执行撤回
        pushService.recallMsg(Arrays.asList(NumberUtil.toStr(chatMsg.getMsgId()), NumberUtil.toStr(chatMsg.getSyncId())));
        // 推送
        PushFrom pushFrom = ShiroUtils.getPushFrom(IdWorker.getId(), IdWorker.getId())
                .setGroupId(robot.getRobotId(), ShiroUtils.getUserId())
                .setSign("");
        PushSync pushSync = robot.getPushSync();
        // 正常消息
        pushService.pushSingle(pushFrom, robot.getRobotId(), tips, PushMsgTypeEnum.TIPS);
        // 同步消息
        pushService.pushSync(pushFrom, pushSync, "[你]撤回了一条消息", PushMsgTypeEnum.TIPS);
    }

    /**
     * 查询撤回消息
     */
    ChatMsg queryRecallMsg(JSONObject jsonObject, boolean verify) {
        Long msgId = jsonObject.getLong("data");
        ChatMsg chatMsg = this.getById(msgId);
        if (chatMsg == null) {
            return null;
        }
        // 验证
        if (verify) {
            if (!ShiroUtils.getUserId().equals(chatMsg.getUserId())) {
                throw new BaseException("只能撤回自己的消息");
            }
            // 撤回时间
            Integer recallTime = chatConfigService.queryConfig(ChatConfigEnum.SYS_RECALL).getInt();
            // 只能撤回固定时间内的消息
            Long between = DateUtil.between(DateUtil.date(), chatMsg.getCreateTime(), DateUnit.MINUTE);
            if (between > recallTime) {
                throw new BaseException(StrUtil.format("只能撤回{}分钟内的消息", recallTime));
            }
        }
        // 只能撤回固定类型的消息
        switch (chatMsg.getMsgType()) {
            case TEXT:
            case IMAGE:
            case VOICE:
            case VIDEO:
            case FILE:
            case LOCATION:
            case CARD:
            case REPLY:
            case FORWARD:
            case AT:
//            case SNAP:
//            case SOLITAIRE:
                break;
            default:
                throw new BaseException("消息类型不支持，撤回失败");
        }
        return chatMsg;
    }

    @Override
    public List<JSONObject> pullMsg() {
        Long current = ShiroUtils.getUserId();
        String lastId = ShiroUtils.getLastId();
        log.info(lastId);
        String token = ShiroUtils.getToken();
        // 下发红点
        this.pushBadger(current);
        // 拉取消息
        List<JSONObject> dataList = pushService.pullMsg(current, lastId, AppConstants.MESSAGE_LIMIT);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        // 刷新msgId
        JSONObject data = dataList.get(dataList.size() - 1);
        String msgId = data.getJSONObject("pushData").getStr("msgId");
        log.info("setLastMomentId：{}",msgId);
        ShiroUserVo userVo = new ShiroUserVo().setLastId(msgId);
        tokenService.refresh(Arrays.asList(token), userVo);
        return dataList;
    }

    /**
     * 下发红点
     */
    private void pushBadger(Long userId) {
        pushService.pushBadger(userId, PushBadgerEnum.FRIEND, null);
        pushService.pushBadger(userId, PushBadgerEnum.GROUP, null);
    }

    @Override
    public void removeMsg(Long userId, List<String> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        pushService.removeMsg(userId, dataList);
    }

}
