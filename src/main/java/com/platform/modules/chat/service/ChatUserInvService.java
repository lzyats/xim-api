package com.platform.modules.chat.service;

import com.platform.modules.chat.domain.ChatUserInv;
import com.platform.common.web.service.BaseService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 会员注册邀请表 服务层
 * </p>
 */
public interface ChatUserInvService extends BaseService<ChatUserInv> {

    /**
     * 执行邀请
     */
    void invode(Long userId, Long userInid, double invUsdt);


}
