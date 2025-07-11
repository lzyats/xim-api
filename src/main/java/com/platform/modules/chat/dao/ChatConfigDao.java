package com.platform.modules.chat.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.chat.domain.ChatConfig;
import com.platform.modules.chat.enums.ChatConfigEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 设置表 数据库访问层
 * </p>
 */
@Repository
public interface ChatConfigDao extends BaseDao<ChatConfig> {

    /**
     * 查询列表
     */
    List<ChatConfig> queryList(ChatConfig chatConfig);

    /**
     * 通过key查询
     */
    ChatConfig queryByKey(ChatConfigEnum configType);

}
