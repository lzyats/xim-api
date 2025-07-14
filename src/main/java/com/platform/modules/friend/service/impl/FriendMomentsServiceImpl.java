package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.modules.friend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMoments;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 朋友圈动态表 服务层实现
 * </p>
 */
@Service("friendMomentsService")
public class FriendMomentsServiceImpl extends BaseServiceImpl<FriendMoments> implements FriendMomentsService {

    @Resource
    private FriendMomentsDao friendMomentsDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMomentsDao);
    }

    @Override
    public List<FriendMoments> queryList(FriendMoments t) {
        List<FriendMoments> dataList = friendMomentsDao.queryList(t);
        return dataList;
    }


    @Transactional
    @Override
    public MomentVo01 getlist(Long userId) {
        //查询出符合用户条件的朋友圈信息
        
        MomentVo01 m1=new MomentVo01();
        // 添加媒体信息
        //chatUserInfoService.addInfo(chatUser.getUserId());
        // 新增钱包
        //walletInfoService.addWallet(chatUser.getUserId());
        return m1;
    }


}
