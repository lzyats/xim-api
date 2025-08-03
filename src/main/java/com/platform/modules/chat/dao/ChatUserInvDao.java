package com.platform.modules.chat.dao;

import com.platform.modules.chat.domain.ChatUserInv;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 会员注册邀请表 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserInvDao extends BaseDao<ChatUserInv> {

    /**
     * 查询列表
     */
    List<ChatUserInv> queryList(ChatUserInv chatUserInv);

}
