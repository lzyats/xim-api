package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatUserLog;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 用户日志 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserLogDao extends BaseDao<ChatUserLog> {

    /**
     * 查询列表
     */
    List<ChatUserLog> queryList(ChatUserLog chatUserLog);

}
