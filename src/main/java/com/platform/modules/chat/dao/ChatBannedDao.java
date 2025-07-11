package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatBanned;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 封禁状态 数据库访问层
 * </p>
 */
@Repository
public interface ChatBannedDao extends BaseDao<ChatBanned> {

    /**
     * 查询列表
     */
    List<ChatBanned> queryList(ChatBanned chatBanned);

}
