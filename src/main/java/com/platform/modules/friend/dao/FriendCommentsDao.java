package com.platform.modules.friend.dao;

import com.platform.modules.friend.domain.FriendComments;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 朋友圈评论表 数据库访问层
 * </p>
 */
@Repository
public interface FriendCommentsDao extends BaseDao<FriendComments> {

    /**
     * 查询列表
     */
    List<FriendComments> queryList(FriendComments friendComments);

}
