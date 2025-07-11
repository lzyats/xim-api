package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatVisit;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 用户访问 数据库访问层
 * </p>
 */
@Repository
public interface ChatVisitDao extends BaseDao<ChatVisit> {

    /**
     * 查询列表
     */
    List<ChatVisit> queryList(ChatVisit chatVisit);

}
