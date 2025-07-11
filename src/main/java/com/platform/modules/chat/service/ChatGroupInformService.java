package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupInform;
import com.platform.modules.chat.vo.GroupVo40;

/**
 * <p>
 * 骚扰举报 服务层
 * </p>
 */
public interface ChatGroupInformService extends BaseService<ChatGroupInform> {

    /**
     * 举报
     */
    void inform(GroupVo40 groupVo);
}
