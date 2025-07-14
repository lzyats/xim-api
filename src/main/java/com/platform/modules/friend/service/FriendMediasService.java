package com.platform.modules.friend.service;

import com.github.pagehelper.PageInfo;
import com.platform.modules.friend.domain.FriendMedias;
import com.platform.common.web.service.BaseService;
import com.platform.modules.friend.vo.*;

/**
 * <p>
 * 朋友圈媒体资源表 服务层
 * </p>
 */
public interface FriendMediasService extends BaseService<FriendMedias> {
    /**
            * 列表
     */
    PageInfo queryListall(Long momentId);

}
