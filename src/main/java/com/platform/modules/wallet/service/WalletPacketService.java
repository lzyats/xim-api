package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletPacket;
import com.platform.modules.wallet.domain.WalletTrade;
import com.platform.modules.wallet.vo.TradeVo12;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 钱包红包 服务层
 * </p>
 */
public interface WalletPacketService extends BaseService<WalletPacket> {

    /**
     * 抢单记录
     */
    void addPacket(Long tradeId, BigDecimal amount);

    /**
     * 抢单记录
     */
    BigDecimal getAmount(Long tradeId);

    /**
     * 查询列表
     */
    List<TradeVo12> queryDataList(WalletTrade trade);

    /**
     * 查询记录
     */
    WalletPacket queryPacket(Long tradeId, Long userId);

    /**
     * 修改昵称
     */
    void editNickname(String nickname);

    /**
     * 修改头像
     */
    void editPortrait(String portrait);

    /**
     * 计算余额
     */
    BigDecimal calculateBalance(Long tradeId);

}
