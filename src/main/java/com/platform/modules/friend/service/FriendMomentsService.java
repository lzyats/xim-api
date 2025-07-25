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
     * 获取指定ID的朋友圈信息列表
     */
     List<MomentVo01> getlist(Long userId);

     /**
      * 新增朋友圈信息
      * @param momentVo02
      * @return
      */
     void admomnet(MomentVo02 momentVo02);

}
