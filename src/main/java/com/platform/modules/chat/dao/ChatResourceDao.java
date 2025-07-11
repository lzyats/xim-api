package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 聊天资源 数据库访问层
 * </p>
 */
@Repository
public interface ChatResourceDao extends BaseDao<ChatResource> {

    /**
     * 查询列表
     */
    List<ChatResource> queryList(ChatResource chatResource);

}
