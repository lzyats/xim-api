package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletRecharge;
import com.platform.modules.wallet.enums.TradePayEnum;
import com.platform.modules.wallet.vo.WalletVo01;
import com.platform.modules.wallet.vo.WalletVo03;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 钱包充值 服务层
 * </p>
 */
public interface WalletRechargeService extends BaseService<WalletRecharge> {

    /**
     * 获取配置
     */
    WalletVo03 getConfig();

    /**
     * 获取充值金额
     */
    List<BigDecimal> getPayAmount();

    /**
     * 获取支付类型
     */
    List<String> getPayType();

    /**
     * 支付充值
     */
    String submit(WalletVo01 walletVo);

    /**
     * 支付回调
     */
    String notify(HttpServletRequest request, TradePayEnum payType);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);
}
