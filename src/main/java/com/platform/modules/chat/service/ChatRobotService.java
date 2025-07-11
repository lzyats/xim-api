package com.platform.modules.chat.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatRobot;
import com.platform.modules.chat.vo.RobotVo01;
import com.platform.modules.chat.vo.RobotVo02;
import com.platform.modules.chat.vo.RobotVo03;
import com.platform.modules.push.dto.PushFrom;

import java.util.List;

/**
 * <p>
 * 服务号 服务层
 * </p>
 */
public interface ChatRobotService extends BaseService<ChatRobot> {

    /**
     * 发送人
     */
    PushFrom getPushFrom(Long robotId);

    /**
     * 获取列表
     */
    List<RobotVo01> getRobotList();

    /**
     * 设置置顶
     */
    void setTop(RobotVo02 robotVo);

    /**
     * 设置静默
     */
    void setDisturb(RobotVo03 robotVo);

}
