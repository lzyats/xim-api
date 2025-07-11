package com.platform.modules.wallet.dao;

import com.platform.modules.wallet.domain.WalletTask;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 钱包任务 数据库访问层
 * </p>
 */
@Repository
public interface WalletTaskDao extends BaseDao<WalletTask> {

    /**
     * 查询列表
     */
    List<WalletTask> queryList(WalletTask walletTask);

}
