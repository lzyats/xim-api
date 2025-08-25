package com.platform.modules.friend.service.impl;

import javax.annotation.Resource;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUserVo;
import com.platform.common.shiro.ShiroUtils;
import com.platform.modules.auth.service.TokenService;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.friend.dao.FriendMomentsDao;
import com.platform.modules.friend.domain.FriendMedias;
import com.platform.modules.friend.domain.FriendMoments;
import com.platform.modules.friend.service.FriendMediasService;
import com.platform.modules.friend.service.FriendMomentsService;
import com.platform.modules.friend.vo.*;
import com.platform.modules.push.dto.PushComments;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushMedias;
import com.platform.modules.push.dto.PushMoment;
import com.platform.modules.push.enums.PushMomentEnum;
import com.platform.modules.push.service.PushService;
import com.platform.modules.quartz.service.QuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.platform.common.web.service.impl.BaseServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 朋友圈动态表 服务层实现
 * </p>
 */
@Slf4j
@Service("friendMomentsService")
public class FriendMomentsServiceImpl extends BaseServiceImpl<FriendMoments> implements FriendMomentsService {

    @Resource
    private FriendMomentsDao friendMomentsDao;

    @Resource
    private FriendMomentsService friendMomentsService;

    @Resource
    private FriendMediasService friendMediasService;

    @Resource
    private PushService pushService;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisUtils redisUtils;

    @Autowired
    private QuartzJobService quartzJobService;

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
    public boolean pushlistdata(ChatUser chatUser,ShiroUserVo loginUser) {
        Long userId=chatUser.getUserId();
        String token=loginUser.getToken();
        // 1. 执行DAO查询
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsWithUserInfo(userId);
        // 2. 转换为PushMoment列表（保持原有逻辑）
        Long lastMomentId = 0L;
        List<PushMoment> pushMoments = new ArrayList<>();
        for (Map<String, Object> moment : allMoments) {
            Long momentId = (Long) moment.get("moment_id");
            lastMomentId=momentId;
            //组装消息
            Long MsgId= IdWorker.getId();
            Long SyncId= IdWorker.getId();
            List<PushMedias> pushMedias = new ArrayList<>();
            List<PushComments> pushComments = new ArrayList<>();
            List<MediasVo01> mediasVo = friendMomentsDao.getMediasByMomentId(momentId);
            for (MediasVo01 mediasVo2 : mediasVo) {
                PushMedias  pushMedias1 = new PushMedias()
                        .setType(mediasVo2.getType())
                        .setUrl(mediasVo2.getUrl())
                        .setThumbnail(mediasVo2.getThumbnail())
                        ;
                pushMedias.add(pushMedias1);
            }
            List<CommentsVo01>  commentsVo01s = friendMomentsDao.getCommentsByMomentId(momentId);
            for (CommentsVo01 commentsVo02s : commentsVo01s) {
                PushComments  pushComments1 = new PushComments()
                        .setSource(commentsVo02s.getSource())
                        .setContent(commentsVo02s.getContent())
                        .setFromUser(commentsVo02s.getFromUser())
                        .setToUser(commentsVo02s.getToUser())
                        ;
                pushComments.add(pushComments1);
            }
            PushMoment pushMoment=new PushMoment()
                    .setMsgId(MsgId.toString())
                    .setMomentId(momentId)
                    .setUserId((Long)moment.get("user_id"))
                    .setPortrait((String) moment.get("portrait"))
                    .setNickname((String) moment.get("nickname"))
                    .setContent((String) moment.get("content"))
                    .setLocation((String) moment.get("location"))
                    .setType(PushMomentEnum.MOMENT.getCode())
                    .setCreateTime((Timestamp ) moment.get("create_time"))
                    .setImages(pushMedias)
                    .setComments(pushComments)
                    .setLikes(friendMomentsDao.getLikesNicknamesByMomentId(momentId))
                    ;
            PushFrom pushFrom = new PushFrom()
                    .setMsgId(MsgId)
                    .setSyncId(MsgId)
                    .setUserId(chatUser.getUserId())
                    .setNickname(chatUser.getNickname())
                    .setPortrait(chatUser.getPortrait())
                    .setSign(loginUser.getSign());
            pushService.pushMomentSync(pushFrom, pushMoment,Arrays.asList(userId),PushMomentEnum.MOMENT,true);
        }
        log.info("开始定时任务=>");
        quartzJobService.triggerDelayedMessage(userId,8);
        ShiroUserVo userVo = new ShiroUserVo().setLastMomentId(lastMomentId.toString());
        tokenService.refresh(Arrays.asList(token), userVo);
        return true;
    }

    @Transactional
    @Override
    public PageInfo<MomentVo01> getlistbyid(Long userId) {
        Long current = ShiroUtils.getUserId();
        // 1. 执行DAO查询（此时若已通过PageHelper.startPage开启分页，返回的是Page类型，含分页元数据）
        List<Map<String, Object>> allMoments = friendMomentsDao.getMomentsById(userId,current);
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

    @Transactional
    @Override
    public void admomnet(MomentVo02 momentVo02){
        //log.info("接收信息：{}",momentVo02);
        //添加朋友圈信息
        FriendMoments friendMoments=new FriendMoments()
                .setUserId(momentVo02.getUserId())
                .setContent(momentVo02.getContent())
                .setLocation(momentVo02.getLocation())
                .setVisibility(momentVo02.getVisibility())
                .setVisuser(momentVo02.getVisuser())
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
            fm.setWidth(mediasV2.get(i).getWidth());
            fm.setHeight(mediasV2.get(i).getHeight());
            fm.setThumbnail(mediasV2.get(i).getThumbnail());
            friendMedias.add(fm);
        }
        //批量添加数据
        friendMediasService.batchAdd(friendMedias,mediasV2.size());
        this.getmoments(momentId,mediasV2);
    };

    /**
     * 拉取消息
     */
    @Override
    public List<JSONObject> pullMsg() {
        Long current = ShiroUtils.getUserId();
        String lastId = ShiroUtils.getLastMomentId();
        log.info(lastId);
        String token = ShiroUtils.getToken();
        // 获取lastId
        if(lastId==null || lastId.isEmpty()){
            ShiroUserVo shiroUser = tokenService.convert(token);
            lastId=shiroUser.getLastMomentId();
        }
        // 拉取消息
        List<JSONObject> dataList = pushService.pullMomentMsg(current, lastId, AppConstants.MESSAGE_LIMIT);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return dataList;
        }
        // 刷新msgId
        JSONObject data = dataList.get(dataList.size() - 1);
        String msgId = data.getJSONObject("pushData").getStr("msgId");
        //log.info("setLastMomentId：{}",msgId);
        ShiroUserVo userVo = new ShiroUserVo().setLastMomentId(msgId);
        tokenService.refresh(Arrays.asList(token), userVo);
        return dataList;
    }

    /**
     * 删除指定朋友圈
     * @param momentId 朋友圈ID
     */
    @Transactional
    @Override
    public void deleteMoment(Long momentId,Long msgId) {
        // 参数校验
        if (momentId == null) {
            throw new IllegalArgumentException("朋友圈ID不能为空");
        }

        Long currentUserId = ShiroUtils.getUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("当前用户未登录");
        }



        // 执行删除操作（根据moment_id和user_id双重条件）
        int deleteCount = friendMomentsDao.deleteByMomentIdAndUserId(momentId, currentUserId);
        // 删除缓存中的消息
        String redisKey = AppConstants.REDIS_PUSH_MOMENT + msgId;
        redisUtils.delete(redisKey);
        //发送广播通知所有人删除本地消息
        this.getmoments(momentId,null,1);
        // 校验删除结果
        if (deleteCount == 0) {
            throw new IllegalStateException("消息不存在或无权限删除");
        }
        //发送广播通知所有人删除本地消息

    }

    /**
     * 查询朋友圈信息并向所有人发送
     * @param momentId
     * @param mediasVo02
     */
    @Transactional
    public void getmoments(Long momentId,List<MediasVo02> mediasVo02){
        Long current = ShiroUtils.getUserId();
        MomentVo03 momentVo03 = friendMomentsDao.getMomentsByMomentId(momentId);
        int isDeleted=momentVo03.getIsdelete();
        //log.info("最后的查询结果：{}",momentVo03);
        // 查询出符合条件的接收人ID
        List<Long> userlist = new ArrayList<Long>();
        if(momentVo03.getVisibility() ==3){
            List<String> visuser=momentVo03.getVisuser();
            //log.info(visuser.toString());
            // 处理空值或空字符串
            if (visuser == null || visuser.isEmpty()) {
                //log.info("visuser is null");
            }else{
                // 转换为Long类型并收集（处理可能的格式异常）
                for (String userIdStr : visuser) {
                    //log.info(userIdStr);
                    // 去除前后空格（避免字符串中包含空格导致转换失败）
                    String trimmed = userIdStr.trim();
                    if (!trimmed.isEmpty()) {
                        userlist.add(Long.parseLong(trimmed));
                    }
                }
            }

        }else{
            userlist=friendMomentsDao.getQualifiedUserIdsByMomentId(momentId);
        }
        //接收人列表中应该加上自己
        if(!userlist.contains(momentVo03.getUserId()))
            userlist.add(momentVo03.getUserId());
        if(!userlist.contains(current))
            userlist.add(current);
        //查询其他信息
        List<PushMedias> pushMediasList=new ArrayList<>();
        if(mediasVo02==null){
            List<MediasVo01> mediasVo01s=friendMomentsDao.getMediasByMomentId(momentId);
            for (int i = 0; i < mediasVo01s.size(); i++) {
                PushMedias pushMedias=new PushMedias();
                pushMedias.setType(mediasVo01s.get(i).getType());
                pushMedias.setUrl(mediasVo01s.get(i).getUrl());
                pushMedias.setWidth(mediasVo01s.get(i).getWidth());
                pushMedias.setHeight(mediasVo01s.get(i).getHeight());
                pushMedias.setThumbnail(mediasVo01s.get(i).getThumbnail());
                pushMediasList.add(pushMedias);
            }
        }else{
            for (int i = 0; i < mediasVo02.size(); i++) {
                mediasVo02.get(i).setMomentId(momentId);
                PushMedias pushMedias=new PushMedias();
                pushMedias.setType(mediasVo02.get(i).getType());
                pushMedias.setUrl(mediasVo02.get(i).getUrl());
                pushMedias.setWidth(mediasVo02.get(i).getWidth());
                pushMedias.setHeight(mediasVo02.get(i).getHeight());
                pushMedias.setThumbnail(mediasVo02.get(i).getThumbnail());
                pushMediasList.add(pushMedias);
            }
        }
        List<PushComments> pushCommentsList=new ArrayList<>();
        List<CommentsVo01> commentsVo01s= friendMomentsDao.getCommentsByMomentId(momentId);
        for (int i = 0; i < commentsVo01s.size(); i++) {
            PushComments pushComments=new PushComments();
            pushComments.setContent(commentsVo01s.get(i).getContent());
            pushComments.setFromUser(commentsVo01s.get(i).getFromUser());
            pushComments.setToUser(commentsVo01s.get(i).getToUser());
            boolean Source=true;
            if(Objects.equals(commentsVo01s.get(i).getFromUser(), momentVo03.getNickname()) && !Objects.equals(commentsVo01s.get(i).getFromUser(), commentsVo01s.get(i).getToUser())){
                Source=false;
            }
            pushComments.setSource(Source);
            pushCommentsList.add(pushComments);
        }
        List<String> likes= friendMomentsDao.getLikesNicknamesByMomentId(momentId);
        //组装消息
        Long MsgId= IdWorker.getId();
        Long SyncId= IdWorker.getId();
        PushMoment pushMoment=new PushMoment()
                .setMsgId(MsgId.toString())
                .setMomentId(momentId)
                .setUserId(momentVo03.getUserId())
                .setPortrait(momentVo03.getPortrait())
                .setNickname(momentVo03.getNickname())
                .setContent(momentVo03.getContent())
                .setLocation(momentVo03.getLocation())
                .setType(PushMomentEnum.MOMENT.getCode())
                .setCreateTime(momentVo03.getCreateTime())
                .setIsDeleted(momentVo03.getIsdelete())
                .setImages(pushMediasList)
                .setComments(pushCommentsList)
                .setLikes(likes)
                ;
        if(isDeleted!=0){
            pushMoment.setIsDeleted(1);
        }
        log.info("接收人列表："+userlist);
        PushFrom pushFrom = new PushFrom()
                .setMsgId(MsgId)
                .setSyncId(MsgId)
                .setUserId(momentVo03.getUserId())
                .setNickname(momentVo03.getNickname())
                .setPortrait(momentVo03.getPortrait())
                .setSign(ShiroUtils.getSign());
        //pushService.pushMoment(pushFrom,pushMoment,userlist);
        pushMoment.setSyncId(MsgId.toString());
        // 同步消息
        //log.info("发送内容：{}",pushMoment);
        pushService.pushMomentSync(pushFrom, pushMoment,userlist,PushMomentEnum.MOMENT);
        //发送朋友圈计数器
        //wpushService.pushBadger(userlist, PushBadgerEnum.MOMENT,true);
    }

    /**
     * 查询朋友圈信息并向所有人发送
     * @param momentId
     * @param mediasVo02
     */
    @Transactional
    public void getmoments(Long momentId,List<MediasVo02> mediasVo02,int delete){
        Long current = ShiroUtils.getUserId();
        MomentVo03 momentVo03 = friendMomentsDao.getMomentsByMomentId(momentId);
        int isDeleted=momentVo03.getIsdelete();
        //log.info("最后的查询结果：{}",momentVo03);
        // 查询出符合条件的接收人ID
        List<Long> userlist = new ArrayList<Long>();
        if(momentVo03.getVisibility() ==3){
            List<String> visuser=momentVo03.getVisuser();
            //log.info(visuser.toString());
            // 处理空值或空字符串
            if (visuser == null || visuser.isEmpty()) {
                //log.info("visuser is null");
            }else{
                // 转换为Long类型并收集（处理可能的格式异常）
                for (String userIdStr : visuser) {
                    //log.info(userIdStr);
                    // 去除前后空格（避免字符串中包含空格导致转换失败）
                    String trimmed = userIdStr.trim();
                    if (!trimmed.isEmpty()) {
                        userlist.add(Long.parseLong(trimmed));
                    }
                }
            }

        }else{
            userlist=friendMomentsDao.getQualifiedUserIdsByMomentId(momentId);
        }
        //接收人列表中应该加上自己
        if(!userlist.contains(momentVo03.getUserId()))
            userlist.add(momentVo03.getUserId());
        if(!userlist.contains(current))
            userlist.add(current);
        //查询其他信息
        List<PushMedias> pushMediasList=new ArrayList<>();
        if(mediasVo02==null){
            List<MediasVo01> mediasVo01s=friendMomentsDao.getMediasByMomentId(momentId);
            for (int i = 0; i < mediasVo01s.size(); i++) {
                PushMedias pushMedias=new PushMedias();
                pushMedias.setType(mediasVo01s.get(i).getType());
                pushMedias.setUrl(mediasVo01s.get(i).getUrl());
                pushMedias.setThumbnail(mediasVo01s.get(i).getThumbnail());
                pushMediasList.add(pushMedias);
            }
        }else{
            for (int i = 0; i < mediasVo02.size(); i++) {
                mediasVo02.get(i).setMomentId(momentId);
                PushMedias pushMedias=new PushMedias();
                pushMedias.setType(mediasVo02.get(i).getType());
                pushMedias.setUrl(mediasVo02.get(i).getUrl());
                pushMedias.setThumbnail(mediasVo02.get(i).getThumbnail());
                pushMediasList.add(pushMedias);
            }
        }
        List<PushComments> pushCommentsList=new ArrayList<>();
        List<CommentsVo01> commentsVo01s= friendMomentsDao.getCommentsByMomentId(momentId);
        for (int i = 0; i < commentsVo01s.size(); i++) {
            PushComments pushComments=new PushComments();
            pushComments.setContent(commentsVo01s.get(i).getContent());
            pushComments.setFromUser(commentsVo01s.get(i).getFromUser());
            pushComments.setToUser(commentsVo01s.get(i).getToUser());
            boolean Source=true;
            if(Objects.equals(commentsVo01s.get(i).getFromUser(), momentVo03.getNickname()) && !Objects.equals(commentsVo01s.get(i).getFromUser(), commentsVo01s.get(i).getToUser())){
                Source=false;
            }
            pushComments.setSource(Source);
            pushCommentsList.add(pushComments);
        }
        List<String> likes= friendMomentsDao.getLikesNicknamesByMomentId(momentId);
        //组装消息
        Long MsgId= IdWorker.getId();
        Long SyncId= IdWorker.getId();
        PushMoment pushMoment=new PushMoment()
                .setMsgId(MsgId.toString())
                .setMomentId(momentId)
                .setUserId(momentVo03.getUserId())
                .setPortrait(momentVo03.getPortrait())
                .setNickname(momentVo03.getNickname())
                .setContent(momentVo03.getContent())
                .setLocation(momentVo03.getLocation())
                .setType(PushMomentEnum.MOMENT.getCode())
                .setCreateTime(momentVo03.getCreateTime())
                .setIsDeleted(momentVo03.getIsdelete())
                .setImages(pushMediasList)
                .setComments(pushCommentsList)
                .setLikes(likes)
                ;
        if(delete!=0){
            pushMoment.setIsDeleted(1);
        }
        log.info("接收人列表："+userlist);
        PushFrom pushFrom = new PushFrom()
                .setMsgId(MsgId)
                .setSyncId(MsgId)
                .setUserId(momentVo03.getUserId())
                .setNickname(momentVo03.getNickname())
                .setPortrait(momentVo03.getPortrait())
                .setSign(ShiroUtils.getSign());
        pushMoment.setSyncId(MsgId.toString());
        // 同步消息
        pushService.pushMomentSync(pushFrom, pushMoment,userlist,PushMomentEnum.MOMENT);
    }

}