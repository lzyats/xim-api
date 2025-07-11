package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatUserCollect;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 收藏表 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserCollectDao extends BaseDao<ChatUserCollect> {

    /**
     * 查询列表
     */
    List<ChatUserCollect> queryList(ChatUserCollect collect);

}
