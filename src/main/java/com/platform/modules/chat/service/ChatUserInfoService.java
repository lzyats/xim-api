package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatUserInfo;
import com.platform.modules.chat.vo.MineVo09;
import com.platform.modules.chat.vo.MineVo16;
import com.platform.modules.chat.vo.MineVo18;

/**
 * <p>
 * 用户详情 服务层
 * </p>
 */
public interface ChatUserInfoService extends BaseService<ChatUserInfo> {

    /**
     * 新增用户
     */
    void addInfo(Long userId);

    /**
     * 封禁状态
     */
    MineVo16 getBannedInfo();

    /**
     * 认证状态
     */
    MineVo18 getAuthInfo();

    /**
     * 提交认证
     */
    void editAuth(MineVo09 mineVo);

}
