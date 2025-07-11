package com.platform.modules.wallet.dao;

import com.platform.modules.wallet.domain.WalletReceive;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 钱包余额 数据库访问层
 * </p>
 */
@Repository
public interface WalletReceiveDao extends BaseDao<WalletReceive> {

    /**
     * 查询列表
     */
    List<WalletReceive> queryList(WalletReceive walletReceive);

}
