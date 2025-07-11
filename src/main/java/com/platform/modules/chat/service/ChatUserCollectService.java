package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserCollect;
import com.platform.modules.chat.vo.CollectVo01;

/**
 * <p>
 * 收藏表 服务层
 * </p>
 */
public interface ChatUserCollectService extends BaseService<ChatUserCollect> {

    /**
     * 新增收藏
     */
    void addCollect(CollectVo01 collectVo);

    /**
     * 列表
     */
    PageInfo queryDataList(ChatUserCollect collect);

}
