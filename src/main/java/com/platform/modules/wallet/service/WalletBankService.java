package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletBank;
import com.platform.modules.wallet.vo.TradeVo08;
import com.platform.modules.wallet.vo.TradeVo07;

import java.util.List;

/**
 * <p>
 * 钱包卡包 服务层
 * </p>
 */
public interface WalletBankService extends BaseService<WalletBank> {

    /**
     * 列表
     */
    List<TradeVo07> queryDataList();

    /**
     * 新增
     */
    void addBank(TradeVo08 tradeVo);

    /**
     * 删除
     */
    void deleteBank(Long bankId);

}
