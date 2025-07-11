package com.platform.modules.wallet.controller;

import cn.hutool.core.lang.Dict;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.common.enums.MessageTypeEnum;
import com.platform.modules.wallet.service.WalletInfoService;
import com.platform.modules.wallet.vo.WalletVo04;
import com.platform.modules.wallet.vo.WalletVo05;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 钱包
 */
@RestController
@Slf4j
@RequestMapping("/wallet")
public class WalletInfoController extends BaseController {

    @Resource
    private WalletInfoService walletInfoService;

    @Resource
    private ChatUserService chatUserService;

    /**
     * 我的钱包
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getInfo")
    public AjaxResult getInfo() {
        Long current = ShiroUtils.getUserId();
        BigDecimal data = walletInfoService.getInfo(current);
        return AjaxResult.success(data);
    }

    /**
     * 发送验证码
     */
    @Deprecated
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping(value = "/sendCode")
    @SubmitRepeat
    public AjaxResult sendCode(@Validated @RequestBody WalletVo04 walletVo) {
        Dict data = chatUserService.sendCode(walletVo.getPhone(), null, MessageTypeEnum.WALLET);
        return AjaxResult.success(data);
    }

    /**
     * 设置密码
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping(value = "/setPass")
    @SubmitRepeat
    public AjaxResult setPass(@Validated @RequestBody WalletVo05 walletVo) {
        walletInfoService.setPass(walletVo);
        return AjaxResult.success();
    }

}
