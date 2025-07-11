package com.platform.modules.wallet.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.service.WalletRechargeService;
import com.platform.modules.wallet.vo.WalletVo01;
import com.platform.modules.wallet.vo.WalletVo03;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 充值
 */
@RestController
@Slf4j
@RequestMapping("/wallet/recharge")
public class WalletRechargeController extends BaseController {

    @Resource
    private WalletRechargeService walletRechargeService;

    /**
     * 获取配置
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/getConfig")
    public AjaxResult getConfig() {
        WalletVo03 data = walletRechargeService.getConfig();
        return AjaxResult.success(data);
    }

    /**
     * 获取充值金额
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getPayAmount")
    public AjaxResult getPayAmount() {
        List<BigDecimal> dataList = walletRechargeService.getPayAmount();
        return AjaxResult.success(dataList);
    }

    /**
     * 获取支付类型
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getPayType")
    public AjaxResult getPayType() {
        List<String> dataList = walletRechargeService.getPayType();
        return AjaxResult.success(dataList);
    }

    /**
     * 账号充值
     */
    @SubmitRepeat(exception = YesOrNoEnum.NO)
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/submit")
    public AjaxResult submit(@Validated @RequestBody WalletVo01 walletVo) {
        if (TradePayEnum.SYS_PAY.equals(walletVo.getPayType())) {
            throw new BaseException("支付方式不能为空");
        }
        String result = walletRechargeService.submit(walletVo);
        return AjaxResult.success(result);
    }

    /**
     * 支付回调
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/notifyAli")
    public String notifyAli(HttpServletRequest request) {
        return walletRechargeService.notify(request, TradePayEnum.ALI_PAY);
    }

    /**
     * 支付回调
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/notifyWx")
    public String notifyWx(HttpServletRequest request) {
        return walletRechargeService.notify(request, TradePayEnum.WX_PAY);
    }

}
