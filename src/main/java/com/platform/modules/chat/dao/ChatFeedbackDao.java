package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatFeedback;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 建议反馈 数据库访问层
 * </p>
 */
@Repository
public interface ChatFeedbackDao extends BaseDao<ChatFeedback> {

    /**
     * 查询列表
     */
    List<ChatFeedback> queryList(ChatFeedback chatFeedback);

}
