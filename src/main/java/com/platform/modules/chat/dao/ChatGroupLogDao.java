package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupLog;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 群组日志 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupLogDao extends BaseDao<ChatGroupLog> {

    /**
     * 查询列表
     */
    List<ChatGroupLog> queryList(ChatGroupLog chatGroupLog);

}
