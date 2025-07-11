package com.platform.modules.chat.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.domain.ChatRobot;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

/**
 * 服务号详情
 */
@Data
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class RobotVo01 {

    /**
     * 服务号
     */
    private Long robotId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 头像
     */
    private String portrait;
    /**
     * 菜单
     */
    private String menu;
    /**
     * 置顶
     */
    private YesOrNoEnum top;
    /**
     * 静默
     */
    private YesOrNoEnum disturb;

    public RobotVo01(ChatRobot robot) {
        this.robotId = robot.getRobotId();
        this.nickname = robot.getNickname();
        this.portrait = robot.getPortrait();
        this.menu = StringUtils.isEmpty(robot.getMenu()) ? "[]" : robot.getMenu();
        this.top = robot.getTop();
        this.disturb = robot.getDisturb();
    }

}
