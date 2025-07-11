package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatRobot;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 服务号 数据库访问层
 * </p>
 */
@Repository
public interface ChatRobotDao extends BaseDao<ChatRobot> {

    /**
     * 查询列表
     */
    List<ChatRobot> queryList(ChatRobot chatRobot);

    /**
     * 查询关注
     */
    List<ChatRobot> getRobotList(Long userId);

}
