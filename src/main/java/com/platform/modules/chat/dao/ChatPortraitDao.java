package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatPortrait;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 聊天头像 数据库访问层
 * </p>
 */
@Repository
public interface ChatPortraitDao extends BaseDao<ChatPortrait> {

    /**
     * 查询列表
     */
    List<ChatPortrait> queryList(ChatPortrait chatPortrait);

}
