package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserAppeal;
import com.platform.modules.chat.vo.MineVo17;

/**
 * <p>
 * 用户申诉 服务层
 * </p>
 */
public interface ChatUserAppealService extends BaseService<ChatUserAppeal> {

    /**
     * 申请解封
     */
    void appealBanned(MineVo17 mineVo);

}
