package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatNumber;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 微聊号码 数据库访问层
 * </p>
 */
@Repository
public interface ChatNumberDao extends BaseDao<ChatNumber> {

    /**
     * 查询列表
     */
    List<ChatNumber> queryList(ChatNumber chatNumber);

}
