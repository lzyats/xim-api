package com.platform.modules.chat.controller;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.config.PlatformConfig;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatRobotService;
import com.platform.modules.chat.vo.RobotVo01;
import com.platform.modules.chat.vo.RobotVo02;
import com.platform.modules.chat.vo.RobotVo03;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务号
 */
@RestController
@Slf4j
@RequestMapping("/robot")
public class RobotController extends BaseController {

    @Resource
    private ChatRobotService chatRobotService;

    /**
     * 服务号列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getRobotList")
    public AjaxResult getRobotList() {
        List<RobotVo01> dataList = chatRobotService.getRobotList();
        return AjaxResult.success(dataList, PlatformConfig.SECRET);
    }

    /**
     * 设置置顶
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setTop")
    public AjaxResult setTop(@Validated @RequestBody RobotVo02 robotVo) {
        chatRobotService.setTop(robotVo);
        return AjaxResult.success();
    }

    /**
     * 设置静默
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/setDisturb")
    public AjaxResult setDisturb(@Validated @RequestBody RobotVo03 robotVo) {
        chatRobotService.setDisturb(robotVo);
        return AjaxResult.success();
    }

}
