package com.platform.modules.friend.service;

import com.platform.modules.friend.domain.FriendLikes;
import com.platform.common.web.service.BaseService;

/**
 * <p>
 * 朋友圈点赞表 服务层
 * </p>
 */
public interface FriendLikesService extends BaseService<FriendLikes> {
    /**
     * 查询某点赞记录是否存在
     * @param momentId
     * @param userId
     * @return
     */
    boolean existsByMomentIdAndUserId(Long momentId, Long userId);

}
