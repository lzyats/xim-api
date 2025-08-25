package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatPortrait;
import com.platform.modules.chat.enums.ChatTalkEnum;

import java.util.List;

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

    /**
     * 查询用户头像列表
     * @return
     */
    List<String> queryPortraitList(ChatTalkEnum chatType);

}
