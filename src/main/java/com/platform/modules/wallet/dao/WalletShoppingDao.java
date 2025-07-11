package com.platform.modules.wallet.dao;

import com.platform.modules.wallet.domain.WalletShopping;
import org.springframework.stereotype.Repository;
import com.platform.common.web.dao.BaseDao;
import java.util.List;

/**
 * <p>
 * 钱包消费 数据库访问层
 * </p>
 */
@Repository
public interface WalletShoppingDao extends BaseDao<WalletShopping> {

    /**
     * 查询列表
     */
    List<WalletShopping> queryList(WalletShopping walletShopping);

}
