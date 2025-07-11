package com.platform.modules.chat.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.utils.timer.TimerUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatRobotReplyDao;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.domain.ChatRobotReply;
import com.platform.modules.chat.enums.ChatReplyEnum;
import com.platform.modules.chat.service.ChatRobotReplyService;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务号 服务层实现
 * </p>
 */
@Service("chatRobotReplyService")
public class ChatRobotReplyServiceImpl extends BaseServiceImpl<ChatRobotReply> implements ChatRobotReplyService {

    @Resource
    private ChatRobotReplyDao chatRobotReplyDao;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private PushService pushService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatRobotReplyDao);
    }

    @Override
    public List<ChatRobotReply> queryList(ChatRobotReply t) {
        List<ChatRobotReply> dataList = chatRobotReplyDao.queryList(t);
        return dataList;
    }

    @Override
    public void subscribe(Long userId) {
        // 查询服务号
        List<ChatRobot> robotList = chatRobotService.queryList(new ChatRobot());
        if (CollectionUtils.isEmpty(robotList)) {
            return;
        }
        // 异步执行
        TimerUtils.instance().addTask((timeout) -> {
            // 异步执行方法
            robotList.forEach(robot -> {
                // 查询消息
                List<String> dataList = this.queryByType(robot.getRobotId(), ChatReplyEnum.SUBSCRIBE);
                // 发送消息
                this.sendMsg(robot, userId, dataList);
            });
        }, 500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void reply(Long robotId, Long userId, JSONObject jsonObject) {
        this.doReply(robotId, userId, ChatReplyEnum.REPLY, jsonObject.getStr("data"));
    }

    @Override
    public void even(Long robotId, Long userId, JSONObject jsonObject) {
        this.doReply(robotId, userId, ChatReplyEnum.EVEN, jsonObject.getStr("data"));
    }

    private void doReply(Long robotId, Long userId, ChatReplyEnum replyType, String replyKey) {
        // 查询机器人
        ChatRobot robot = chatRobotService.getById(robotId);
        if (robot == null) {
            return;
        }
        // 查询消息
        List<String> dataList = this.queryReply(robot.getRobotId(), replyType, replyKey);
//        if (CollectionUtils.isEmpty(dataList)) {
//            dataList = this.queryByType(robot.getRobotId(), ChatReplyEnum.ERROR);
//        }
        // 异步执行
        TimerUtils.instance().addTask((timeout) -> {
            // 发送消息
            this.sendMsg(robot, userId, dataList);
        }, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据类型查询
     */
    private List<String> queryByType(Long robotId, ChatReplyEnum replyType) {
        ChatRobotReply query = new ChatRobotReply()
                .setRobotId(robotId)
                .setReplyType(replyType);
        List<ChatRobotReply> dataList = this.queryList(query);
        return dataList.stream().map(ChatRobotReply::getContent).collect(Collectors.toList());
    }

    /**
     * 查询回复
     */
    private List<String> queryReply(Long robotId, ChatReplyEnum replyType, String replyKey) {
        QueryWrapper<ChatRobotReply> wrapper = new QueryWrapper();
        wrapper.eq(ChatRobotReply.COLUMN_ROBOT_ID, robotId);
        wrapper.eq(ChatRobotReply.COLUMN_REPLY_TYPE, replyType);
        wrapper.like(ChatRobotReply.COLUMN_REPLY_KEY, "|" + replyKey + "|");
        ChatRobotReply reply = this.queryOne(wrapper);
        List<String> dataList = new ArrayList<>();
        if (reply != null) {
            dataList.add(reply.getContent());
        }
        return dataList;
    }

    /**
     * 发送消息
     */
    private void sendMsg(ChatRobot chatRobot, Long current, List<String> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        Long msgId = IdWorker.getId();
        PushFrom pushFrom = chatRobot.getPushFrom(msgId)
                .setGroupId(chatRobot.getRobotId(), current);
        // 执行方法
        dataList.forEach(content -> {
            JSONObject jsonObject = new JSONObject().set("data", content);
            pushService.pushSingle(pushFrom, current, JSONUtil.toJsonStr(jsonObject), PushMsgTypeEnum.TEXT);
        });
    }

}
