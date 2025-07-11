package com.platform.modules.wallet.service;

import com.platform.common.web.service.BaseService;
import com.platform.modules.wallet.domain.WalletInfo;
import com.platform.modules.wallet.vo.WalletVo05;

import java.math.BigDecimal;

/**
 * <p>
 * 钱包 服务层
 * </p>
 */
public interface WalletInfoService extends BaseService<WalletInfo> {

    /**
     * 新增钱包
     */
    void addWallet(Long userId);

    /**
     * 获取详情
     */
    BigDecimal getInfo(Long userId);

    /**
     * 验证验证码
     */
    void setPass(WalletVo05 walletVo);

    /**
     * 增加余额
     */
    BigDecimal addBalance(Long userId, BigDecimal amount);

    /**
     * 增加余额
     */
    BigDecimal addTransactional(Long userId, BigDecimal amount);

    /**
     * 扣减余额
     */
    BigDecimal subtractBalance(Long userId, BigDecimal amount, String password);
}
