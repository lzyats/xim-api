package com.platform.modules.wallet.controller;

import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.wallet.service.WalletBankService;
import com.platform.modules.wallet.vo.TradeVo07;
import com.platform.modules.wallet.vo.TradeVo08;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 银行卡
 */
@RestController
@Slf4j
@RequestMapping("/wallet/bank")
public class WalletBankController extends BaseController {

    @Resource
    private WalletBankService walletBankService;

    /**
     * 列表数据
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/getList")
    public AjaxResult getList() {
        List<TradeVo07> dataList = walletBankService.queryDataList();
        return AjaxResult.success(dataList);
    }

    /**
     * 新增数据
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/add")
    public AjaxResult add(@Validated @RequestBody TradeVo08 tradeVo) {
        walletBankService.addBank(tradeVo);
        return AjaxResult.success();
    }

    /**
     * 删除数据
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable Long id) {
        walletBankService.deleteBank(id);
        return AjaxResult.success();
    }

}
