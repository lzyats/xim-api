package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatNotice;
import com.platform.modules.common.vo.CommonVo04;

import java.util.List;

/**
 * <p>
 * 通知公告 服务层
 * </p>
 */
public interface ChatNoticeService extends BaseService<ChatNotice> {

    /**
     * 列表
     */
    PageInfo queryDataList();

}
