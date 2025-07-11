package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupMemberDao extends BaseDao<ChatGroupMember> {

    /**
     * 查询列表
     */
    List<ChatGroupMember> queryList(ChatGroupMember chatGroupMember);

}
