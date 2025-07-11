package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupApply;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 群组申请表 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupApplyDao extends BaseDao<ChatGroupApply> {

    /**
     * 查询列表
     */
    List<ChatGroupApply> queryList(ChatGroupApply apply);

}
