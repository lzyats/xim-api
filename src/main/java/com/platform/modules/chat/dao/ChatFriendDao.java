package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.vo.FriendVo09;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 好友表 数据库访问层
 * </p>
 */
@Repository
public interface ChatFriendDao extends BaseDao<ChatFriend> {

    /**
     * 查询列表
     */
    List<ChatFriend> queryList(ChatFriend chatFriend);

    /**
     * 查询好友列表
     */
    List<FriendVo09> getFriendList(Long currentId);

}
