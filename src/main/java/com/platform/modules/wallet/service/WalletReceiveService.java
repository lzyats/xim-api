package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletReceive;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包余额 服务层
 * </p>
 */
public interface WalletReceiveService extends BaseService<WalletReceive> {

    /**
     * 新增接收
     */
    void addReceive(Long tradeId, Long userId, BigDecimal amount);

    /**
     * 执行任务
     */
    void task();

    /**
     * 执行任务
     */
    void doTask(WalletReceive receive);

    /**
     * 执行任务
     */
    void once(String param);

}
