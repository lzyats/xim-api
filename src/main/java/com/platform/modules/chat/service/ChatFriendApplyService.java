package com.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriendApply;
import com.platform.modules.chat.vo.FriendVo02;
import com.platform.modules.chat.vo.FriendVo11;

/**
 * <p>
 * 好友申请表 服务层
 * </p>
 */
public interface ChatFriendApplyService extends BaseService<ChatFriendApply> {

    /**
     * 申请好友
     */
    void applyFriend(FriendVo02 friendVo);

    /**
     * 申请记录
     */
    PageInfo queryDataList();

    /**
     * 同意申请
     */
    void agree(FriendVo11 friendVo);

    /**
     * 拒绝申请
     */
    void reject(Long applyId);

    /**
     * 删除申请
     */
    void applyDelete(Long applyId);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

}
