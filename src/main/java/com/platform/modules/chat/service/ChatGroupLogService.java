package com.platform.modules.chat.service;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupLog;
import com.platform.modules.chat.enums.GroupLogEnum;

/**
 * <p>
 * 群组日志 服务层
 * </p>
 */
public interface ChatGroupLogService extends BaseService<ChatGroupLog> {

    /**
     * 增加日志
     */
    void addLog(Long groupId, GroupLogEnum logType);

    /**
     * 增加日志
     */
    void addLog(Long groupId, GroupLogEnum logType, Long content);

    /**
     * 增加日志
     */
    void addLog(Long groupId, GroupLogEnum logType, String content);

    /**
     * 增加日志
     */
    void addLog(Long groupId, GroupLogEnum logType, YesOrNoEnum config);

}
