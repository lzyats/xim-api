package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatUserToken;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 用户token 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserTokenDao extends BaseDao<ChatUserToken> {

    /**
     * 查询列表
     */
    List<ChatUserToken> queryList(ChatUserToken chatUserToken);

}
