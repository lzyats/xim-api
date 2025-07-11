package com.platform.modules.chat.service;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserToken;

import java.util.List;

/**
 * <p>
 * 用户token 服务层
 * </p>
 */
public interface ChatUserTokenService extends BaseService<ChatUserToken> {

    /**
     * 更新token
     */
    void resetToken(Long userId, String token, YesOrNoEnum special);

    /**
     * 查询token
     */
    List<String> queryTokenList(Long userId);

    /**
     * 退出
     */
    void logout(String token);

    /**
     * 注销
     */
    void deleted(Long userId);
}
