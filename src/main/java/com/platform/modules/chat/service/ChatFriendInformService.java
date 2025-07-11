package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriendInform;
import com.platform.modules.chat.vo.FriendVo07;

/**
 * <p>
 * 骚扰举报 服务层
 * </p>
 */
public interface ChatFriendInformService extends BaseService<ChatFriendInform> {

    /**
     * 举报
     */
    void inform(FriendVo07 friendVo);
}
