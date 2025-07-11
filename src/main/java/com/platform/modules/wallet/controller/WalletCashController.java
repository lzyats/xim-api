package com.platform.modules.wallet.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.wallet.service.WalletCashService;
import com.platform.modules.wallet.vo.WalletVo02;
import com.platform.modules.wallet.vo.WalletVo06;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 提现
 */
@RestController
@Slf4j
@RequestMapping("/wallet/cash")
public class WalletCashController extends BaseController {

    @Resource
    private WalletCashService walletCashService;

    /**
     * 获取配置
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/getConfig")
    public AjaxResult getConfig() {
        WalletVo02 data = walletCashService.getConfig();
        return AjaxResult.success(data);
    }

    /**
     * 提现申请
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping(value = "/apply")
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    public AjaxResult apply(@Validated @RequestBody WalletVo06 walletVo) {
        walletCashService.apply(walletVo);
        return AjaxResult.success();
    }

}
