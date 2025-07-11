package com.platform.modules.chat.service;

import cn.hutool.json.JSONObject;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatMsg;
import com.platform.modules.chat.vo.*;

import java.util.List;

/**
 * <p>
 * 聊天消息 服务层
 * </p>
 */
public interface ChatMsgService extends BaseService<ChatMsg> {

    /**
     * 发送消息
     */
    ChatVo00 sendSelfMsg(ChatVo01 chatVo);

    /**
     * 发送消息
     */
    ChatVo00 sendFriendMsg(ChatVo01 chatVo);

    /**
     * 挂断电话
     */
    String callKit(ChatVo06 chatVo);

    /**
     * 发送消息
     */
    ChatVo00 sendGroupMsg(ChatVo02 chatVo);

    /**
     * 发送消息
     */
    ChatVo00 sendRobotMsg(ChatVo03 chatVo);

    /**
     * 获取消息ID
     */
    ChatVo05 getMsgId();

    /**
     * 拉取消息
     */
    List<JSONObject> pullMsg();

    /**
     * 删除消息
     */
    void removeMsg(Long userId, List<String> dataList);

}
