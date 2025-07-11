package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatVersion;
import com.platform.modules.common.vo.CommonVo05;

/**
 * <p>
 * 版本 服务层
 * </p>
 */
public interface ChatVersionService extends BaseService<ChatVersion> {

    /**
     * 获取版本
     */
    CommonVo05 upgrade();

}
