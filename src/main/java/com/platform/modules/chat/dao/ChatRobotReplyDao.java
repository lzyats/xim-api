package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatRobotReply;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 服务号 数据库访问层
 * </p>
 */
@Repository
public interface ChatRobotReplyDao extends BaseDao<ChatRobotReply> {

    /**
     * 查询列表
     */
    List<ChatRobotReply> queryList(ChatRobotReply chatRobotReply);

}
