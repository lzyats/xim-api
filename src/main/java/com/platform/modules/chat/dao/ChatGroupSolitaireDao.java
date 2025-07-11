package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupSolitaire;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 成语接龙 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupSolitaireDao extends BaseDao<ChatGroupSolitaire> {

    /**
     * 查询列表
     */
    List<ChatGroupSolitaire> queryList(ChatGroupSolitaire solitaire);

}
