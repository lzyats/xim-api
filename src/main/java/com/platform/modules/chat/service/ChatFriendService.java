package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.vo.*;

import java.util.List;

/**
 * <p>
 * 好友表 服务层
 * </p>
 */
public interface ChatFriendService extends BaseService<ChatFriend> {

    /**
     * 设置黑名单
     */
    void setBlack(FriendVo03 friendVo);

    /**
     * 删除好友
     */
    void delFriend(Long userId);

    /**
     * 设置备注
     */
    void setRemark(FriendVo05 friendVo);

    /**
     * 设置置顶
     */
    void setTop(FriendVo06 friendVo);

    /**
     * 设置静默
     */
    void setDisturb(FriendVo08 friendVo);

    /**
     * 好友列表
     */
    List<FriendVo09> getFriendList();

    /**
     * 好友详情
     */
    FriendVo09 getInfo(Long userId);

    /**
     * 查询好友
     */
    ChatFriend getFriend(Long current, Long userId);

    /**
     * 校验好友
     */
    List<Long> verifyFriend(List<Long> friendList);

    /**
     * 搜索好友
     */
    FriendVo01 searchFriend(String param);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 通知推送
     */
    void pushSetting(Long userId, Long object, String label, String value);

}
