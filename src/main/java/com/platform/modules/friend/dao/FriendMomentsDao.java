package com.platform.modules.friend.dao;


import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 朋友圈动态表 数据库访问层
 * </p>
 */
@Repository
public interface FriendMomentsDao extends BaseDao<FriendMoments> {

    /**
     * 查询列表
     */
    List<FriendMoments> queryList(FriendMoments friendMoments);

    /**
     * 查询列表
     */
    List<FriendMoments> queryDataList(FriendMoments friendMoments);

}
