package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.modules.chat.domain.ChatUserSign;
import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.enums.TradeTypeEnum;

import java.util.Map;

/**
 * <p>
 * 用户按天签到记录 服务层
 * </p>
 */
public interface ChatUserSignService extends BaseService<ChatUserSign> {
    /**
     * 账单列表
     */
    PageInfo getSignList();

    /**
     * 获取指定用户基本签到信息
     * @return
     */
    Map<String, Object> getSignStats();
    /**
     * 签到
     * @return
     */
    Map<String, Object> sign();
}
