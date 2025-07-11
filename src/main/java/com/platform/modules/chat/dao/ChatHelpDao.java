package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatHelp;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 聊天帮助 数据库访问层
 * </p>
 */
@Repository
public interface ChatHelpDao extends BaseDao<ChatHelp> {

    /**
     * 查询列表
     */
    List<ChatHelp> queryList(ChatHelp chatHelp);

}
