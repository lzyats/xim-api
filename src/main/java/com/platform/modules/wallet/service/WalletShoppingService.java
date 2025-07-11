package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletShopping;
import com.platform.modules.wallet.domain.WalletTrade;

/**
 * <p>
 * 钱包消费 服务层
 * </p>
 */
public interface WalletShoppingService extends BaseService<WalletShopping> {

    /**
     * 写入
     */
    void addShopping(WalletTrade trade);
}
