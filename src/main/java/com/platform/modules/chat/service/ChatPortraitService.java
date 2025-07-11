package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatPortrait;

/**
 * <p>
 * 聊天头像 服务层
 * </p>
 */
public interface ChatPortraitService extends BaseService<ChatPortrait> {

    /**
     * 查询群组头像
     */
    String queryGroupPortrait();

    /**
     * 查询用户头像
     */
    String queryUserPortrait();

}
