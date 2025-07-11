package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatSms;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;

import java.util.List;

/**
 * <p>
 * 短信记录 数据库访问层
 * </p>
 */
@Repository
public interface ChatSmsDao extends BaseDao<ChatSms> {

    /**
     * 查询列表
     */
    List<ChatSms> queryList(ChatSms chatSms);

}
