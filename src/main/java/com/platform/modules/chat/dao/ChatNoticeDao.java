package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatNotice;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 通知公告 数据库访问层
 * </p>
 */
@Repository
public interface ChatNoticeDao extends BaseDao<ChatNotice> {

    /**
     * 查询列表
     */
    List<ChatNotice> queryList(ChatNotice chatNotice);

}
