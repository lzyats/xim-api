package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserDeleted;

/**
 * <p>
 * 注销表 服务层
 * </p>
 */
public interface ChatUserDeletedService extends BaseService<ChatUserDeleted> {

    /**
     * 通过账号查询
     */
    void register(String phone);

    /**
     * 注销用户
     */
    void deleted(Long userId, String phone);

}
