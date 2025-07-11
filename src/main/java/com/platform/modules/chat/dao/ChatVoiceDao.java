package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatVoice;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 声音表 数据库访问层
 * </p>
 */
@Repository
public interface ChatVoiceDao extends BaseDao<ChatVoice> {

    /**
     * 查询列表
     */
    List<ChatVoice> queryList(ChatVoice chatVoice);

}
