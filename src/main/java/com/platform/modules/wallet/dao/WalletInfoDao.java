package com.platform.modules.wallet.dao;

import com.platform.modules.wallet.domain.WalletInfo;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;

import java.util.List;

/**
 * <p>
 * 钱包 数据库访问层
 * </p>
 */
@Repository
public interface WalletInfoDao extends BaseDao<WalletInfo> {

    /**
     * 查询列表
     */
    List<WalletInfo> queryList(WalletInfo walletInfo);

}
