package com.platform.modules.chat.service;

import com.platform.common.shiro.ShiroUserVo;

/**
 * <p>
 * 刷新 服务层
 * </p>
 */
public interface ChatRefreshService {

    /**
     * 刷新昵称
     */
    void refreshNickname(String nickname);

    /**
     * 刷新头像
     */
    void refreshPortrait(String portrait);

    /**
     * 刷新群名
     */
    void refreshGroupName(Long groupId, String groupName);

    /**
     * 刷新token
     */
    void refresh(ShiroUserVo userVo);

}
