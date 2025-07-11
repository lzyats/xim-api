package com.platform.modules.chat.service.impl;

import com.platform.common.constant.AppConstants;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Override
    public PushFrom getPushFrom(Long robotId) {
        ChatRobot chatRobot = chatRobotService.getById(robotId);
        return chatRobot.getPushFrom();
    }

    @Override
    public List<RobotVo01> getRobotList() {
        Long current = ShiroUtils.getUserId();
        // 查询
        List<ChatRobot> dataList = chatRobotDao.getRobotList(current);
        // 转换
        return dataList.stream().collect(ArrayList::new, (x, y) -> {
            x.add(new RobotVo01(y));
        }, ArrayList::addAll);
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
