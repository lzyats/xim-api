package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatUserAppeal;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户申诉 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserAppealDao extends BaseDao<ChatUserAppeal> {

    /**
     * 查询列表
     */
    List<ChatUserAppeal> queryList(ChatUserAppeal chatUserAppeal);

}
