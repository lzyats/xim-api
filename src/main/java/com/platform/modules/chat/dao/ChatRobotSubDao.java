package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatRobotSub;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 服务号 数据库访问层
 * </p>
 */
@Repository
public interface ChatRobotSubDao extends BaseDao<ChatRobotSub> {

    /**
     * 查询列表
     */
    List<ChatRobotSub> queryList(ChatRobotSub chatRobotSub);

}
