package com.platform.modules.friend.dao;


import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

import java.util.Map;

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

    /**
     * 获取朋友圈动态并关联用户昵称和头像
     */
    List<Map<String, Object>> getMomentsWithUserInfo(Long userId);

}
