package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatUserInfo;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 用户详情 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserInfoDao extends BaseDao<ChatUserInfo> {

    /**
     * 查询列表
     */
    List<ChatUserInfo> queryList(ChatUserInfo chatUserInfo);

}
