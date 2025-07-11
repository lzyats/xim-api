package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupInform;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 骚扰举报 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupInformDao extends BaseDao<ChatGroupInform> {

    /**
     * 查询列表
     */
    List<ChatGroupInform> queryList(ChatGroupInform inform);

}
