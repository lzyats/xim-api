package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.platform.common.web.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

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
    public List<MomentVo01> getlist(Long userId) {
        // 合并查询friend_moments表和chat_user表，获取所需数据
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsWithUserInfo(userId);

        List<MomentVo01> momentVoList = new ArrayList<>();

        // 遍历所有查询结果
        for (Map<String, Object> moment : allMoments) {
            MomentVo01 m1 = new MomentVo01();

            // 将查询结果中的数据赋值给MomentVo01对象
            m1.setMomentId((Long) moment.get("moment_id"));
            m1.setUserId((Long) moment.get("user_id"));
            m1.setPortrait((String) moment.get("portrait"));
            m1.setNickname((String) moment.get("nickname"));
            m1.setContent((String) moment.get("content"));
            m1.setLocation((String) moment.get("location"));

            // 获取 create_time
            Date createTime = (Date) moment.get("create_time");
            if (createTime != null) {
                // 将 Date 类型的 create_time 转换为 LocalDateTime 类型
                LocalDateTime createDateTime = createTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                // 获取当前时间
                LocalDateTime now = LocalDateTime.now();

                // 计算时间差
                long minutes = ChronoUnit.MINUTES.between(createDateTime, now);
                long hours = ChronoUnit.HOURS.between(createDateTime, now);
                long days = ChronoUnit.DAYS.between(createDateTime, now);
                long weeks = ChronoUnit.WEEKS.between(createDateTime, now);

                String timeDiff;
                if (minutes < 1) {
                    timeDiff = "刚刚";
                } else if (minutes < 60) {
                    timeDiff = minutes + "分钟以前";
                } else if (hours < 24) {
                    timeDiff = hours + "小时以前";
                } else if (days == 1) {
                    timeDiff = "昨天";
                } else if (days < 7) {
                    timeDiff = days + "天以前";
                } else if (weeks == 1) {
                    timeDiff = "一周前";
                } else {
                    // 可以根据需求继续扩展更久时间的描述
                    timeDiff = "很久之前";
                }

                // 设置处理后的时间差到 m1 对象
                m1.setCreateTime(timeDiff);

                // 处理图片列表
                // 查询 friend_medias 表并赋值给 m1.images
                Long momentId = (Long) moment.get("moment_id");
                List<MediasVo01> mediasList = friendMomentsDao.getMediasByMomentId(momentId);
                m1.setImages(mediasList);
                // 处理评论
                // 查询 friend_comments 表并赋值给 m1.comments
                List<CommentsVo01> commentsList = friendMomentsDao.getCommentsByMomentId(momentId);
                m1.setComments(commentsList);

                // 处理点赞信息
                // 查询 friend_likes 表，关联 chat_user 表，获取点赞用户的昵称
                List<String> likes = friendMomentsDao.getLikesNicknamesByMomentId(momentId);
                m1.setLikes(likes);
            }
            momentVoList.add(m1);
        }

        return momentVoList;
    }
}