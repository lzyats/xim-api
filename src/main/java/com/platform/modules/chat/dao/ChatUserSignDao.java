package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatUserSign;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 用户按天签到记录 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserSignDao extends BaseDao<ChatUserSign> {

    /**
     * 查询列表
     */
    List<ChatUserSign> queryList(ChatUserSign chatUserSign);

}
