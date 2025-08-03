package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.modules.chat.service.impl.ChatVersionServiceImpl;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMedias;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.modules.friend.service.FriendMediasService;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.platform.common.web.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.sql.Timestamp;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

/**
 * <p>
 * 朋友圈动态表 服务层实现
 * </p>
 */
@Service("friendMomentsService")
public class FriendMomentsServiceImpl extends BaseServiceImpl<FriendMoments> implements FriendMomentsService {

    @Resource
    private FriendMomentsDao friendMomentsDao;

    @Resource
    private FriendMomentsService friendMomentsService;

    @Resource
    private FriendMediasService friendMediasService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(friendMomentsDao);
    }

    // 初始化日志对象（注意：这里应使用当前类的字节码对象）
    private static final Logger logger = LoggerFactory.getLogger(ChatVersionServiceImpl.class);

    @Override
    public List<FriendMoments> queryList(FriendMoments t) {
        List<FriendMoments> dataList = friendMomentsDao.queryList(t);
        return dataList;
    }

    @Transactional
    @Override
    public PageInfo<MomentVo01> getlist(Long userId) {
        // 1. 执行DAO查询（此时若已通过PageHelper.startPage开启分页，返回的是Page类型，含分页元数据）
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsWithUserInfo(userId);
        // 强转为Page，获取分页信息（总条数、页码等）
        Page<Map<String, Object>> momentsPage = (Page<Map<String, Object>>) allMoments;
        //logger.info("原始查询分页数据: 总条数={}, 当前页={}, 页大小={}",  momentsPage.getTotal(), momentsPage.getPageNum(), momentsPage.getPageSize());


        // 2. 转换为MomentVo01列表（保持原有逻辑）
        List<MomentVo01> momentVoList = new ArrayList<>();
        for (Map<String, Object> moment : allMoments) {
            MomentVo01 m1 = new MomentVo01();

            // 基本字段赋值
            m1.setMomentId((Long) moment.get("moment_id"));
            m1.setUserId((Long) moment.get("user_id"));
            m1.setPortrait((String) moment.get("portrait"));
            m1.setNickname((String) moment.get("nickname"));
            m1.setContent((String) moment.get("content"));
            m1.setLocation((String) moment.get("location"));
            // 处理时间差（保持原有逻辑）
            Timestamp  createTimeStry = (Timestamp ) moment.get("create_time");
            Date createTimeStr = createTimeStry;
            m1.setCreateTime(createTimeStr);
            // 处理图片、评论、点赞（保持原有逻辑）
            Long momentId = (Long) moment.get("moment_id");
            m1.setImages(friendMomentsDao.getMediasByMomentId(momentId));
            m1.setComments(friendMomentsDao.getCommentsByMomentId(momentId));
            m1.setLikes(friendMomentsDao.getLikesNicknamesByMomentId(momentId));
            momentVoList.add(m1);
        }


        // 3. 构建包含分页信息的Page对象（绑定VO列表和原分页元数据）
        Page<MomentVo01> resultPage = new Page<>(
                momentsPage.getPageNum(),  // 当前页码
                momentsPage.getPageSize()  // 页大小
        );
        resultPage.setTotal(momentsPage.getTotal());  // 总条数（关键：保证分页准确性）
        resultPage.addAll(momentVoList);  // 当前页的VO数据


        // 4. 用PageInfo封装并返回（包含完整分页信息）
        return new PageInfo<>(resultPage);
    }

    @Transactional
    @Override
    public PageInfo<MomentVo01> getlistbyid(Long userId) {
        // 1. 执行DAO查询（此时若已通过PageHelper.startPage开启分页，返回的是Page类型，含分页元数据）
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsById(userId);
        //logger.info("原始数据: {}",allMoments);
        // 强转为Page，获取分页信息（总条数、页码等）
        Page<Map<String, Object>> momentsPage = (Page<Map<String, Object>>) allMoments;
        //logger.info("原始查询分页数据: 总条数={}, 当前页={}, 页大小={}",
         //       momentsPage.getTotal(), momentsPage.getPageNum(), momentsPage.getPageSize());


        // 2. 转换为MomentVo01列表（保持原有逻辑）
        List<MomentVo01> momentVoList = new ArrayList<>();
        for (Map<String, Object> moment : allMoments) {
            MomentVo01 m1 = new MomentVo01();

            // 基本字段赋值
            m1.setMomentId((Long) moment.get("moment_id"));
            m1.setUserId((Long) moment.get("user_id"));
            m1.setPortrait((String) moment.get("portrait"));
            m1.setNickname((String) moment.get("nickname"));
            m1.setContent((String) moment.get("content"));
            m1.setLocation((String) moment.get("location"));
            // 处理时间差（保持原有逻辑）
            Timestamp  createTimeStry = (Timestamp ) moment.get("create_time");
            Date createTimeStr = createTimeStry;
            m1.setCreateTime(createTimeStr);
            // 处理图片
            Long momentId = (Long) moment.get("moment_id");
            m1.setImages(friendMomentsDao.getMediasByMomentId(momentId));
            momentVoList.add(m1);
        }


        // 3. 构建包含分页信息的Page对象（绑定VO列表和原分页元数据）
        Page<MomentVo01> resultPage = new Page<>(
                momentsPage.getPageNum(),  // 当前页码
                momentsPage.getPageSize()  // 页大小
        );
        resultPage.setTotal(momentsPage.getTotal());  // 总条数（关键：保证分页准确性）
        resultPage.addAll(momentVoList);  // 当前页的VO数据


        // 4. 用PageInfo封装并返回（包含完整分页信息）
        return new PageInfo<>(resultPage);
    }

    @Override
    public void admomnet(MomentVo02 momentVo02){
        //添加朋友圈信息
        FriendMoments friendMoments=new FriendMoments()
                .setUserId(momentVo02.getUserId())
                .setContent(momentVo02.getContent())
                .setLocation(momentVo02.getLocation())
                .setVisibility(momentVo02.getVisibility())
                .setCreateTime(DateUtil.date()
                );
        friendMomentsService.add(friendMoments);
        //修改媒体信息的momentId
        Long momentId=friendMoments.getMomentId();
        List<MediasVo02> mediasV2=momentVo02.getImages();
        List<FriendMedias> friendMedias=new ArrayList<>();
        for (int i = 0; i < mediasV2.size(); i++) {
            mediasV2.get(i).setMomentId(momentId);
            FriendMedias fm = new FriendMedias();
            fm.setMomentId(momentId);
            fm.setType(mediasV2.get(i).getType());
            fm.setUrl(mediasV2.get(i).getUrl());
            fm.setThumbnail(mediasV2.get(i).getThumbnail());
            friendMedias.add(fm);
        }
        //批量添加数据
        friendMediasService.batchAdd(friendMedias,mediasV2.size());
    };
}