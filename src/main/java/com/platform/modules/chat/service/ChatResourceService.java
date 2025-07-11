package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatResource;

/**
 * <p>
 * 聊天资源 服务层
 * </p>
 */
public interface ChatResourceService extends BaseService<ChatResource> {

    /**
     * 新增资源
     */
    void addResource(String url);

    /**
     * 删除资源
     */
    void delResource(String url);
}
