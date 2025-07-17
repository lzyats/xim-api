package com.platform.modules.friend.service;

import com.platform.modules.friend.domain.FriendComments;
import com.platform.common.web.service.BaseService;
import cn.hutool.core.lang.Dict;
import com.github.pagehelper.PageInfo;
import com.platform.modules.friend.vo.*;

/**
 * <p>
 * 朋友圈评论表 服务层
 * </p>
 */
public interface FriendCommentsService extends BaseService<FriendComments> {

    /**
     * 列表
     */
    PageInfo queryListall(Long momentId);

    /**
     * admomnet
     */



}
