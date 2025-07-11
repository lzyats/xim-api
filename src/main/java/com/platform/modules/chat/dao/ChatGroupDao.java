package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.vo.GroupVo11;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 群组 数据库访问层
 * </p>
 */
@Repository
public interface ChatGroupDao extends BaseDao<ChatGroup> {

    /**
     * 查询列表
     */
    List<ChatGroup> queryList(ChatGroup chatGroup);

    /**
     * 查询列表
     */
    List<ChatGroup> querySearch(Long userId, String param);

    /**
     * 查询列表
     */
    List<GroupVo11> groupList(Long userId);

}
