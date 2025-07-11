package com.platform.modules.wallet.controller;

import com.github.pagehelper.PageInfo;
import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.page.TableDataInfo;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.wallet.enums.TradeTypeEnum;
import com.platform.modules.wallet.service.WalletTradeService;
import com.platform.modules.wallet.vo.TradeVo06;
import com.platform.modules.wallet.vo.TradeVo11;
import com.platform.modules.wallet.vo.TradeVo12;
import com.platform.modules.wallet.vo.TradeVo15;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 交易
 */
@RestController
@Slf4j
@RequestMapping("/wallet/trade")
public class WalletTradeController extends BaseController {

    @Resource
    private WalletTradeService walletTradeService;

    /**
     * 账单列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getTradeList")
    public TableDataInfo getTradeList(TradeTypeEnum tradeType) {
        startPage("createTime desc");
        return getDataTable(walletTradeService.getTradeList(tradeType));
    }

    /**
     * 账单详情
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getTradeInfo/{tradeId}")
    public AjaxResult getTradeInfo(@PathVariable Long tradeId) {
        return AjaxResult.success(walletTradeService.getTradeInfo(tradeId));
    }

    /**
     * 删除账单
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/removeTrade/{tradeId}")
    public AjaxResult removeTrade(@PathVariable Long tradeId) {
        walletTradeService.removeTrade(tradeId);
        return AjaxResult.success();
    }

    /**
     * 扫码支付
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/transfer")
    public AjaxResult transfer(@Validated @RequestBody TradeVo06 tradeVo) {
        return AjaxResult.success(walletTradeService.sendScan(tradeVo));
    }

    /**
     * 接收操作
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @SubmitRepeat
    @GetMapping("/doReceive/{tradeId}")
    public AjaxResult doReceive(@PathVariable Long tradeId) {
        BigDecimal data = walletTradeService.doReceive(tradeId);
        return AjaxResult.success(data);
    }

    /**
     * 接收详情
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getSender/{tradeId}")
    public AjaxResult getSender(@PathVariable Long tradeId) {
        TradeVo11 data = walletTradeService.getSender(tradeId);
        return AjaxResult.success(data);
    }

    /**
     * 接收列表
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getReceiver/{tradeId}")
    public AjaxResult getReceiver(@PathVariable Long tradeId) {
        List<TradeVo12> dataList = walletTradeService.getReceiver(tradeId);
        return AjaxResult.success(dataList);
    }

    /**
     * 群组红包
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping("/getGroupPacket/{groupId}")
    public TableDataInfo getGroupPacket(@PathVariable Long groupId) {
        PageInfo pageInfo = walletTradeService.getGroupPacket(groupId);
        return getDataTable(pageInfo);
    }

    /**
     * 商品支付
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @PostMapping("/payment")
    public AjaxResult payment(@Validated @RequestBody TradeVo15 tradeVo) {
        String appId = tradeVo.getAppId();
        String goodsName = tradeVo.getGoodsName();
        String orderNo = tradeVo.getOrderNo();
        BigDecimal goodsPrice = tradeVo.getGoodsPrice();
        String password = tradeVo.getPassword();
        walletTradeService.payment(appId, goodsName, goodsPrice, orderNo, password);
        return AjaxResult.success();
    }

}
