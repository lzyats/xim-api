package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserLog;
import com.platform.modules.chat.enums.UserLogEnum;

/**
 * <p>
 * 用户日志 服务层
 * </p>
 */
public interface ChatUserLogService extends BaseService<ChatUserLog> {

    /**
     * 增加日志
     */
    void addLog(Long userId, UserLogEnum logType);

    /**
     * 增加日志
     */
    void addLog(Long userId, UserLogEnum logType, Long content);

    /**
     * 增加日志
     */
    void addLog(Long userId, UserLogEnum logType, String content);

    /**
     * 增加日志
     */
    void addLog(Long userId);

}
