package com.platform.modules.friend.dao;


import com.platform.modules.friend.domain.FriendMoments;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

import java.util.Map;

import com.platform.modules.friend.vo.*;

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

    /**
     * 根据 moment_id 查询 friend_medias 表
     * @param momentId 朋友圈动态 ID
     * @return 媒体信息列表
     */
    List<MediasVo01> getMediasByMomentId(@Param("momentId") Long momentId);

    List<CommentsVo01> getCommentsByMomentId(Long momentId);

}
