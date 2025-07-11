package com.platform.modules.chat.service;

import cn.hutool.json.JSONObject;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatRobotReply;

/**
 * <p>
 * 服务号 服务层
 * </p>
 */
public interface ChatRobotReplyService extends BaseService<ChatRobotReply> {

    /**
     * 关注回复
     */
    void subscribe(Long userId);

    /**
     * 文字消息
     */
    void reply(Long robotId, Long userId, JSONObject jsonObject);

    /**
     * 事件消息
     */
    void even(Long robotId, Long userId, JSONObject jsonObject);

}
