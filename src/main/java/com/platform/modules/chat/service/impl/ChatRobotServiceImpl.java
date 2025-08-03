package com.platform.modules.chat.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.redis.RedisUtils;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatRobotDao;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.domain.ChatRobotSub;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.chat.service.ChatRobotSubService;
import com.platform.modules.chat.vo.RobotVo01;
import com.platform.modules.chat.vo.RobotVo02;
import com.platform.modules.chat.vo.RobotVo03;
import com.platform.modules.push.dto.PushFrom;
import com.platform.modules.push.dto.PushSetting;
import com.platform.modules.push.enums.PushSettingEnum;
import com.platform.modules.push.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.platform.common.redis.RedisJsonUtil;

import java.util.Collection;
import java.util.Collections; // 新增：用于返回空列表
import java.util.stream.Collectors;  // 新增这行导入

/**
 * <p>
 * 服务号 服务层实现
 * </p>
 */
@Service("chatRobotService")
@CacheConfig(cacheNames = AppConstants.REDIS_CHAT_ROBOT)
public class ChatRobotServiceImpl extends BaseServiceImpl<ChatRobot> implements ChatRobotService {

    @Resource
    private ChatRobotDao chatRobotDao;

    @Resource
    private ChatRobotService chatRobotService;

    @Resource
    private ChatRobotSubService chatRobotSubService;

    @Autowired
    private RedisJsonUtil redisJsonUtil;

    @Resource
    private PushService pushService;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatRobotDao);
    }



    @Override
    public List<ChatRobot> queryList(ChatRobot t) {
        List<ChatRobot> dataList = chatRobotDao.queryList(t);
        return dataList;
    }

    @Override
    @Cacheable(key = "#robotId", unless = "#result == null")
    public ChatRobot getById(Long robotId) {
        return super.getById(robotId);
    }

    // 初始化日志对象（注意：这里应使用当前类的字节码对象）
    private static final Logger logger = LoggerFactory.getLogger(ChatVersionServiceImpl.class);

    @Override
    public PushFrom getPushFrom(Long robotId) {
        ChatRobot chatRobot = chatRobotService.getById(robotId);
        return chatRobot.getPushFrom();
    }

    @Override
    public List<RobotVo01> getRobotList() {
        Long current = ShiroUtils.getUserId();
        // 缓存key（包含用户ID，避免不同用户数据混淆）
        String redisKey = AppConstants.REDIS_COMMON_CONFIG + "robot:list:" + current;
        // 1. 从Redis的List中获取所有数据（range 0 -1 表示获取全部元素）
        List<ChatRobot> cacheList = redisJsonUtil.range(redisKey, 0, -1, ChatRobot.class);

        // 2. 缓存存在且不为空，直接转换返回
        if (cacheList != null && !cacheList.isEmpty()) {
            logger.info("从Redis缓存获取getRobotList信息成功，用户ID：{}", current);
            return cacheList.stream()
                    .map(RobotVo01::new)
                    .collect(Collectors.toList());
        }

        // 3. 缓存不存在，从数据库查询数据
        logger.info("Redis缓存未命中，从数据库查询getRobotList信息，用户ID：{}", current);
        List<ChatRobot> dataList = chatRobotDao.getRobotList(current);
        if (dataList == null || dataList.isEmpty()) {
            return new ArrayList<>(); // 数据库也无数据，返回空列表
        }

        // 4. 将数据库查询结果写入Redis的List（leftPushAll批量从左侧插入）
        // 先清空可能存在的旧数据（避免残留脏数据）
        redisJsonUtil.delete(redisKey);
        // 循环插入每个元素
        for (ChatRobot robot : dataList) {
            // 此处不设置过期时间，避免重复设置，最后统一设置
            redisJsonUtil.leftPush(redisKey, robot, null, null);
        }
        // 统一设置过期时间（5分钟）
        redisJsonUtil.expire(redisKey, 300, TimeUnit.SECONDS);
        // 设置过期时间（例如2小时，根据业务调整）
        logger.info("数据写入Redis缓存成功，用户ID：{}，缓存key：{}", current, redisKey);

        // 5. 转换并返回数据
        return dataList.stream()
                .map(RobotVo01::new)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void setTop(RobotVo02 robotVo) {
        Long robotId = robotVo.getRobotId();
        YesOrNoEnum top = robotVo.getTop();
        // 查询订阅
        ChatRobotSub robotSub = this.getRobotSub(robotId);
        // 更新
        chatRobotSubService.updateById(new ChatRobotSub(robotSub.getSubId()).setTop(top));
        // 推送通知
        Long current = ShiroUtils.getUserId();
        pushSetting(current, robotId, ChatRobot.LABEL_TOP, top.getCode());
    }

    @Transactional
    @Override
    public void setDisturb(RobotVo03 robotVo) {
        Long robotId = robotVo.getRobotId();
        YesOrNoEnum disturb = robotVo.getDisturb();
        // 查询订阅
        ChatRobotSub robotSub = this.getRobotSub(robotId);
        // 更新
        chatRobotSubService.updateById(new ChatRobotSub(robotSub.getSubId()).setDisturb(disturb));
        // 推送通知
        Long current = ShiroUtils.getUserId();
        pushSetting(current, robotId, ChatRobot.LABEL_DISTURB, disturb.getCode());
    }

    /**
     * 查询订阅
     */
    private ChatRobotSub getRobotSub(Long robotId) {
        // 查询
        ChatRobot chatRobot = chatRobotService.getById(robotId);
        if (chatRobot == null) {
            throw new BaseException("服务号已停用");
        }
        Long current = ShiroUtils.getUserId();
        ChatRobotSub robotSub = chatRobotSubService.queryOne(new ChatRobotSub(robotId, current));
        if (robotSub == null) {
            robotSub = new ChatRobotSub(robotId, current)
                    .setTop(YesOrNoEnum.NO)
                    .setDisturb(YesOrNoEnum.NO);
            chatRobotSubService.add(robotSub);
        }
        return robotSub;
    }

    /**
     * 推送
     */
    public void pushSetting(Long userId, Long object, String label, String value) {
        PushSetting setting = new PushSetting(PushSettingEnum.ROBOT, object, label, value);
        pushService.pushSetting(setting, Arrays.asList(userId));
    }

}
