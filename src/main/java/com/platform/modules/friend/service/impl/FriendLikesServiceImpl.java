package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.friend.service.FriendLikesService;
import com.platform.modules.friend.dao.FriendLikesDao;
import com.platform.modules.friend.domain.FriendLikes;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 朋友圈点赞表 服务层实现
 * </p>
 */
@Service("friendLikesService")
public class FriendLikesServiceImpl extends BaseServiceImpl<FriendLikes> implements FriendLikesService {

    @Resource
    private FriendLikesDao friendLikesDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendLikesDao);
    }

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<FriendLikes> queryList(FriendLikes t) {
        List<FriendLikes> dataList = friendLikesDao.queryList(t);
        return dataList;
    }

    @Override
    public boolean existsByMomentIdAndUserId(Long momentId, Long userId) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("momentId", momentId);
            paramMap.put("userId", userId);
            int count = session.selectOne("com.platform.modules.friend.dao.FriendLikesDao.existsByMomentIdAndUserId", paramMap);
            return count > 0;
        }
    }

}
