package com.platform.modules.wallet.dao;

import com.platform.common.web.dao.BaseDao;
import com.platform.modules.wallet.domain.WalletPacket;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 钱包红包 数据库访问层
 * </p>
 */
@Repository
public interface WalletPacketDao extends BaseDao<WalletPacket> {

    /**
     * 查询列表
     */
    List<WalletPacket> queryList(WalletPacket walletPacket);

}
