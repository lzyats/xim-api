package com.platform.modules.friend.service;

import cn.hutool.json.JSONObject;
import com.github.pagehelper.PageInfo;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.modules.chat.domain.ChatUser;
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
     PageInfo<MomentVo01> getlist(Long userId);
     /**
      * 获取指定ID的补发朋友圈数据
      */
     boolean pushlistdata(ChatUser chatUser, ShiroUserVo loginUser);

     /**
      * 获取指定ID的朋友圈信息列表
      */
     PageInfo<MomentVo01> getlistbyid(Long userId,Integer pageNum);

     /**
      * 新增朋友圈信息
      * @param momentVo02
      * @return
      */
     void admomnet(MomentVo02 momentVo02);

     /**
      * 获取指定ID的朋友圈信息
      */

     void getmoments(Long momentId, List<MediasVo02> mediasVo02);

     /**
      * 拉取消息
      */
     List<JSONObject> pullMsg();

     /**
      * 删除指定朋友圈
      * @param momentId
      */
     void deleteMoment(Long momentId,Long msgId);

}
