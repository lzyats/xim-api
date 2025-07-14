package com.platform.modules.friend.service;

import com.platform.modules.friend.domain.FriendMoments;
import com.platform.common.web.service.BaseService;
import com.platform.modules.friend.vo.*;

import java.util.List;

/**
 * <p>
 * 朋友圈动态表 服务层
 * </p>
 */
public interface FriendMomentsService extends BaseService<FriendMoments> {

     /**
     * 支持分页的 getlist 方法
     */
     Page<MomentVo01> getlist(Long userId, Pageable pageable);



}
