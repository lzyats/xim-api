package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupSolitaire;
import com.platform.modules.chat.vo.*;

/**
 * <p>
 * 成语接龙 服务层
 * </p>
 */
public interface ChatGroupSolitaireService extends BaseService<ChatGroupSolitaire> {

    /**
     * 创建接龙
     */
    GroupVo41 createSolitaire(GroupVo29 groupVo);

    /**
     * 查询接龙
     */
    GroupVo43 querySolitaire(GroupVo31 groupVo);

    /**
     * 提交接龙
     */
    GroupVo42 submitSolitaire(GroupVo32 groupVo);

}
