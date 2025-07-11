package com.platform.modules.wallet.dao;

import com.platform.modules.wallet.domain.WalletBank;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 钱包卡包 数据库访问层
 * </p>
 */
@Repository
public interface WalletBankDao extends BaseDao<WalletBank> {

    /**
     * 查询列表
     */
    List<WalletBank> queryList(WalletBank walletBank);

}
