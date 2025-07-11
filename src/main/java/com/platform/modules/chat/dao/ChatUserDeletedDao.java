package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatUserDeleted;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 注销表 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserDeletedDao extends BaseDao<ChatUserDeleted> {

    /**
     * 查询列表
     */
    List<ChatUserDeleted> queryList(ChatUserDeleted chatUserDeleted);

}
