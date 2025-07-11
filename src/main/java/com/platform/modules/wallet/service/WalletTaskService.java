package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletTask;
import com.platform.modules.wallet.domain.WalletTrade;

/**
 * <p>
 * 钱包任务 服务层
 * </p>
 */
public interface WalletTaskService extends BaseService<WalletTask> {

    /**
     * 增加任务
     */
    void addTask(WalletTrade trade);

    /**
     * 执行任务
     */
    void task();

    /**
     * 执行任务
     */
    void doTask(WalletTask walletTask);

}
