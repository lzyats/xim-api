package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.common.web.vo.LabelVo;
import com.platform.modules.chat.domain.ChatHelp;

import java.util.List;

/**
 * <p>
 * 聊天帮助 服务层
 * </p>
 */
public interface ChatHelpService extends BaseService<ChatHelp> {

    /**
     * 查询列表
     */
    List<LabelVo> queryDataList();

}
