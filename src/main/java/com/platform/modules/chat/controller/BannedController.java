package com.platform.modules.chat.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatUserAppealService;
import com.platform.modules.chat.service.ChatUserInfoService;
import com.platform.modules.chat.vo.MineVo16;
import com.platform.modules.chat.vo.MineVo17;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 封禁
 */
@RestController
@Slf4j
@RequestMapping("/banned")
public class BannedController extends BaseController {

    @Resource
    private ChatUserInfoService chatUserInfoService;

    @Resource
    private ChatUserAppealService chatUserAppealService;

    /**
     * 查询封禁
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        MineVo16 data = chatUserInfoService.getBannedInfo();
        return AjaxResult.success(data);
    }

    /**
     * 申请解封
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/appeal")
    public AjaxResult appeal(@Validated @RequestBody MineVo17 mineVo) {
        chatUserAppealService.appealBanned(mineVo);
        return AjaxResult.success();
    }

}
