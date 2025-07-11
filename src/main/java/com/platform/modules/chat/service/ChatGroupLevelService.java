package com.platform.modules.chat.service;

import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.vo.GroupVo27;

import java.util.List;

/**
 * <p>
 * 群组级别 服务层
 * </p>
 */
public interface ChatGroupLevelService {

    /**
     * 价格列表
     */
    List<GroupVo27> queryPriceList(ChatGroup chatGroup);

    /**
     * 价格详情
     */
    GroupVo27 queryPriceInfo(Integer groupLevel, ChatGroup chatGroup);
}
