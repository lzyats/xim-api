package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatFriendInform;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 骚扰举报 数据库访问层
 * </p>
 */
@Repository
public interface ChatFriendInformDao extends BaseDao<ChatFriendInform> {

    /**
     * 查询列表
     */
    List<ChatFriendInform> queryList(ChatFriendInform inform);

}
