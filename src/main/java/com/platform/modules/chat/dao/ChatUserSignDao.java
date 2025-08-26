package com.platform.modules.chat.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.platform.modules.chat.domain.ChatUserSign;
import com.platform.modules.chat.vo.ChatSignVo01;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户按天签到记录 数据库访问层
 * </p>
 */
@Repository
public interface ChatUserSignDao extends BaseDao<ChatUserSign> {

    /**
     * 查询列表
     */
    List<ChatUserSign> queryList(ChatUserSign chatUserSign);

    /**
     * 查询列表
     */
    List<ChatSignVo01> queryList1(@Param(Constants.WRAPPER) Wrapper<ChatSignVo01> wrapper);

}
