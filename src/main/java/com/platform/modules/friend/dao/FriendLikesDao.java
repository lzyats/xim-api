package com.platform.modules.friend.dao;

import com.platform.modules.friend.domain.FriendLikes;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 朋友圈点赞表 数据库访问层
 * </p>
 */
@Repository
public interface FriendLikesDao extends BaseDao<FriendLikes> {

    /**
     * 查询列表
     */
    List<FriendLikes> queryList(FriendLikes friendLikes);

    List<FriendLikes> selectByMomentIdAndUserId(Long momentId, Long userId);

}
