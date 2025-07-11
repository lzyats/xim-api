package com.platform.modules.chat.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.*;
import com.platform.modules.chat.enums.ChatConfigEnum;
import com.platform.modules.chat.service.*;
import com.platform.modules.common.service.HookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 聊天任务 服务层实现
 * </p>
 */
@Slf4j
@Service("chatTaskService")
public class ChatTaskServiceImpl implements ChatTaskService {

    @Resource
    private ChatUserLogService chatUserLogService;

    @Resource
    private ChatVisitService chatVisitService;

    @Resource
    private ChatUserService chatUserService;

    @Resource
    private ChatBannedService chatBannedService;

    @Resource
    private ChatGroupService chatGroupService;

    @Resource
    private ChatConfigService chatConfigService;

    @Resource
    private ChatUserAppealService chatUserAppealService;

    @Resource
    private HookService hookService;

    @Resource
    private ChatTaskService chatTaskService;

    @Override
    public void visit() {
        // 统计最近一周
        Date beginTime = DateUtil.beginOfDay(DateUtil.lastWeek());
        Date endTime = DateUtil.endOfDay(DateUtil.yesterday());
        QueryWrapper<ChatUserLog> wrapper = new QueryWrapper<>();
        wrapper.select("DATE(create_time) AS create_time, COUNT(DISTINCT user_id) AS 'count'");
        wrapper.between("create_time", beginTime, endTime);
        wrapper.groupBy("DATE(create_time)");
        List<ChatUserLog> dataList = chatUserLogService.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 查询
        QueryWrapper<ChatVisit> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("visit_date", beginTime);
        List<Date> visitList = chatVisitService.queryList(queryWrapper)
                .stream().map(ChatVisit::getVisitDate).collect(Collectors.toList());
        dataList.forEach(data -> {
            if (!visitList.contains(data.getCreateTime())) {
                try {
                    chatVisitService.add(new ChatVisit(data.getCreateTime(), data.getCount().intValue()));
                } catch (Exception e) {
                    log.error("定时任务[用户日活]执行失败", e);
                }
            }
        });
    }

    @Override
    public void banned() {
        QueryWrapper<ChatBanned> wrapper = new QueryWrapper<>();
        wrapper.lt(ChatBanned.COLUMN_BANNED_TIME, DateUtil.now());
        List<ChatBanned> dataList = chatBannedService.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        dataList.forEach(data -> {
            try {
                chatTaskService.doBanned(data.getBannedId());
            } catch (Exception e) {
                log.error("定时任务[用户解封]执行失败", e);
            }
        });
    }

    @Transactional
    @Override
    public void doBanned(Long userId) {
        // 更新
        ChatUser chatUser = new ChatUser(userId).setBanned(YesOrNoEnum.NO);
        chatUserService.updateById(chatUser);
        // 删除
        chatBannedService.deleteById(userId);
        // 删除
        chatUserAppealService.deleteById(userId);
        // 日志
        chatUserLogService.addLog(userId);
        // 推送
        hookService.doTask(userId);
    }

    @Override
    public void level() {
        QueryWrapper<ChatGroup> wrapper = new QueryWrapper<>();
        wrapper.gt(ChatGroup.COLUMN_GROUP_LEVEL, 0);
        wrapper.lt(ChatGroup.COLUMN_GROUP_LEVEL_TIME, DateUtil.now());
        List<ChatGroup> dataList = chatGroupService.queryList(wrapper);
        // 集合判空
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 群组成员默认数量
        Integer levelCount = chatConfigService.queryConfig(ChatConfigEnum.GROUP_LEVEL_COUNT).getInt();
        dataList.forEach(data -> {
            try {
                chatTaskService.doLevel(data.getGroupId(), levelCount);
            } catch (Exception e) {
                log.error("定时任务[群组降级]执行失败", e);
            }
        });
    }

    @Override
    public void doLevel(Long groupId, Integer levelCount) {
        // 更新
        ChatGroup chatGroup = new ChatGroup(groupId).setGroupLevel(0).setGroupLevelCount(levelCount);
        chatGroupService.updateById(chatGroup);
    }

}
