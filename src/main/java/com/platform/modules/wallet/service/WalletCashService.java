package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletCash;
import com.platform.modules.wallet.vo.WalletVo02;
import com.platform.modules.wallet.vo.WalletVo06;

/**
 * <p>
 * 钱包提现 服务层
 * </p>
 */
public interface WalletCashService extends BaseService<WalletCash> {

    /**
     * 获取配置
     */
    WalletVo02 getConfig();

    /**
     * 提现申请
     */
    void apply(WalletVo06 walletVo);
}
