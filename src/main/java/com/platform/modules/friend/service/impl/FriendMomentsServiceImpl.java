package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.vo.MomentVo01;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import com.platform.common.web.service.impl.BaseServiceImpl;
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
        // 合并查询friend_moments表和chat_user表，获取所需数据
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsWithUserInfo(userId);

        MomentVo01 m1 = new MomentVo01();
        // 可以在这里根据需要对m1进行进一步的处理，例如将allMoments的数据赋值给m1的相应属性
        // 示例：将查询结果中的数据赋值给MomentVo01对象
        // 这里假设只取第一条数据，实际情况可根据需求修改
        if (!allMoments.isEmpty()) {
            Map<String, Object> moment = allMoments.get(0);
            m1.setMomentId((Long) moment.get("moment_id"));
            m1.setUserId((Long) moment.get("user_id"));
            m1.setPortrait((String) moment.get("portrait"));
            m1.setNickname((String) moment.get("nickname"));
            m1.setContent((String) moment.get("content"));
            m1.setLocation((String) moment.get("location"));
            // 注意：create_time 类型需要根据实际情况转换
            // m1.setCreateTime(...)
        }

        return m1;
    }
}