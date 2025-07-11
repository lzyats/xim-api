package com.platform.modules.wallet.dao;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.dao.BaseDao;
import com.platform.modules.wallet.domain.WalletTrade;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 钱包交易 数据库访问层
 * </p>
 */
@Repository
public interface WalletTradeDao extends BaseDao<WalletTrade> {

    /**
     * 查询列表
     */
    List<WalletTrade> queryList(WalletTrade walletTrade);

    /**
     * 查询列表
     */
    List<WalletTrade> getGroupPacket(Long groupId, Long userId, YesOrNoEnum status);

}
