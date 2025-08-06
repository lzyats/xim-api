package com.platform.modules.wallet.controller;

import com.platform.common.aspectj.SubmitRepeat;
import com.platform.common.aspectj.VersionRepeat;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.redis.RedisUtils;
import com.platform.common.web.controller.BaseController;
import com.platform.common.web.domain.AjaxResult;
import com.platform.common.web.version.VersionEnum;
import com.platform.modules.chat.service.impl.ChatNoticeServiceImpl;
import com.platform.modules.wallet.service.WalletCashService;
import com.platform.modules.wallet.vo.WalletVo02;
import com.platform.modules.wallet.vo.WalletVo06;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.ChatConfigEnum;
import java.math.BigDecimal;  // 必须引入的包
import java.math.RoundingMode; // 可选（如果需要设置舍入模式）

import com.platform.common.constant.AppConstants;

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

    @Autowired
    private  RedisUtils redisUtils;

    private static final Logger logger = LoggerFactory.getLogger(ChatNoticeServiceImpl.class);


    /**
     * 获取配置
     */
    @VersionRepeat(VersionEnum.V1_0_0)
    @GetMapping(value = "/getConfig")
    public AjaxResult getConfig() {
        //验证缓存
        String redisKey = AppConstants.REDIS_WALLET_ROBOT+"config";
        if (redisUtils.hasKey(redisKey)) {
            logger.info("从缓存查询配置");
            // 从Redis获取值
            String authCode = redisUtils.hGet(redisKey, "wallet_cash_auth").toString(); // 假设返回"Y"
            WalletVo02 data=new WalletVo02()
                    .setCost(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_cost"))
                    .setMax(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_max"))
                    .setMin(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_min"))
                    .setRemark(redisUtils.hGet(redisKey,"wallet_cash_remark").toString())
                    .setRate(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_rate"))
                    .setRates(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_rates"))
                    .setCount(Integer.parseInt(getWalletCashProperty(redisUtils,redisKey, "wallet_cash_count").toString()))
                    .setRemark(redisUtils.hGet(redisKey,"wallet_cash_remark").toString())
                    .setAuth(authCode)
                    ;
            return AjaxResult.success(data);
        }
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

    /**
     * 从redis中获取哈希值
     * @param redisKey
     * @param field
     * @return
     */
    public static BigDecimal getWalletCashProperty(RedisUtils redisUtils,String redisKey,String field) {
        String costStr = redisUtils.hGet(redisKey, field).toString();
        if (costStr == null || costStr.isEmpty()) {
            return BigDecimal.ZERO; // 处理空值情况
        }
        try {
            return new BigDecimal(costStr);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO; // 转换失败时返回默认值
        }
    }

}
