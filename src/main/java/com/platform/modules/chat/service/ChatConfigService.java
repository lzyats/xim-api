package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;

import java.util.Map;

/**
 * <p>
 * 设置表 服务层
 * </p>
 */
public interface ChatConfigService extends BaseService<ChatConfig> {

    /**
     * 查询配置
     */
    Map<ChatConfigEnum, ChatConfig> queryConfig();

    /**
     * 查询配置
     */
    ChatConfig queryConfig(ChatConfigEnum configKey);

}
