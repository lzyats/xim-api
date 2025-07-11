package com.platform.modules.wallet.service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 钱包容量 服务层
 * </p>
 */
public interface WalletConfigService {

    /**
     * 查询列表
     */
    List<BigDecimal> queryList();
}
