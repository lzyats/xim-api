package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatNumber;

/**
 * <p>
 * 微聊号码 服务层
 * </p>
 */
public interface ChatNumberService extends BaseService<ChatNumber> {

    /**
     * 查询微聊号
     */
    String queryNextNo();

}
