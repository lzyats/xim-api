package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatFriendApply;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 好友申请表 数据库访问层
 * </p>
 */
@Repository
public interface ChatFriendApplyDao extends BaseDao<ChatFriendApply> {

    /**
     * 查询列表
     */
    List<ChatFriendApply> queryList(ChatFriendApply apply);


}
